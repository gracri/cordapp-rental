package com.template.webserver;

import com.template.flows.RentalFlow;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(Controller.class);
    private static final String template = "Hello, %s!";
    private Rental currentRental = null;

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/start-rental-flow", produces = "text/plain")
    private String startRentalFlow() {

        String partyName = currentRental.getOwner();
        String dailyPrice = currentRental.getDailyPrice();
        String startDay = currentRental.getStartDay();
        String endDay = currentRental.getEndDay();
        String startTime = currentRental.getStartTime();
        String endTime = currentRental.getEndTime();
        ArrayList<List<String>> images = currentRental.getImages();
        String vinNumber = "1234567";


        if(partyName == null){
            return "Query parameter 'partyName' must not be null.\n";
        }
        if (Integer.parseInt(dailyPrice) <= 0 ) {
            return "Daily price must be non-negative.\n";
        }

        CordaX500Name partyX500Name = CordaX500Name.parse(partyName);
        Party otherParty = proxy.wellKnownPartyFromX500Name(partyX500Name);

        try {
            CordaFuture<Void> signedTx = proxy.startTrackedFlowDynamic(RentalFlow.class, vinNumber, otherParty, dailyPrice, startDay, endDay, startTime, endTime, images).getReturnValue();
            return ("Transaction id: " + signedTx + "comitted to ledger");

        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(value = "/initiate-rental", produces = "text/plain")
    private String createRentalEvent(@RequestBody Rental rental) {
        currentRental = rental;

        return "rental created with renter: " + rental.getRenter() + ", owner: " + rental.getOwner() ;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(value = "/update-rental", produces = "text/plain")
    private String updateRentalEvent(@RequestBody Rental rental) {
        currentRental.setDailyPrice(rental.getDailyPrice());
        currentRental.setStartDay(rental.getStartDay());
        currentRental.setEndDay(rental.getEndDay());
        currentRental.setStartTime(rental.getStartTime());
        currentRental.setEndTime(rental.getEndTime());

        return "rental updated";
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(value = "/create-image",  produces = "text/plain")
    private String createIOU(@RequestBody Image image) {
        currentRental.addImage(image);
        return "Image saved successfully: ";
    }
}