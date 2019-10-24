package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

/* For QR code  */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


@Controller
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;


    @GetMapping("/flightsearchform")
    public String showFlightSearchForm(Model model){
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("flight", new Flight());
        ArrayList<Flight> flights = flightRepository.findAll();
        Flight flight = new Flight();
        Set<String> airports = flight.getAirportList(flights);
        model.addAttribute("airports", airports);
        return "flightsearchform";
    }

    @PostMapping("/processflightsearch")
    public String processFlightSearch(@ModelAttribute("reservation") Reservation reservation, Model model,
                                      @RequestParam(name="numberPassengers") int numPass,
                                      @RequestParam(name="SearchSelectorRT") String rtrip,
                                      @RequestParam(name="SearchSelectorPassClass") String passClass,
                                      @RequestParam(name = "SearchSelectorDepApt") String depApt,
                                      @RequestParam(name = "SearchSelectorArrApt") String arrApt
                                      /*@RequestParam(name = "depDate") String depDate,
                                      @RequestParam(name = "retDate") String retDate*/) {

        model.addAttribute("depFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(depApt, arrApt));
        model.addAttribute("retFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(arrApt, depApt));

//        boolean isRoundTrip;
        if (rtrip.equals("RoundTrip")) {
//            isRoundTrip = true;
            reservation.setRoundTrip(true);

        } else {
//            isRoundTrip = false;
            reservation.setRoundTrip(false);
        }
//        model.addAttribute("isRoundTrip", isRoundTrip);

/*
        String pattern = "yyyy-MM-dd";
        try {
            String formattedDepDate = depDate.substring(1);
            SimpleDateFormat simpleDepDateFormat = new SimpleDateFormat(pattern);
            Date realDepDate = simpleDepDateFormat.parse(formattedDepDate);
            model.addAttribute("depDate", realDepDate);
        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        try {
            String formattedRetDate = retDate.substring(1);
            SimpleDateFormat simpleRetDateFormat = new SimpleDateFormat(pattern);
            Date realRetDate = simpleRetDateFormat.parse(formattedRetDate);
            model.addAttribute("retDate", realRetDate);
        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }
*/
        reservation.setNumberPassengers(numPass);
        reservation.setFlightClass(passClass);
        model.addAttribute(reservation);

/*        model.addAttribute("numPass", numPass);
        model.addAttribute("passClass", passClass);
        model.addAttribute("depApt", depApt);
        model.addAttribute("arrApt", arrApt);*/

        return "redirect:/listSearchResults";
    }

    @GetMapping("/listSearchResults")
    public String showSearchResultsForm(Model model, @RequestParam("reservation") Reservation reservation,
                                        @RequestParam("depFlights") Iterable<Flight> depFlights,
                                        @RequestParam("retFlights") Iterable<Flight> retFlights){

        model.addAttribute("depFlights", depFlights);

        System.out.println(((HashSet<Flight>) depFlights).size());
        if (reservation.isRoundTrip() == true)
            model.addAttribute("retFlights", retFlights);
        else
            model.addAttribute("retFlights", null);

        Set<Passenger> passengers = new HashSet<Passenger>();
        for (int i = 0; i < reservation.getNumberPassengers(); i++) {
            passengers.add(new Passenger());
        }
        reservation.setPassengers(passengers);
        model.addAttribute("reservation", reservation);

        return "listsearchresults";
    }

    public double getTotalTripPrice(Reservation reservation){
        Flight departureFlight = reservation.getDepartureFlight();
        double pricePerPassDep = departureFlight.getPricePerPassenger(reservation.getFlightClass(),departureFlight.getBasePrice());
        double windowPrice = 5.00;
        int numPass = reservation.getNumberPassengers();
        double totalTripPrice = pricePerPassDep * numPass;
        if (reservation.isRoundTrip()) {
            Flight returnFlight = reservation.getReturnFlight();
            double pricePerPassRet = returnFlight.getPricePerPassenger(reservation.getFlightClass(), returnFlight.getBasePrice());
            windowPrice = 10.00;
            totalTripPrice += pricePerPassRet * numPass;
        }
        Set<Passenger> passengers = reservation.getPassengers();
        for(Passenger passenger : passengers){
            if(passenger.getIsWindow()==true){
                totalTripPrice += windowPrice;
            }
        }
        return totalTripPrice;
    }

    /**
     * The following Request Mapping is responsible for creating The QR code for a user id and reservation id.
     * @param model
     * @param userId
     * @param reservationId
     * @return
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping("/createQRCodeURL")
    public String createQRCodeURL(Model model, @RequestParam("user_id") String userId, @RequestParam("reservation_id") String reservationId)
            throws WriterException, IOException {
        System.out.println("Entered createQRCodeURL");
        String fileType = "png";
        int size = 125;
        String filePath = "QRcode.png";
        File qrFile = new File(filePath);

        StringBuilder qrCodeText = new StringBuilder();
        qrCodeText.append("http://localhost:8080?user_id=");
        qrCodeText.append(userId);
        qrCodeText.append("&reservation_id=");
        qrCodeText.append(reservationId);

        createQRImage(qrFile, qrCodeText.toString(), size, fileType);

        model.addAttribute("qrCodeURL", qrCodeText.toString());

        return "passengerlist";
    }

    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {

        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, fileType, qrFile);
    }

}
