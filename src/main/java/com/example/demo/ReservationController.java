package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

/**
 * Class ReservationController
 */
@Controller
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;


    /**
     *
     * @param model
     * @return
     */
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

    /**
     *
     * @param reservation
     * @param model
     * @param numPass
     * @param rtrip
     * @param passClass
     * @param depApt
     * @param arrApt
     * @param depDate
     * @param retDate
     * @param request
     * @return
     */
    @PostMapping("/processflightsearch")
    public String processFlightSearch(@ModelAttribute("reservation") Reservation reservation,
                                      Model model,
                                      @RequestParam(name="numberPassengers") int numPass,
                                      @RequestParam(name="SearchSelectorRT") String rtrip,
                                      @RequestParam(name="SearchSelectorPassClass") String passClass,
                                      @RequestParam(name = "SearchSelectorDepApt") String depApt,
                                      @RequestParam(name = "SearchSelectorArrApt") String arrApt,
                                      @RequestParam(name = "depDate") String depDate,
                                      @RequestParam(name = "retDate") String retDate,
                                      HttpServletRequest request) {

        request.setAttribute("depFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(depApt, arrApt));
        request.setAttribute("retFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(arrApt, depApt));
/*        model.addAttribute("depFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(depApt, arrApt));
        model.addAttribute("retFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(arrApt, depApt));*/

//        boolean isRoundTrip;
//        Reservation r = reservation;
        if (rtrip.equals("RoundTrip")) {
//            isRoundTrip = true;
            reservation.setIsRoundTrip(true);

        } else {
//            isRoundTrip = false;
            reservation.setIsRoundTrip(false);
        }
//        model.addAttribute("isRoundTrip", isRoundTrip);

        String pattern = "yyyy-MM-dd";
        try {
            String formattedDepDate = depDate.substring(1);
            SimpleDateFormat simpleDepDateFormat = new SimpleDateFormat(pattern);
            Date realDepDate = simpleDepDateFormat.parse(formattedDepDate);
//            model.addAttribute("depDate", realDepDate);
            reservation.setDepartureDate(realDepDate);

        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        if (reservation.getIsRoundTrip()) {
            try {
                String formattedRetDate = retDate.substring(1);
                SimpleDateFormat simpleRetDateFormat = new SimpleDateFormat(pattern);
                Date realRetDate = simpleRetDateFormat.parse(formattedRetDate);
//            model.addAttribute("retDate", realRetDate);
                reservation.setReturnDate(realRetDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        reservation.setNumberPassengers(numPass);
        reservation.setFlightClass(passClass);
//        model.addAttribute(reservation);
        request.setAttribute("reservation", reservation);

/*        model.addAttribute("numPass", numPass);
        model.addAttribute("passClass", passClass);
        model.addAttribute("depApt", depApt);
        model.addAttribute("arrApt", arrApt);*/

        return "forward:/listSearchResults";
    }

    /**
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/listSearchResults")
    public String showSearchResultsForm(HttpServletRequest request, Model model){

        Reservation r = (Reservation) request.getAttribute("reservation");
        model.addAttribute("depFlights", request.getAttribute("depFlights"));

        if (r.getIsRoundTrip() == true) {
            model.addAttribute("retFlights", request.getAttribute("retFlights"));
        }
        else {
            model.addAttribute("retFlights", null);
        }
        Passenger passenger = new Passenger();
        model.addAttribute("passenger", passenger);
//        Collection<Passenger> passengers = new ArrayList<>();
//        for (int i = 0; i < r.getNumberPassengers(); i++) {
//            passengers.add(new Passenger());
//        }
//        r.setPassengers(passengers);
//        model.addAttribute("passengers", passengers);


        model.addAttribute("reservation", r);

        return "listsearchresults";
    }

    /**
     *
     * @param reservation
     * @param model
     * @param depFlight
     * @param retFlight
     * @param p1id
     * @param p1firstName
     * @param p1lastName
     * @param p1seatNumber
     * @param p2id
     * @param p2firstName
     * @param p2lastName
     * @param p2seatNumber
     * @return
     */
    @PostMapping("/confirmReservation")
    public String confirmReservation(@ModelAttribute("reservation") Reservation reservation,
                                     Model model,
                                     @RequestParam(name="depFlight") Flight depFlight,
                                     @RequestParam(name="retFlight") Flight retFlight,
                                     @RequestParam(name="p1id") Long p1id,
                                     @RequestParam(name = "p1firstName") String p1firstName,
                                     @RequestParam(name = "p1lastName") String p1lastName,
                                     @RequestParam(name = "p1seatNumber") int p1seatNumber,
                                     @RequestParam(name="p2id") Long p2id,
                                     @RequestParam(name = "p2firstName") String p2firstName,
                                     @RequestParam(name = "p2lastName") String p2lastName,
                                     @RequestParam(name = "p2seatNumber") int p2seatNumber) {
        //Still need to add isWindowSeat (first change listSearchResults)
        reservation.setDepartureFlight(depFlight);
        reservation.setReturnFlight(retFlight);
        Passenger passenger1 = new Passenger();
        passenger1.setId(p1id);
        passenger1.setFirstName(p1firstName);
        passenger1.setLastName(p1lastName);
        passenger1.setSeatNumber(p1seatNumber);
        Passenger passenger2 = new Passenger();
        passenger2.setId(p2id);
        passenger2.setFirstName(p2firstName);
        passenger2.setLastName(p2lastName);
        passenger2.setSeatNumber(p2seatNumber);
        Collection<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger1);
        passengers.add(passenger2);
        reservation.setPassengers(passengers);
        User user = userService.getUser();
        reservation.setUser(user);
        reservationRepository.save(reservation);
        model.addAttribute("reservation", reservation);
        return "/showboardingpass";
    }

    @RequestMapping("/showboardingpass")
    public String showBoardingPass(Model model,
                                   Reservation reservation){
//        code here


        return "/boardingpass";
    }

    /**
     *
     * @param reservation
     * @return
     */

    public double getTotalTripPrice(Reservation reservation){
        Flight departureFlight = reservation.getDepartureFlight();
        double pricePerPassDep = departureFlight.getPricePerPassenger(reservation.getFlightClass(),departureFlight.getBasePrice());
        double windowPrice = 5.00;
        int numPass = reservation.getNumberPassengers();
        double totalTripPrice = pricePerPassDep * numPass;
        if (reservation.getIsRoundTrip()==true) {
            Flight returnFlight = reservation.getReturnFlight();
            double pricePerPassRet = returnFlight.getPricePerPassenger(reservation.getFlightClass(), returnFlight.getBasePrice());
            windowPrice = 10.00;
            totalTripPrice += pricePerPassRet * numPass;
        }
        Collection<Passenger> passengers = reservation.getPassengers();
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
        System.out.println(qrCodeText.toString());
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
