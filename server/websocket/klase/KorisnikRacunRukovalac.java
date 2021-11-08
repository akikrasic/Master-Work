package srb.akikrasic.websocket.klase;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import srb.akikrasic.websocket.ZajednickiRukovalacZaWebSocket;

import java.util.LinkedHashMap;

/**
 * Created by aki on 11/6/17.
 */
@Component
public class KorisnikRacunRukovalac extends ZajednickiRukovalacZaWebSocket {
    private static LinkedHashMap<String, WebSocketSession> mapaSesijaZaSvakogKorisnika = new LinkedHashMap<>();
    private static LinkedHashMap< WebSocketSession, String> mapaObrnutaZaLogout= new LinkedHashMap<>();

    public LinkedHashMap<String, WebSocketSession> getMapaSesijaZaSvakogKorisnika(){
        return KorisnikRacunRukovalac.mapaSesijaZaSvakogKorisnika;
    }
    public LinkedHashMap< WebSocketSession, String> getMapaObrnutaZaLogout(){
        return KorisnikRacunRukovalac.mapaObrnutaZaLogout;
    }

    @Override
    public Object vratiteObjekatZaJson() {
        jsonStanje.setStanje((double)objekatZaSlanje);
        return jsonStanje;
    }

    private JsonStanje jsonStanje= new JsonStanje();
    private class JsonStanje {
        private double stanje;

        public double getStanje() {
            return stanje;
        }

        public void setStanje(double stanje) {
            this.stanje = stanje;
        }
    }


}
