package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.poruke.vratitesagovornike;

import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.Poruka;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.HashMap;
import java.util.List;

/**
 * Created by aki on 11/8/17.
 */
public class RezultataKorisniciSaKojimaImaPravaDaPrica extends RezultatRegistrovaniKorisnik {
    private List<Korisnik> korisnici;
    private HashMap<String, List<Poruka>> poruke;
    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public HashMap<String, List<Poruka>> getPoruke() {
        return poruke;
    }

    public void setPoruke(HashMap<String, List<Poruka>> poruke) {
        this.poruke = poruke;
    }
}
