package ua.com.spiritus;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    @Value("${smpp.host}")
    @Getter
    private String host;
    @Value("${smpp.port}")
    @Getter
    private Integer port;
    @Value("${smpp.systemId}")
    @Getter
    private String systemId;
    @Value("${smpp.password}")
    @Getter
    private String password;

    @Autowired
    private Environment environment;

    public String getPhoneNumber() {
        return environment.getProperty("test.dst.number");
    }
}
