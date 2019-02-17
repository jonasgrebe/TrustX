package com.example.contract;

import com.example.state.BuyerTransState;
import com.google.common.collect.ImmutableList;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import static com.example.contract.BuyerContract.BUYER_CONTRACT_ID;
import static net.corda.testing.node.NodeTestUtils.ledger;

public class BuyerContractTests {
    static private final MockServices ledgerServices = new MockServices();
    static private TestIdentity joeBuyer = new TestIdentity(new CordaX500Name("JoeBuyer", "Birmingham", "GB"));
    static private TestIdentity taxGov = new TestIdentity(new CordaX500Name("taxGov", "Sussex", "GB"));
    static private String qrCodeFilePath = "/home/anixon604/dev/samples/cordapp-example/QR_42.png";

    @Test
    public void transactionMustIncludeCreateCommand() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BUYER_CONTRACT_ID, new BuyerTransState(qrCodeFilePath, joeBuyer.getParty(), taxGov.getParty()));
                tx.fails();
                tx.command(ImmutableList.of(joeBuyer.getPublicKey(), taxGov.getPublicKey()), new BuyerContract.Commands.Create());
                tx.verifies();
                return null;
            });
            return null;
        }));
    }

    @Test
    public void userMustSignTransaction() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BUYER_CONTRACT_ID, new BuyerTransState(qrCodeFilePath, joeBuyer.getParty(), taxGov.getParty()));
                tx.command(taxGov.getPublicKey(), new BuyerContract.Commands.Create());
                tx.failsWith("All of the participants must be signers.");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void govMustSignTransaction() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BUYER_CONTRACT_ID, new BuyerTransState(qrCodeFilePath, joeBuyer.getParty(), taxGov.getParty()));
                tx.command(joeBuyer.getPublicKey(), new BuyerContract.Commands.Create());
                tx.failsWith("All of the participants must be signers.");
                return null;
            });
            return null;
        }));
    }

//    @Test
//    public void filePathMustNotBeNull() {
//        ledger(ledgerServices, (ledger -> {
//            ledger.transaction(tx -> {
//                tx.output(BUYER_CONTRACT_ID, new BuyerTransState("", joeBuyer.getParty(), taxGov.getParty()));
//                tx.command(ImmutableList.of(joeBuyer.getPublicKey(), taxGov.getPublicKey()), new BuyerContract.Commands.Create());
//                tx.failsWith("File path should not be null");
//                return null;
//            });
//            return null;
//        }));
//    }


}