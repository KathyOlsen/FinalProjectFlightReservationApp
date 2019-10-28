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
    public String processFlightSearch(@ModelAttribute("reservation") Reservation reservation,
                                      Model model,
                                      @RequestParam(name="SearchSelectorNumPass") int numPass,
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

        if (rtrip.equals("RoundTrip")) {
            reservation.setIsRoundTrip(true);

        } else {
            reservation.setIsRoundTrip(false);
        }

        String pattern = "yyyy-MM-dd";
        try {
            String formattedDepDate = depDate.substring(0);
            SimpleDateFormat simpleDepDateFormat = new SimpleDateFormat(pattern);
            Date realDepDate = simpleDepDateFormat.parse(formattedDepDate);
            reservation.setDepartureDate(realDepDate);

        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        if (reservation.getIsRoundTrip()==true) {
            try {
                String formattedRetDate = retDate.substring(0);
                SimpleDateFormat simpleRetDateFormat = new SimpleDateFormat(pattern);
                Date realRetDate = simpleRetDateFormat.parse(formattedRetDate);
                reservation.setReturnDate(realRetDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        reservation.setNumberPassengers(numPass);
        reservation.setFlightClass(passClass);
        request.setAttribute("reservation", reservation);
        System.out.println("test 0a (depDate in procflightsearch): " + reservation.getDepartureDate());
        return "forward:/listSearchResults";
    }

    @RequestMapping("/listSearchResults")
    public String showSearchResultsForm(HttpServletRequest request,
                                        Model model){

        Reservation reservation = (Reservation) request.getAttribute("reservation");
        model.addAttribute("depFlights", request.getAttribute("depFlights"));
        System.out.println("test 0b (depDate in listsearchresults): " + reservation.getDepartureDate());
        if (reservation.getIsRoundTrip() == true) {
            model.addAttribute("retFlights", request.getAttribute("retFlights"));
        }
        else {
            model.addAttribute("retFlights", null);
        }
        Passenger passenger = new Passenger();
        model.addAttribute("passenger", passenger);
        model.addAttribute("reservation", reservation);
        request.setAttribute("reservation", reservation);

        return "listsearchresults";
    }

    @PostMapping("/confirmflight")
    public String confirmflight(@Valid Reservation reservation, BindingResult result,
//            @ModelAttribute("reservation") Reservation reservation,
                                     Model model,
                                     @RequestParam(name="depFlightRadio") Flight depFlight,
                                     @RequestParam(name="retFlightRadio") Optional<Flight> retFlight,
                                     HttpServletRequest request){
        System.out.println("test0bc depdate from valid reservation in confirmflight): " + reservation.getDepartureDate());
//        Reservation reservation = (Reservation) request.getAttribute("reservation");
//        System.out.println("test 0c (depDate from r(=request) in confirmflight): " + reservation.getDepartureDate());
        reservation.setDepartureFlight(depFlight);
        System.out.println("test 1a (depFlight): " + depFlight.getId());
        System.out.println("test 1b (res.depFlight): " + reservation.getDepartureFlight().getId());
        if(reservation.getIsRoundTrip()==true) {
            Flight realReturnFlight = retFlight.get();
            reservation.setReturnFlight(realReturnFlight);
            System.out.println("test 2aa (retFlight.toString): " + retFlight.toString());
            System.out.println("test 2a (realretFlight): " + realReturnFlight.getId());
            System.out.println("test 2b (res.realretFlight): " + reservation.getReturnFlight().getId());
        }
        User user = userService.getUser();
        reservation.setUser(user);
        System.out.println("test 3 (username): " + user.getUsername());
        Collection<Passenger> passengers = new ArrayList<>();
        reservation.setPassengers(passengers);
        reservationRepository.save(reservation);
        request.setAttribute("reservation", reservation);
//        request.setAttribute("passengers", passengers);

        return "forward:/passengerform";
    }

    @RequestMapping("/passengerform")
    public String getPassengerForm(Model model,
                                   HttpServletRequest request){
        Reservation reservation = (Reservation) request.getAttribute("reservation");
        model.addAttribute("reservation", reservation);
        model.addAttribute("passenger", new Passenger());
        request.setAttribute("reservation", reservation);
        System.out.println("test 0d (res.depDate in passengerform): " + reservation.getDepartureDate());
        System.out.println("test 4 (res.depFlight in passform): " + reservation.getDepartureFlight().getId());
        return "passengerform";
    }

    @PostMapping("/processpassenger")
    public String processForm( @Valid Passenger passenger,
                               BindingResult result,
                               Model model,
//                               @ModelAttribute("reservation") Reservation reservation,
//                               @ModelAttribute("passengers") Collection<Passenger> passengers,
//                               @ModelAttribute("passenger") Passenger passenger,
                               @RequestParam(name = "seatNumber") String seatNumber,
                               HttpServletRequest request){
        Reservation reservation = (Reservation) request.getAttribute("reservation");
        System.out.println("test 0e (res.depDate in processpassenger): " + reservation.getDepartureDate());
        if(result.hasErrors()) {
            return "passengerform";
        }
        System.out.println("test 5 (seatNumber in processpass): " + seatNumber);
        if(seatNumber.endsWith("A") | seatNumber.endsWith("F")){
                passenger.setIsWindow(true);
            } else {
                passenger.setIsWindow(false);
            }
        passenger.setReservation(reservation);
        reservation.getPassengers().add(passenger);
        passengerRepository.save(passenger);
        if(reservation.getPassengers().size() < reservation.getNumberPassengers()) {
            request.setAttribute("reservation", reservation);
            return "forward:/passengerform";
        }else{
            reservationRepository.save(reservation);
            request.setAttribute("reservation", reservation);
            return "forward:/showboardingpass";
        }
    }

    @RequestMapping("/showboardingpass")
    public String showBoardingPass(Model model,
                                   HttpServletRequest request){
        Reservation reservation = (Reservation) request.getAttribute("reservation");
        model.addAttribute("reservation", reservation);
        double totalTripPrice = getTotalTripPrice(reservation);
        model.addAttribute("totalTripPrice", totalTripPrice);
        Long userId = reservation.getUser().getId();
        String userID = userId.toString();
        Long reservationId = reservation.getId();
        String reservationID = reservationId.toString();
        String qrCodeUrl = null;
        try {
            qrCodeUrl = createQRCodeURL(model, userID, reservationID);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("qrCodeUrl", qrCodeUrl);
        return "boardingpass";
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
    
    public String createQRCodeURL(Model model,
                                  String userId,
                                  String reservationId)
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

        return qrCodeText.toString();
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

    @RequestMapping("/sampleboardingpass")
    public String getSampleBoardingPass(Model model){
        User jwoods = userRepository.findByUsername("jwoods");
        Reservation reservation = reservationRepository.findByUser(jwoods).get(0);
        model.addAttribute("reservation", reservation);
        Collection<Passenger> passengers = reservation.getPassengers();
        model.addAttribute("passengers", passengers);
        System.out.println("passengers.size: " + passengers.size());
        for(Passenger p : passengers) {
            System.out.println("passenger.getFirstName: " + p.getFirstName());
        }
        double ttp = getTotalTripPrice(reservation);
        model.addAttribute("ttp", ttp);
        Long userId = jwoods.getId();
        String userID = userId.toString();
        Long reservationId = reservation.getId();
        String reservationID = reservationId.toString();
        String qrCodeUrl = null;
        try {
            qrCodeUrl = createQRCodeURL(model, userID, reservationID);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("qrCodeUrl", qrCodeUrl);
        return "boardingpass";
    }

}
