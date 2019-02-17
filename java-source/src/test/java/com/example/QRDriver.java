package com.example;

import com.example.state.QRCode;
import com.example.state.QRContent;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;

import java.util.HashMap;
import java.util.Map;

public class QRDriver {
    public static void main(String[] args) {
        Float totalValue = 40.0F;
        Float taxValue = 10.0F;
        UniqueIdentifier transId = new UniqueIdentifier();
        UniqueIdentifier companyId = new UniqueIdentifier();
        SecureHash txHash = SecureHash.randomSHA256();
        //String txHash = txSecHash.toString();

        QRContent qrContent = new QRContent(totalValue, taxValue, transId, companyId, txHash);

        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        String filePath = "QR_"+ 42 + ".png";


        //JSONSerializer ser = new JSONSerializer();

        // CREATE QR CODE
        try {
            QRCode.createQRCode(qrContent.serialize(), filePath, charset, hintMap, 200, 200);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        // READ QR CODE
        String qrContentString = "";
        try {
            qrContentString = QRCode.readQRCode(filePath, charset, hintMap);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        System.out.print("STRING");
        System.out.println(qrContentString);

        //JSONDeserializer<QRContent> des = new JSONDeserializer<QRContent>();
        //JSONDeserializer<QRContent> der = new JSONDeserializer<QRContent>();
        QRContent outContent = QRContent.deserialize(qrContentString);

        System.out.println(outContent.getTotalValue());
        System.out.println(outContent.getTaxValue());
        System.out.println(outContent.getTransId());
        System.out.println(outContent.getCompanyId());
        System.out.println(outContent.getTxHash());



    }
}
