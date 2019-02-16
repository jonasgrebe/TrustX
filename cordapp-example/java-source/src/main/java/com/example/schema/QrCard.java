package com.example.schema;

import net.glxn.qrgen.core.scheme.VCard;

public class QrCard extends VCard {

    private String transId;
    private String companyId;
    private String totalValue;
    private String taxValue;

    public QrCard() { super(); }

    public String getTransId() {
        return transId;
    }

    public QrCard setTransId(String transId) {
        this.transId = transId;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public QrCard setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public QrCard setTotalValue(String totalValue) {
        this.totalValue = totalValue;
        return this;
    }

    public String getTaxValue() {
        return taxValue;
    }

    public QrCard setTaxValue(String taxValue) {
        this.taxValue = taxValue;
        return this;
    }



}

