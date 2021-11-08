package srb.akikrasic.token;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.websocket.klase.AdminRacunRukovalac;
import srb.akikrasic.websocket.klase.KorisnikRacunRukovalac;
import srb.akikrasic.websocket.klase.ObavestenjaRukovalac;
import srb.akikrasic.websocket.klase.RazgovorRukovalac;

/**
 * Created by aki on 7/23/17.
 */
@RestController
public class Tokeni {
    private HashMap<String, String> mapaUlogovanihKorisnika;
    private String adminToken;
    private final String POCETNA_VREDNOST="";

    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    @Autowired
    private ObavestenjaRukovalac porudzbina;
    @Autowired
    private AdminRacunRukovalac adminRacun;

    @Autowired
    private KorisnikRacunRukovalac korisnikRacun;
    @Autowired
    private RazgovorRukovalac razgovor;

    private void postaviteAdminTokenNaPocetak(){
        adminToken = POCETNA_VREDNOST;
    }

    public Tokeni(){
        postaviteAdminTokenNaPocetak();
        mapaUlogovanihKorisnika = new HashMap<>();

    }

    public boolean daLiJeUlogovanKorisnik(String emailKorisnika, String token){
        String rezultatPretrage= mapaUlogovanihKorisnika.get(emailKorisnika);
        if(rezultatPretrage==null)return false;
        return rezultatPretrage.equals(token);
    }
    public boolean daLiJeUlogovanAdmin(String token){
        if(token.length()==0)return false;
        return adminToken.equals(token);
    }


    public String adminUspesnoUlogovan(){

        adminToken=radSaGenerisanjemTokena.generisiteNoviAdminToken();
        return adminToken;

    }

    public String korisnikUspesnoUlogovan( String imeNaziv,String email,String adresa,String mesto,  String tel1){
        String token = this.radSaGenerisanjemTokena.generisiteNoviKorisnikToken(imeNaziv, email, adresa,mesto, tel1);
        mapaUlogovanihKorisnika.put(email, token);
        return token;
    }


    public boolean adminOdjava(String tokenZaPoredjenje){
        boolean rezultat = tokenZaPoredjenje.equals(adminToken);
        if(rezultat) {
            postaviteAdminTokenNaPocetak();
            adminRacun.odjavaKorisnika();
        }
        return rezultat;
    }

    private void odjavaWebSocketa(String email){
        porudzbina.odjavaKorisnika(email);
        korisnikRacun.odjavaKorisnika(email);
        razgovor.odjavaKorisnika(email);
    }

    public boolean korisnikOdjava(String emailKorisnika, String tokenZaPoredjenje){
        String token =  mapaUlogovanihKorisnika.get(emailKorisnika);
        boolean rezultat = token.equals(tokenZaPoredjenje);
        if(rezultat) {
            mapaUlogovanihKorisnika.remove(emailKorisnika);
            odjavaWebSocketa(emailKorisnika);
        }
        return rezultat;
    }

    public GenerisanjeTokena getRadSaGenerisanjemTokena() {
        return radSaGenerisanjemTokena;
    }

    public void setRadSaGenerisanjemTokena(GenerisanjeTokena radSaGenerisanjemTokena) {
        this.radSaGenerisanjemTokena = radSaGenerisanjemTokena;
    }
}
