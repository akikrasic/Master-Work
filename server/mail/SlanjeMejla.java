package srb.akikrasic.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by aki on 7/19/17.
 */
@Configuration
@ComponentScan(basePackages={"srb.akikrasic.mail"})
public class SlanjeMejla {

    @Bean
    public JavaMailSenderImpl zaSlanje(){
        JavaMailSenderImpl m = new JavaMailSenderImpl();
        m.setHost("smtp.gmail.com");
        m.setPort(587);
        m.setUsername("akikrasicmasterrad");
        m.setPassword("novasifra");

        Properties p = new Properties();
        p.put("mail.smtp.starttls.enable", "true");
        m.setJavaMailProperties(p);
        return m;
    }
}
