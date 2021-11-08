package srb.akikrasic.websocket.klase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.websocket.ZajednickiRukovalacZaWebSocket;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by aki on 11/6/17.
 */
@Component
public class RazgovorRukovalac extends ZajednickiRukovalacZaWebSocket {
    @Autowired
    private Baza b;

    private ZajednickaOperacija login = new Login();
    private ZajednickaOperacija prijemPoruke = new PrijemPoruke();


    private static LinkedHashMap<String, WebSocketSession> mapaSesijaZaSvakogKorisnika = new LinkedHashMap<>();
    private static LinkedHashMap< WebSocketSession, String> mapaObrnutaZaLogout= new LinkedHashMap<>();
    private static LinkedHashMap<WebSocketSession, ZajednickaOperacija> mapaOperacijaZaSvakuSesiju = new LinkedHashMap<>();

    public LinkedHashMap<WebSocketSession, ZajednickaOperacija>getMapaOperacijaZaSvakuSesiju(){
        return RazgovorRukovalac.mapaOperacijaZaSvakuSesiju;
    }

    public LinkedHashMap<String, WebSocketSession> getMapaSesijaZaSvakogKorisnika(){
        return RazgovorRukovalac.mapaSesijaZaSvakogKorisnika;
    }
    public LinkedHashMap< WebSocketSession, String> getMapaObrnutaZaLogout(){
        return RazgovorRukovalac.mapaObrnutaZaLogout;
    }

    @Override
    public Object vratiteObjekatZaJson() {
       // slanjePoruke.setOd();
       // slanjePoruke.setTekst();

        return slanjePoruke ;//slanje poruke
    }


    private JsonSlanjePoruke slanjePoruke = new JsonSlanjePoruke();
    private class JsonSlanjePoruke{
        private String od;
        private String tekst;

        public String getOd() {
            return od;
        }

        public void setOd(String od) {
            this.od = od;
        }

        public String getTekst() {
            return tekst;
        }

        public void setTekst(String tekst) {
            this.tekst = tekst;
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        this.getMapaOperacijaZaSvakuSesiju().put(session, login);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        this.getMapaOperacijaZaSvakuSesiju().remove(session);

    }


    @Override
    public void obraditePoruku(HashMap<String, Object> mapa, WebSocketSession sesija){
        this.getMapaOperacijaZaSvakuSesiju().get(sesija).izvrsiteObraduPoruke(mapa, sesija);
    }


    private interface ZajednickaOperacija{
        public void izvrsiteObraduPoruke(HashMap<String, Object> mapa, WebSocketSession sesija);
    }
    private class Login implements ZajednickaOperacija{

        @Override
        public void izvrsiteObraduPoruke(HashMap<String, Object> mapa, WebSocketSession sesija) {
            String email = genTokena.dekodujteURLFormat((String)mapa.get("email"));
            String token = genTokena.dekodujteURLFormat((String)mapa.get("token"));
            if(tokeni.daLiJeUlogovanKorisnik(email, token)){
                getMapaSesijaZaSvakogKorisnika().put(email, sesija);
                getMapaObrnutaZaLogout().put(sesija, email);
            }
            getMapaOperacijaZaSvakuSesiju().put(sesija,prijemPoruke);
        }
    }
    private class PrijemPoruke implements ZajednickaOperacija{

        @Override
        public void izvrsiteObraduPoruke(HashMap<String, Object> mapa, WebSocketSession sesija) {
            //prijem poruke

            Object emailO= mapa.get("email");
            Object tokenO= mapa.get("token");
            Object zaO = mapa.get("za");
            Object tekstO = mapa.get("tekst");
            if(emailO==null||tokenO==null||zaO==null||tekstO==null)return;
            String email = genTokena.dekodujteURLFormat(emailO.toString());
            String token =  genTokena.dekodujteURLFormat(tokenO.toString());
            String za =  genTokena.dekodujteURLFormat(zaO.toString());
            String tekst =  genTokena.dekodujteURLFormat(tekstO.toString());
            if(RazgovorRukovalac.this.tokeni.daLiJeUlogovanKorisnik(email, token)){
                b.snimitePoruku(email,  za, tekst);
                slanjePoruke.setTekst( tekst);
                slanjePoruke.setOd(email);
                System.out.println(getMapaSesijaZaSvakogKorisnika());
                posaljiteTextMessage(za, napraviteTextMessage());
            }
        }
    }
}
