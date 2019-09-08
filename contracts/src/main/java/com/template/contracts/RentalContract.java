package com.template.contracts;

import com.template.states.RentalState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;


public class RentalContract implements Contract {
    public static final String ID = "com.template.contracts.RentalContract";

    // Our Create command.
    public static class Create implements CommandData {
    }

    @Override
    public void verify(LedgerTransaction tx) {
        final CommandWithParties<RentalContract.Create> command = requireSingleCommand(tx.getCommands(), RentalContract.Create.class);

        // Constraints on the shape of the transaction.
        if (!tx.getInputs().isEmpty())
            throw new IllegalArgumentException("No inputs should be consumed when issuing a Rental.");
        if (!(tx.getOutputs().size() == 1))
            throw new IllegalArgumentException("There should be one output state of type RentalState.");

        // specific constraints.
        final RentalState output = tx.outputsOfType(RentalState.class).get(0);
        final Party owner = output.getOwner();
        final Party renter = output.getRenter();
        if (output.getVinNumber() == null)
            throw new IllegalArgumentException("The vehicle must be specified by vin number.");
        if (owner.equals(renter))
            throw new IllegalArgumentException("The renter and owner cannot be the same entity.");

        // Constraints on the signers.
        final List<PublicKey> requiredSigners = command.getSigners();
        final List<PublicKey> expectedSigners = Arrays.asList(owner.getOwningKey(), renter.getOwningKey());
        if (requiredSigners.size() != 2)
            throw new IllegalArgumentException("There must be two signers.");
        if (!(requiredSigners.containsAll(expectedSigners)))
            throw new IllegalArgumentException("The renter and owner must be signers.");

    }
}