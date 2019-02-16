package com.example.state;

import com.google.gson.Gson;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;

public class QRContent {
    private final Float totalValue; // full value of transaction
    private final Float taxValue; // amount of tax on transaction paid
    private final UniqueIdentifier transId; // Unique transaction id
    private final UniqueIdentifier companyId;
    private final SecureHash txHash;


    public QRContent(Float totalValue, Float taxValue, UniqueIdentifier transId, UniqueIdentifier companyId, SecureHash txHash) {
        this.totalValue = totalValue;
        this.taxValue = taxValue;
        this.transId = transId;
        this.companyId = companyId;
        this.txHash = txHash;
    }

    public Float getTaxValue() {
        return taxValue;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public UniqueIdentifier getCompanyId() {
        return companyId;
    }

    public UniqueIdentifier getTransId() {
        return transId;
    }

    public SecureHash getTxHash() {
        return txHash;
    }

    public String serialize(){
        return new Gson().toJson(this);
    }

    public static QRContent deserialize(String jsonString){
        return new Gson().fromJson(jsonString, QRContent.class);
    }
}
