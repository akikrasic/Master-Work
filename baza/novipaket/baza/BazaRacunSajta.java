package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.domen.Racun;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.stripe.RadSaPlacanjem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaRacunSajta extends BazaOsnovna {


    public void snimiteNaRacunSajta(double stanje){
        b.update("UPDATE racun SET racunsajta=?", stanje);
    }

    public double vratiteStanjeRacunaSajta(){
        List<Map<String, Object>> l = b.queryForList("SELECT racunsajta FROM racun");
        Map<String, Object> m = l.get(0);
        double vrednost = (Double)m.get("racunsajta");
        return vrednost;
    }
    public void snimiteNaRacunZaPrenos(double stanje){
        b.update("UPDATE racun SET racunzaprenos=?", stanje);
    }
    public double vratiteStanjeRacunaZaPrenos(){
        List<Map<String, Object>> l = b.queryForList("SELECT racunzaprenos FROM racun");
        Map<String, Object> m = l.get(0);
        double vrednost = (Double)m.get("racunzaprenos");
        return vrednost;
    }



    public HashMap<String, Double> vratiteMapuKorisnikaIStanjeRacuna(){
        HashMap<String, Double> mapa = new LinkedHashMap<>();
        List<Map<String, Object>> l = b.queryForList("SELECT email, racun FROM korisnik");
        l.forEach(m->{
            String email = (String)m.get("email");
            Double racun = (Double) m.get("racun");
            mapa.put(email, racun);
        });

        return mapa;
    }


    public void snimiteStanjeNaRacunKorisnika(String email, double stanje){
        b.update("UPDATE korisnik SET racun=? WHERE email=?", stanje, email);
    }
    public double vratiteStanjeRacunaKorisnika(String email){
        List<Map<String, Object>> l = b.queryForList("SELECT racun FROM korisnik WHERE email=?", email);
        Map<String, Object> m = l.get(0);
        double vrednost = (Double)m.get("racun");
        return vrednost;
    }

}
