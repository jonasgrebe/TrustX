package com.example.contract;

import com.example.state.BuyerTransState;
import com.example.state.POSTransState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.transactions.LedgerTransaction;

import javax.validation.constraints.Null;
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
public class BuyerContract implements Contract {
    public static final String BUYER_CONTRACT_ID = "com.example.contract.BuyerContract";

    /**
     * The verify() function of all the states' contracts must not throw an exception for a transaction to be
     * considered valid.
     */
    @Override
    public void verify(LedgerTransaction tx) {
        final CommandWithParties<Commands.Create> command = requireSingleCommand(tx.getCommands(), Commands.Create.class);
        requireThat(require -> {
            // Generic constraints around the POS transaction.

            final BuyerTransState out = tx.outputsOfType(BuyerTransState.class).get(0);
            require.using("File path should not be null",
                    out.getQrCodeFilePath().length() > 1);
            require.using("All of the participants must be signers.",
                    command.getSigners().containsAll(out.getParticipants().stream().map(AbstractParty::getOwningKey).collect(Collectors.toList())));


            // CHECK AFTER QR Processing
            require.using("Tax Value should not be empty",
                    out.getTotalValue() >= 0 );
            require.using("Total Tax Value should be >=0",
                    out.getTaxValue() >= 0 );
            require.using("There must be a transaction ID.",
                    out.getTransId() != null);
            require.using("There has to be a hash",
                    out.getTxHash() != null);
            require.using("Buyer value should be empty",
                    out.getBuyer() != null);
            require.using("Government should not be null",
                        out.getGov() != null);



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