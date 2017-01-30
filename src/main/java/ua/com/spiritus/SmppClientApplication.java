package ua.com.spiritus;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import java.io.IOException;
import java.util.Date;

@SpringBootApplication
@Log4j2
public class SmppClientApplication {


    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(SmppClientApplication.class, args);
        SMPPSession session = ctx.getBean("smppSession", SMPPSession.class);
        AppService appService = ctx.getBean("appService", AppService.class);
        String msg  = "My test message";
        try {
            session.connectAndBind(appService.getHost(), appService.getPort(),
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
            log.error("Failed connect and bind to host");
        }
        try {
            String messageId = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616",
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657",
                    new ESMClass(), (byte) 0, (byte) 1, timeFormatter.format(new Date()), null, new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT),
                    (byte) 0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
                    "jSMPP simplify SMPP on Java platform".getBytes());
            log.info("Message submitted, message_id is " + messageId+" and the message is - "+msg);
        } catch (PDUException e) {
            // Invalid PDU parameter
            log.error("Invalid PDU parameter");
        } catch (ResponseTimeoutException e) {
            // Response timeout
            log.error("Response timeout");
        } catch (InvalidResponseException e) {
            // Invalid response
            log.error("Receive invalid respose");
        } catch (NegativeResponseException e) {
            // Receiving negative response (non-zero command_status)
            log.error("Receive negative response");
        } catch (IOException e) {
            log.error("IO error occur");
        }
        session.unbindAndClose();

/*


		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();*/
    }

    @Bean
    public SMPPSession smppSession() throws IOException {
        SMPPSession session = new SMPPSession();
        return session;
    }

}
