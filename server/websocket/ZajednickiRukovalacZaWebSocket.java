package srb.akikrasic.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Tokeni;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aki on 11/6/17.
 */
public abstract class ZajednickiRukovalacZaWebSocket extends TextWebSocketHandler {
    @Autowired
    protected Tokeni tokeni;

    @Autowired
    protected GenerisanjeTokena genTokena;

    public abstract LinkedHashMap<String, WebSocketSession> getMapaSesijaZaSvakogKorisnika();
    public abstract LinkedHashMap< WebSocketSession, String> getMapaObrnutaZaLogout();

    protected Object objekatZaSlanje;

    private ObjectMapper om = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage poruka) throws IOException {
        String text =  poruka.getPayload();

        HashMap<String, Object> mapa=om.readValue(text, new TypeReference<Map<String, String>>(){});
        this.obraditePoruku(mapa, session);
    }

    public void obraditePoruku(HashMap<String, Object> mapa, WebSocketSession sesija){
        Object emailO = mapa.get("email");
        Object tokenO = mapa.get("token");
        if(emailO==null||tokenO==null)return;
        String email = genTokena.dekodujteURLFormat(emailO.toString());
        String token = genTokena.dekodujteURLFormat(tokenO.toString());
        if(tokeni.daLiJeUlogovanKorisnik(email, token)){
            getMapaSesijaZaSvakogKorisnika().put(email, sesija);
            getMapaObrnutaZaLogout().put(sesija, email);
        }
    }

    public abstract Object vratiteObjekatZaJson();

    protected TextMessage napraviteTextMessage()  {
        TextMessage t =null;
        try {
            t = new TextMessage(om.writeValueAsString(vratiteObjekatZaJson()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public void posaljite(String email,Object objekatZaSlanje){
        this.objekatZaSlanje=objekatZaSlanje;
        posaljiteTextMessage(email, napraviteTextMessage());
    }

    public void posaljiteTextMessage(String email, TextMessage poruka){
        WebSocketSession sesija = getMapaSesijaZaSvakogKorisnika().get(email);
        if(sesija==null)return;
        try {
            sesija.sendMessage(poruka);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);


    }

    public void odjavaKorisnika(String email){
        WebSocketSession sesija= getMapaSesijaZaSvakogKorisnika().get(email);
        getMapaSesijaZaSvakogKorisnika().remove(email);
        getMapaObrnutaZaLogout().remove(sesija);
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String email = getMapaObrnutaZaLogout().get(session);
        if(email!=null){
            getMapaSesijaZaSvakogKorisnika().remove(email);
            getMapaObrnutaZaLogout().remove(session);
        }
    }

}
