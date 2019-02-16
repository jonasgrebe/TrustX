//package com.example.state;
//
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import net.corda.core.contracts.CommandAndState;
//import net.corda.core.contracts.OwnableState;
//import net.corda.core.identity.AbstractParty;
//import net.corda.core.identity.Party;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class QRGenState implements OwnableState {
//
//    public Party owner;
//
//    public QRGenState(Party owner, QRContent qrContent) {
//        this.owner = owner;
//
//        String charset = "UTF-8"; // or "ISO-8859-1"
//        Map hintMap = new HashMap();
//        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//
//        QRCode.createQRCode(qrContent.serialize(), );
//    }
//
//    @NotNull
//    @Override
//    public AbstractParty getOwner() {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public CommandAndState withNewOwner(@NotNull AbstractParty newOwner) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public List<AbstractParty> getParticipants() {
//        return null;
//    }
//}
