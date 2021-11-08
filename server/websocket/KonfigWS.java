package srb.akikrasic.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import srb.akikrasic.websocket.klase.*;

/**
 * Created by aki on 9/26/17.
 */

@Configuration
@EnableWebSocket
public class KonfigWS implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mojRukovaoc(),"/wsProba")
                .setAllowedOrigins("*");
        registry.addHandler(obavestenja(),"/wsObavestenja")
                .setAllowedOrigins("*");
        registry.addHandler(adminRacun(),"/wsAdminRacun")
                .setAllowedOrigins("*");
        registry.addHandler(korisnikRacun(),"/wsKorisnikRacun")
                .setAllowedOrigins("*");
        registry.addHandler(razgovor(),"/wsRazgovor")
                .setAllowedOrigins("*");

    }
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer(){
        ServletServerContainerFactoryBean c = new ServletServerContainerFactoryBean();
        c.setMaxTextMessageBufferSize(8192);
        c.setMaxBinaryMessageBufferSize(8192);
        return c;
    }

    @Bean
    public WebSocketHandler mojRukovaoc(){
        return new ProbaHandler();
    }
    @Bean
    public WebSocketHandler obavestenja(){
        return new ObavestenjaRukovalac();
    }
    @Bean
    public WebSocketHandler adminRacun(){
        return new AdminRacunRukovalac();
    }
    @Bean
    public WebSocketHandler korisnikRacun(){
        return new KorisnikRacunRukovalac();
    }
    @Bean
    public WebSocketHandler razgovor(){
        return new RazgovorRukovalac();
    }
}
