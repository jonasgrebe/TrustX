package com.example.contract;

import com.example.state.POSTransState;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import static com.example.contract.POSContract.POS_CONTRACT_ID;
import static net.corda.testing.node.NodeTestUtils.ledger;

public class POSContractTests {
    static private final MockServices ledgerServices = new MockServices();
    static private TestIdentity samsElectric = new TestIdentity(new CordaX500Name("SamsElectric", "London", "GB"));
    static private TestIdentity taxGov = new TestIdentity(new CordaX500Name("taxGov", "Sussex", "GB"));
    static private Float totalValue = 100.0F;
    static private Float taxValue = 10.0F;
    static private Float totalLiability = 330.0F;

    @Test
    public void transactionMustIncludeCreateCommand() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, taxValue, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.fails();
                tx.command(ImmutableList.of(samsElectric.getPublicKey(), taxGov.getPublicKey()), new POSContract.Commands.Create());
                tx.verifies();
                return null;
            });
            return null;
        }));
    }

    @Test
    public void transactionMustHaveOneOutput() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, taxValue, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, taxValue, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(ImmutableList.of(samsElectric.getPublicKey(), taxGov.getPublicKey()), new POSContract.Commands.Create());
                tx.failsWith("Only one output state should be created.");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void totalValueGreaterThanZero() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(-1.0F, taxValue, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(ImmutableList.of(samsElectric.getPublicKey(), taxGov.getPublicKey()), new POSContract.Commands.Create());
                tx.failsWith("The totalValue should be greater than 0");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void taxValueGreaterOrEqualZero() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, -2.0F, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(ImmutableList.of(samsElectric.getPublicKey(), taxGov.getPublicKey()), new POSContract.Commands.Create());
                tx.failsWith("Tax value should be >= 0");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void liabilityGreaterThanTaxValue() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, 44.0F, 5.3F, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(ImmutableList.of(samsElectric.getPublicKey(), taxGov.getPublicKey()), new POSContract.Commands.Create());
                tx.failsWith("Total liability should by >= taxValue");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void sellerNotGov() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, taxValue, totalLiability, samsElectric.getParty(), samsElectric.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(ImmutableList.of(samsElectric.getPublicKey(), taxGov.getPublicKey()), new POSContract.Commands.Create());
                tx.failsWith("The sender and the gov cannot be the same entity.");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void sellerMustSignTransaction() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, taxValue, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(taxGov.getPublicKey(), new POSContract.Commands.Create());
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
                tx.output(POS_CONTRACT_ID, new POSTransState(totalValue, taxValue, totalLiability, samsElectric.getParty(), taxGov.getParty(), new UniqueIdentifier(), new UniqueIdentifier()));
                tx.command(samsElectric.getPublicKey(), new POSContract.Commands.Create());
                tx.failsWith("All of the participants must be signers.");
                return null;
            });
            return null;
        }));
    }

}