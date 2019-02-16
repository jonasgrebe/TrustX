package com.example.state;

import com.example.schema.IOUSchemaV1;
import com.google.common.collect.ImmutableList;
import kotlinx.html.SVG;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import javax.servlet.http.Part;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * The state object recording IOU agreements between two parties.
 *
 * A state must implement [ContractState] or one of its descendants.
 */
public class BuyerTransState implements LinearState, QueryableState {
    private final Party buyer;
    private final Party gov;

    // instance variables for the decoded qr code information
     private final Float totalValue;
     private final Float taxValue;
     private final UniqueIdentifier transId;
     private final UniqueIdentifier companyId;

    /**
     * @param qrCodeFile file of the scanned in qr code
     * @param buyer the party generating the transaction
     * @param gov the government validating the transactions against user submissions
     */
    public BuyerTransState(File qrCodeFile,
                           Party buyer,
                           Party gov)
    {
        this.buyer = buyer;
        this.gov = gov;

        // TODO: Extract information out of qr code file
        try {
            String decodedFile = QRCodeReader.decodeQRCode(qrCodeFile);
            System.out.println(decodedFile);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        // TODO: fill in information from qr code
        this.totalValue = null;
        this.taxValue = null;
        this.transId = null;
        this.companyId = null;
    }

    @Override public UniqueIdentifier getLinearId() { return null; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(buyer, gov);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        return null;
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new IOUSchemaV1());
    }

    @Override
    public String toString() {
        return String.format("BuyerTransState(...)");
    }

}