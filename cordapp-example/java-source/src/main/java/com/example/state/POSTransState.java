package com.example.state;

import com.example.schema.IOUSchemaV1;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import java.util.Arrays;
import java.util.List;

/**
 * The state object recording IOU agreements between two parties.
 *
 * A state must implement [ContractState] or one of its descendants.
 */
public class POSTransState implements LinearState, QueryableState {
    private final Float totalValue; // full value of transaction
    private final Float taxValue; // amount of tax on transaction paid
    private final Party seller; // seller or vender
    private final Party gov; // government authority node

    private final UniqueIdentifier linearId;

    /**
     * @param totalValue the total value of the transaction.
     * @param taxValue the tax paid on the transaction
     * @param seller the party generating the transaction
     * @param gov the government validating the transactions against user submissions
     */
    public POSTransState(Float totalValue ,
                          Float taxValue ,
                          Party seller,
                          Party gov ,
                          UniqueIdentifier linearId)
    {
        this.totalValue = totalValue;
        this.taxValue = taxValue;
        this.seller = seller;
        this.gov = gov;
        this.linearId = linearId;
    }

    public Float getTotalValue() { return totalValue; }
    public Float getTaxValue() { return taxValue; }
    public Party getSeller() { return seller; }
    @Override public UniqueIdentifier getLinearId() { return linearId; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(seller, gov);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof IOUSchemaV1) {
            return new IOUSchemaV1.PersistentIOU(
                    this.seller.getName().toString(),
                    this.gov.getName().toString(),
                    1, // random value to satisfy the arguments
                    this.linearId.getId());
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new IOUSchemaV1());
    }

    @Override
    public String toString() {
        return String.format("POSTransState(totalValue=%s, taxValue=%s, seller=%s, gov=%s, linearId=%s)", totalValue, taxValue, seller, gov, linearId);
    }
}