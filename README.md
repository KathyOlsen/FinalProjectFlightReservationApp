# FinalProjectFlightReservationApp
Java 8-Week Boot Camp Fall 2019 Week 8

Heroku URL

    http://______
  
Project Developers

    -John Kneisler
    
    -Daniel Krause
    
    -Katherine K. Olsen
    
This app allows a user to search flights on a single airline, select a departure and return flight, provide passenger names, log in to finalize a reservation, select seats, and receive a boarding pass with a QR code containing reservation details.

Users can submit a search form without logging in that includes:

    -Round-Trip vs. One-Way
    
    -Number of Passengers
    
    -Class (Economy, Business, or First)
    
    -Departure Airport
    
    -Destination Airport
    
    -Departure Date
    
    -Return Date (if applicable)
    
The app will display separate lists of departure flights and return flights (if applicable) that meet the search criteria, or a message if no matching flights are found.  The flight search results include for each flight:

    -Flight date, departure time, arrival time, and flight duration
    
    -Departure airport and arrival airport names and airport codes
    
    -Flight cost per passenger, which varies depending on the flight class originally selected

The boarding pass the user receives includes flight details and a QR code. The total flight cost includes a $5 fee for each window seat selected.

The user can:

    -Search flights without logging in
    
    -Make flight reservations after logging in
    
    -After logging in, select a link in the navigation bar to see their own flight history.

The Administrator upon logging in has access to their own Administrator Links page via a link in the navigation bar visible only to the Administrator.  The Administrator can:

    -View a list of users and details of each user
    
    -View a list of roles and details of each role, including which users hold that role
    
    -View a list of flights
    
    -Add flights
    
The following credentials may be used to test the app:

    -Administrator:
        
        - username: auser  ;  password: pwdau
        
    -User:
    
        - username: jwoods  ;  password: pwdjw


