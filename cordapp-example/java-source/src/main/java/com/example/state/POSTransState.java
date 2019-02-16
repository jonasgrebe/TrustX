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
import net.glxn.qrgen.core.scheme.VCard;

import javax.servlet.http.Part;
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
    private final Float totalLiability; // total amount cumulative tax owing
    private final Party seller; // seller or vender
    private final Party gov; // government authority node
    //private final SVG qrTransCode; // QR code for transaction

    private final UniqueIdentifier transId; // Unique transaction id
    private final UniqueIdentifier companyId;

    /**
     * @param totalValue the total value of the transaction.
     * @param taxValue the tax paid on the transaction
     * @param seller the party generating the transaction
     * @param gov the government validating the transactions against user submissions
     */
    public POSTransState(Float totalValue ,
                          Float taxValue ,
                          Float totalLiability ,
                          Party seller,
                          Party gov ,
                          UniqueIdentifier transId,
                          UniqueIdentifier companyId) // represents company
    {
        this.totalValue = totalValue;
        this.taxValue = taxValue;
        this.totalLiability = totalLiability;
        this.seller = seller;
        this.gov = gov;
        this.transId = transId;
        this.companyId = companyId;


// SAMPLE for Vcard
//        // encode contact data as vcard using defaults
//        VCard johnDoe = new VCard("John Doe")
//                .setEmail("john.doe@example.org")
//                .setAddress("John Doe Street 1, 5678 Doestown")
//                .setTitle("Mister")
//                .setCompany("John Doe Inc.")
//                .setPhoneNumber("1234")
//                .setWebsite("www.example.org");
//        QRCode.from(johnDoe).file();

    }

    public Float getTotalValue() { return totalValue; }
    public Float getTaxValue() { return taxValue; }
    public Float getTotalLiability() { return  totalLiability; }
    public Party getSeller() { return seller; }
    public Party getGov() { return gov; }
    //public SVG getQrTransCode() { return qrTransCode; }

    public UniqueIdentifier getTransId() { return transId; }
    @Override public UniqueIdentifier getLinearId() { return getCompanyId(); }
    public UniqueIdentifier getCompanyId() { return companyId; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(seller, gov);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof IOUSchemaV1) {
            return new IOUSchemaV1.PersistentIOU(
                    this.seller.getName().toString(),
                    this.gov.getName().toString(),
                    1, // random value to satisfy the arguments
                    this.companyId.getId());
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new IOUSchemaV1());
    }

    @Override
    public String toString() {
        return String.format("POSTransState(totalValue=%s, taxValue=%s, seller=%s, gov=%s, companyId=%s)", totalValue, taxValue, seller, gov, companyId);
    }
}