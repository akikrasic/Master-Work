package srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik;

import org.springframework.stereotype.Component;
import srb.akikrasic.domen.*;
import srb.akikrasic.zahtevi.rezultati.stari.svikorisnici.Rezultat;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.komentariocena.dalismedakomentariseiocenjuje.RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.stanjeracuna.RezultatStanjeRacunaNaPocetkuRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavca.RezultatNaruceniProizvodiProdavca;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.narudzbenica.vracanjenarudzbenicekupcu.RezultatNarudzbeniceKupca;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.poruke.vratiterazgovor.RezultatPoruke;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.poruke.vratitesagovornike.RezultataKorisniciSaKojimaImaPravaDaPrica;

import java.util.HashMap;
import java.util.List;

/**
 * Created by aki on 10/13/17.
 */
@Component
public class RezultatRegistrovani extends Rezultat {
    public RezultatRegistrovaniKorisnik rezultatNaruceniProizvodiProdavca(List<NaruceniProizvod> naruceniProizvodi){
        RezultatNaruceniProizvodiProdavca r = new RezultatNaruceniProizvodiProdavca();
        r.setNaruceniProizvodi(naruceniProizvodi);
        r.setDuzina(naruceniProizvodi.size());
        r.setDaLiJeURedu(true);
        return r;
    }
    public RezultatRegistrovaniKorisnik rezultatNaruceniProizvodiKupca(List<Narudzbenica> l){
        RezultatNarudzbeniceKupca r = new RezultatNarudzbeniceKupca();
        r.setDaLiJeURedu(true);
        r.setNarudzbenice(l);
        r.setDuzina(l.size());
        return r;
    }
    public RezultatRegistrovaniKorisnik rezultatOtkazivanjeNarucenogProizvoda(boolean rezultat){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(rezultat);
        return r;
    }

    public RezultatRegistrovaniKorisnik rezultatRezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod(boolean rezultat, Komentar komentar, Ocena ocena){
        RezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod r = new RezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod();
        r.setDaLiJeURedu(true);
        r.setDaLiImaPrava(rezultat);
        r.setKomentar(komentar);
        r.setOcena(ocena);
        return r;
    }
    public RezultatRegistrovaniKorisnik rezultatNoviDaLiSmeDaOcenjujeIKomentarise(boolean rezultat, KomentarIOcena komentarIOcena){
        RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise r = new RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise();
        r.setDaLiJeURedu(true);
        r.setDaLiImaPrava(rezultat);
        r.setKomentarIOcena(komentarIOcena);
        return r;
    }
    public RezultatRegistrovaniKorisnik rezultatKorisniciSaKojimaSmeDaPrica(List<Korisnik> korisnici, HashMap<String, List<Poruka>> poruke){
        RezultataKorisniciSaKojimaImaPravaDaPrica r = new RezultataKorisniciSaKojimaImaPravaDaPrica();
        r.setDaLiJeURedu(true);
        r.setKorisnici(korisnici);
        r.setPoruke(poruke);
        return r;
    }
    public RezultatPoruke rezultatPoruke(List<Poruka> poruke){
        RezultatPoruke r = new RezultatPoruke();
        r.setDaLiJeURedu(true);
        r.setPoruke(poruke);
        return r;
    }
    public RezultatRegistrovaniKorisnik pocetnoStanjeRacunaKorisnika(double stanje){
        RezultatStanjeRacunaNaPocetkuRegistrovaniKorisnik r = new RezultatStanjeRacunaNaPocetkuRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        r.setStanje(stanje);
        return r;
    }

    public RezultatRegistrovaniKorisnik sveJeURedu(){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        return r;
    }
}
