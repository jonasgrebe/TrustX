package com.example.state;

import com.example.schema.IOUSchemaV1;
import com.google.common.collect.ImmutableList;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import kotlinx.html.SVG;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import javax.servlet.http.Part;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     private final String txHash;
     //private final SecureHash txHash;
     private final String qrCodeFilePath;

    /**
     * @param qrCodeFilePath file of the scanned in qr code
     * @param buyer the party generating the transaction
     * @param gov the government validating the transactions against user submissionn
     */
    public BuyerTransState(String qrCodeFilePath,
                           Party buyer,
                           Party gov)
    {
        this.buyer = buyer;
        this.gov = gov;
        this.qrCodeFilePath = qrCodeFilePath;

        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        String qrContentString = "";
        try {
             qrContentString = QRCode.readQRCode(qrCodeFilePath, charset, hintMap);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        QRContent qrContent = QRContent.deserialize(qrContentString);


        this.totalValue = qrContent.getTotalValue();
        this.taxValue = qrContent.getTaxValue();
        this.transId = qrContent.getTransId();
        this.companyId = qrContent.getCompanyId();
        this.txHash = qrContent.getTxHash();
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public Float getTaxValue() {
        return taxValue;
    }

    public UniqueIdentifier getTransId() {
        return transId;
    }

    public UniqueIdentifier getCompanyId() {
        return companyId;
    }

    public String getTxHash() {
        return txHash;
    }

    public Party getBuyer() {
        return buyer;
    }

    public Party getGov() {
        return gov;
    }

    public String getQrCodeFilePath() { return qrCodeFilePath; }


    @Override public UniqueIdentifier getLinearId() { return null; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(buyer, gov);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) { return null; }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new IOUSchemaV1());
    }

    @Override
    public String toString() {
        return String.format("BuyerTransState(...)");
    }

}