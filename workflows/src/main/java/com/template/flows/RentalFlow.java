package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.RentalContract;
import com.template.states.RentalState;
import net.corda.core.contracts.Command;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//renter requests rental for a certain period
@InitiatingFlow
@StartableByRPC
public class RentalFlow extends FlowLogic<Void> {
    private final String startDay;
    private final String endDay;
    private final String startTime;
    private final String endTime;
    private final String dailyPrice;
    private final String vinNumber;
    private final Party otherParty;
    private final List<?> images;

    /**
     * The progress tracker provides checkpoints indicating the progress of the flow to observers.
     */
    private final ProgressTracker progressTracker = new ProgressTracker();

    public RentalFlow(String vinNumber, Party otherParty, String dailyPrice, String startDay, String endDay, String startTime, String endTime, List<?> images) {
        this.startDay = startDay;
        this.endDay = endDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dailyPrice = dailyPrice;
        this.vinNumber = vinNumber;
        this.otherParty = otherParty;
        this.images = images;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    /**
     * The flow logic is encapsulated within the call() method.
     */
    @Suspendable
    @Override
    public Void call() throws FlowException {
// We retrieve the notary identity from the network map.
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

// We create the transaction components.
        RentalState outputState = new RentalState(images, vinNumber, dailyPrice, startDay, endDay, startTime, endTime, getOurIdentity(), otherParty);
        List<PublicKey> requiredSigners = Arrays.asList(getOurIdentity().getOwningKey(), otherParty.getOwningKey());
        Command command = new Command<>(new RentalContract.Create(), requiredSigners);

// We create a transaction builder and add the components.
        TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(outputState, RentalContract.ID)
                .addCommand(command);

// Verifying the transaction.
        txBuilder.verify(getServiceHub());

// Signing the transaction.
        SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

// Creating a session with the other party.
        FlowSession otherPartySession = initiateFlow(otherParty);

// Obtaining the counterparty's signature.
        SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(
                signedTx, Arrays.asList(otherPartySession), CollectSignaturesFlow.tracker()));

// Finalising the transaction.
        subFlow(new FinalityFlow(fullySignedTx, otherPartySession));

        return null;
    }
}
