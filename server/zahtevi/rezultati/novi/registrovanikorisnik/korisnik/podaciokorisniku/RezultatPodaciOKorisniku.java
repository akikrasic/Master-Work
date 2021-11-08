package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.podaciokorisniku;

import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.KorisnikOcena;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 1/15/18.
 */
public class RezultatPodaciOKorisniku extends RezultatRegistrovaniKorisnik{

    private Korisnik korisnik;
    private String ekstenzija;
    private List<Proizvod> proizvodi;
    private int ukBrojProizvoda;
    private List<KorisnikOcena> oceneKorisnika;
    private boolean daLiMozeKupacDaOcenjujeProdavca;

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public String getEkstenzija() {
        return ekstenzija;
    }

    public void setEkstenzija(String ekstenzija) {
        this.ekstenzija = ekstenzija;
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }

    public List<KorisnikOcena> getOceneKorisnika() {
        return oceneKorisnika;
    }

    public void setOceneKorisnika(List<KorisnikOcena> oceneKorisnika) {
        this.oceneKorisnika = oceneKorisnika;
    }

    public boolean isDaLiMozeKupacDaOcenjujeProdavca() {
        return daLiMozeKupacDaOcenjujeProdavca;
    }

    public void setDaLiMozeKupacDaOcenjujeProdavca(boolean daLiMozeKupacDaOcenjujeProdavca) {
        this.daLiMozeKupacDaOcenjujeProdavca = daLiMozeKupacDaOcenjujeProdavca;
    }

    public int getUkBrojProizvoda() {
        return ukBrojProizvoda;
    }

    public void setUkBrojProizvoda(int ukBrojProizvoda) {
        this.ukBrojProizvoda = ukBrojProizvoda;
    }
}
