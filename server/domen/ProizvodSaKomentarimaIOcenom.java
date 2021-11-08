package srb.akikrasic.domen;

import java.util.Map;

/**
 * Created by aki on 12/1/17.
 */
public class ProizvodSaKomentarimaIOcenom extends Proizvod {
    public static ProizvodSaKomentarimaIOcenom izBaze(Map<String, Object> m){
        ProizvodSaKomentarimaIOcenom p = new ProizvodSaKomentarimaIOcenom();
        p.setId((Integer)m.get("id"));
        p.setNaziv((String)m.get("naziv"));
        p.setOpis((String)m.get("opis"));
        //stalno je bacalo gresku da je obj tipa float pa greska da treba da bude double, a kad se promeni onda je isla obrnuta greska
        //zato kao string pa od njega double
        String trenutnaCena=  m.get("trenutnacena").toString();
        p.setTrenutnaCena(Double.parseDouble(trenutnaCena));
        p.setAktivan((Boolean)m.get("aktivan"));
        p.setProsecnaOcena((Double)m.get("prosecna_ocena"));
        return p;
    }

    public static ProizvodSaKomentarimaIOcenom izProizvoda(Proizvod p ){
        ProizvodSaKomentarimaIOcenom pko = new ProizvodSaKomentarimaIOcenom();
        pko.setId(p.getId());
        pko.setNaziv(p.getNaziv());
        pko.setOpis(p.getOpis());

        pko.setKorisnik(p.getKorisnik());
        pko.setKomentariIOcene(p.getKomentariIOcene());
        pko.setKategorija(p.getKategorija());
        pko.setTrenutnaCena(p.getTrenutnaCena());
        pko.setAktivan(p.isAktivan());
        pko.setProsecnaOcena(p.getProsecnaOcena());
        pko.setSlike(p.getSlike());
        return pko;

    }
}
