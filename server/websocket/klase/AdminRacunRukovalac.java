package srb.akikrasic.websocket.klase;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import srb.akikrasic.domen.Racun;
import srb.akikrasic.websocket.ZajednickiRukovalacZaWebSocket;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by aki on 11/6/17.
 */
@Component
public class AdminRacunRukovalac extends ZajednickiRukovalacZaWebSocket {
    private static LinkedHashMap<String, WebSocketSession> mapaSesijaZaSvakogKorisnika = new LinkedHashMap<>();
    private static LinkedHashMap< WebSocketSession, String> mapaObrnutaZaLogout= new LinkedHashMap<>();

    public LinkedHashMap<String, WebSocketSession> getMapaSesijaZaSvakogKorisnika(){
        return AdminRacunRukovalac.mapaSesijaZaSvakogKorisnika;
    }
    public LinkedHashMap< WebSocketSession, String> getMapaObrnutaZaLogout(){
        return AdminRacunRukovalac.mapaObrnutaZaLogout;
    }

    @Override
    public Object vratiteObjekatZaJson() {
        jsonRacun.setRacun((Racun)objekatZaSlanje);
        return jsonRacun;
    }

    private JsonRacun jsonRacun = new JsonRacun();

    private class JsonRacun{
        private Racun racun;

        public Racun getRacun() {
            return racun;
        }

        public void setRacun(Racun racun) {
            this.racun = racun;
        }
    }
    @Override
    public void obraditePoruku(HashMap<String, Object> mapa, WebSocketSession sesija) {
        Object tokenO = mapa.get("token");
        if(tokenO==null)return;
        String token = genTokena.dekodujteURLFormat(tokenO.toString());
        if(this.tokeni.daLiJeUlogovanAdmin(token)){
            getMapaSesijaZaSvakogKorisnika().put("admin", sesija);
            getMapaObrnutaZaLogout().put(sesija, "admin");
        }
    }

    public void posaljite(Object racun){
        this.posaljite("admin", racun);
    }
    public void odjavaKorisnika(){
        this.odjavaKorisnika("admin");
    }
}
