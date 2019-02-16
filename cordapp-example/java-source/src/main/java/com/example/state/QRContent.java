package com.example.state;

import com.google.gson.Gson;
import net.corda.core.contracts.UniqueIdentifier;

public class QRContent {
    private final Float totalValue; // full value of transaction
    private final Float taxValue; // amount of tax on transaction paid
    private final UniqueIdentifier transId; // Unique transaction id
    private final UniqueIdentifier companyId;


    public QRContent(Float totalValue, Float taxValue, UniqueIdentifier transId, UniqueIdentifier companyId) {
        this.totalValue = totalValue;
        this.taxValue = taxValue;
        this.transId = transId;
        this.companyId = companyId;
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

    public String serialize(){
        return new Gson().toJson(this);
    }

    public static QRContent deserialize(String jsonString){
        return new Gson().fromJson(jsonString, QRContent.class);
    }
}
