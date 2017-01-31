package ua.com.spiritus.services;

import lombok.extern.log4j.Log4j2;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.spiritus.models.SmsMessage;

import java.io.IOException;
import java.util.Date;

@Service
@Log4j2
public class MessageServiceImpl implements MessageService {

    @Autowired
    private AppService appService;

    @Autowired
    private SMPPSession smppSession;

    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

    @Override
    public boolean sendMessage(final SmsMessage smsMessage) {
        try {
            smppSession.connectAndBind(appService.getHost(), appService.getPort(),
                    new BindParameter(
                            BindType.BIND_TX,
                            appService.getSystemId(),
                            appService.getPassword(),
                            "cp",
                            TypeOfNumber.UNKNOWN,
                            NumberingPlanIndicator.UNKNOWN,
                            null));
        } catch (IOException e) {
            System.out.println("Failed connect and bind to host");
            log.error("Failed connect and bind to host: " + e.getMessage());
        }
        try {
            String messageId = smppSession.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, smsMessage.getSenderNumber(),
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, smsMessage.getReceiverNumber(),
                    new ESMClass(), (byte) 0, (byte) 1, timeFormatter.format(new Date()), null, new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT),
                    (byte) 0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
                    smsMessage.getMessage().getBytes());
            log.info("Message submitted, message_id is " + messageId+" and the message is - "+ smsMessage.getMessage());
        } catch (PDUException e) {
            // Invalid PDU parameter
            log.error("Invalid PDU parameter");
            return false;
        } catch (ResponseTimeoutException e) {
            // Response timeout
            log.error("Response timeout");
            return false;
        } catch (InvalidResponseException e) {
            // Invalid response
            log.error("Receive invalid respose");
            return false;
        } catch (NegativeResponseException e) {
            // Receiving negative response (non-zero command_status)
            log.error("Receive negative response");
            return false;
        } catch (IOException e) {
            log.error("IO error occur");
            return false;
        }
        smppSession.unbindAndClose();
        return true;
    }
}
