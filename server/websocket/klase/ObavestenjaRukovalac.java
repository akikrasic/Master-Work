package srb.akikrasic.websocket.klase;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import srb.akikrasic.websocket.ZajednickiRukovalacZaWebSocket;

import java.util.LinkedHashMap;

/**
 * Created by aki on 10/11/17.
 */
@Component
public class ObavestenjaRukovalac extends ZajednickiRukovalacZaWebSocket {

    private static LinkedHashMap<String, WebSocketSession> mapaSesijaZaSvakogKorisnika = new LinkedHashMap<>();
    private static LinkedHashMap< WebSocketSession, String> mapaObrnutaZaLogout= new LinkedHashMap<>();

    public LinkedHashMap<String, WebSocketSession> getMapaSesijaZaSvakogKorisnika(){
        return ObavestenjaRukovalac.mapaSesijaZaSvakogKorisnika;
    }
    public LinkedHashMap< WebSocketSession, String> getMapaObrnutaZaLogout(){
        return ObavestenjaRukovalac.mapaObrnutaZaLogout;
    }

    @Override
    public Object vratiteObjekatZaJson() {
        porukaZaJson.setPoruka((String)objekatZaSlanje);
        return porukaZaJson;
    }


    private PorukaZaJson porukaZaJson= new PorukaZaJson();
    private class PorukaZaJson {
        private String poruka;

        public String getPoruka() {
            return poruka;
        }

        public void setPoruka(String poruka) {
            this.poruka = poruka;
        }
    }

}