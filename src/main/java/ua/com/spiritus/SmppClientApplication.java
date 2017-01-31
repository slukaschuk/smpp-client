package ua.com.spiritus;

import lombok.extern.log4j.Log4j2;
import org.jsmpp.session.SMPPSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Log4j2
public class SmppClientApplication{

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SmppClientApplication.class, args);
    }


    @Bean
    public SMPPSession smppSession() {
        SMPPSession session = new SMPPSession();
        return session;
    }

}
