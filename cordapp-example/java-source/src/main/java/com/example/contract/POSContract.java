package com.example.contract;

import com.example.state.POSTransState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.transactions.LedgerTransaction;

import java.util.stream.Collectors;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * A implementation of a basic smart contract in Corda.
 *
 * This contract enforces rules regarding the creation of a valid [IOUState], which in turn encapsulates an [IOU].
 *
 * For a new [IOU] to be issued onto the ledger, a transaction is required which takes:
 * - Zero input states.
 * - One output state: the new [IOU].
 * - An Create() command with the public keys of both the lender and the borrower.
 *
 * All contracts must sub-class the [Contract] interface.
 */
public class POSContract implements Contract {
    public static final String POS_CONTRACT_ID = "com.example.contract.POSContract";

    /**
     * The verify() function of all the states' contracts must not throw an exception for a transaction to be
     * considered valid.
     */
    @Override
    public void verify(LedgerTransaction tx) {
        final CommandWithParties<Commands.Create> command = requireSingleCommand(tx.getCommands(), Commands.Create.class);
        requireThat(require -> {
            // Generic constraints around the POS transaction.
            require.using("Only one output state should be created.",
                    tx.getOutputs().size() == 1);


            final POSTransState out = tx.outputsOfType(POSTransState.class).get(0);
            require.using("The totalValue should be greater than 0",
                    out.getTotalValue() > 0);
            require.using("Tax value should be >= 0",
                    out.getTaxValue() >= 0);
            require.using("Total liability should by >= taxValue",
                    out.getTotalLiability() >= out.getTaxValue());

//          DO we need requirements for composition of the QR properties

            require.using("The sender and the gov cannot be the same entity.",
                    out.getSeller() != out.getGov());
            require.using("All of the participants must be signers.",
                    command.getSigners().containsAll(out.getParticipants().stream().map(AbstractParty::getOwningKey).collect(Collectors.toList())));

            return null;
        });
    }

    /**
     * This contract only implements one command, Create.
     */
    public interface Commands extends CommandData {
        class Create implements Commands {}
    }
}