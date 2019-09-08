package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.states.RentalState;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(RentalFlow.class)
public class RentalFlowResponder extends FlowLogic<Void> {
    private final FlowSession otherPartySession;

    public RentalFlowResponder(FlowSession otherPartySession) {
        this.otherPartySession = otherPartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        class SignTxFlow extends SignTransactionFlow {
            private SignTxFlow(FlowSession otherPartySession) {
                super(otherPartySession);
            }

            @Override
            protected void checkTransaction(SignedTransaction stx) {
                requireThat(require -> {
                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    require.using("This must be a rental transaction.", output instanceof RentalState);
                    RentalState rental = (RentalState) output;
                    return null;
                });
            }
        }

        SecureHash expectedTxId = subFlow(new SignTxFlow(otherPartySession)).getId();

        subFlow(new ReceiveFinalityFlow(otherPartySession, expectedTxId));

        return null;
    }
}
