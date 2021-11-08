package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaReferenca;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;

import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/25/17.
 */
@Component
public class BazaLogovanje extends BazaOsnovna {



    @Autowired
    private Sifra radSaSifrom;
    @Autowired
    private Tokeni radSaTokenima;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;


    private String neuspesnoLogovanjePoruka = "Унели сте погрешну адресу електронске поште или шифру.";

    public RezultatLogovanja logovanjeKorisnika(String email, byte[] sifra) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT imeNaziv, email, adresa, mesto, tel1, sifra FROM korisnik WHERE email=?", email);
        RezultatLogovanja l = new RezultatLogovanja();
        if (rezultat.isEmpty()) {
            l.setUspesno(false);
            List<Map<String, Object>> rezultatDrugePretrage = b.queryForList("SELECT email, sifra from nepotvrdjenikorisnik WHERE email=?", email);
            if (rezultatDrugePretrage.isEmpty()) {
                l.setRazlogNeuspesnogLogovanja(neuspesnoLogovanjePoruka);
            } else {
                byte[] sifraIzBaze = (byte[]) rezultatDrugePretrage.get(0).get("sifra");
               /* System.out.println(sifra);
                System.out.println(sifraIzBaze);*/
                if (radSaSifrom.proveriteSifru(sifra, sifraIzBaze)) {
                    l.setRazlogNeuspesnogLogovanja("Адреса електронске поште коју сте унели није потврђена, молимо Вас да је потврдите. ");
                } else {
                    l.setRazlogNeuspesnogLogovanja(neuspesnoLogovanjePoruka);
                }
            }
        } else {
            byte[] sifraIzBaze = (byte[]) rezultat.get(0).get("sifra");
            if (radSaSifrom.proveriteSifru(sifra, sifraIzBaze)) {
                l.setUspesno(true);
                l.setEmail(email);
                Map<String, Object> redIzMape = rezultat.get(0);

                l.setToken(radSaTokenima.korisnikUspesnoUlogovan((String) redIzMape.get("imeNaziv"), (String) redIzMape.get("email"), (String) redIzMape.get("adresa"), (String) redIzMape.get("mesto"), (String) redIzMape.get("tel1")));
                l.setStanjeRacuna(upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email));
                //l.setToken(radSaTokenima.generisiteTokenZaNeregistrovanogKorisnika());
            } else {
                l.setUspesno(false);
                l.setRazlogNeuspesnogLogovanja(neuspesnoLogovanjePoruka);
            }

        }
        return l;
    }

    public String getNeuspesnoLogovanjePoruka() {
        return neuspesnoLogovanjePoruka;
    }

    public void setNeuspesnoLogovanjePoruka(String neuspesnoLogovanjePoruka) {
        this.neuspesnoLogovanjePoruka = neuspesnoLogovanjePoruka;
    }

    public BazaReferenca getB() {
        return b;
    }

    public void setB(BazaReferenca b) {
        this.b = b;
    }

    public Sifra getRadSaSifrom() {
        return radSaSifrom;
    }

    public void setRadSaSifrom(Sifra radSaSifrom) {
        this.radSaSifrom = radSaSifrom;
    }

    public Tokeni getRadSaTokenima() {
        return radSaTokenima;
    }

    public void setRadSaTokenima(Tokeni radSaTokenima) {
        this.radSaTokenima = radSaTokenima;
    }
}
