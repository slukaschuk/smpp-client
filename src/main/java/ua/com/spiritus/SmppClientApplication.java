package ua.com.spiritus;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class SmppClientApplication{

    public static void main(String[] args) {
        SpringApplication.run(SmppClientApplication.class, args);
    }
}
