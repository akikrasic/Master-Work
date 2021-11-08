package srb.akikrasic.websocket.klase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import srb.akikrasic.token.Tokeni;

import java.io.IOException;

/**
 * Created by aki on 9/26/17.
 */
public class ProbaHandler extends TextWebSocketHandler {
    @Autowired
    private Tokeni tokeni;

    public void handleTextMessage(WebSocketSession sesija, TextMessage poruka) throws IOException {
        System.out.println(new String(poruka.asBytes()));
        System.out.println(poruka.getPayload());
        TextMessage t = new TextMessage("SERVER ODGOVARA: "+poruka.getPayload());
        sesija.sendMessage(t);
        System.out.println(tokeni);

    }
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        super.afterConnectionEstablished(session);
        System.out.println(session);
    }
    public ProbaHandler(){
        System.out.println("napravljen");
    }


}
