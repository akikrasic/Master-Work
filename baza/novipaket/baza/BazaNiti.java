package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.NaruceniProizvodBezNarudzbenice;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.stripe.RadSaPlacanjem;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaNiti extends BazaOsnovna {

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private RadSaPlacanjem radSaPlacanjem;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    public void proveriteDaLiSuTokeniZaPromenuSifreValidni(int brojMinuta){
        long period = 60*1000*brojMinuta;
        long milisekunde = System.nanoTime();
        List<Map<String,Object>> rez = b.queryForList("SELECT email, vreme FROM promenasifre");
        rez.forEach(m->{
            String email = (String)m.get("email");
            Long vreme = (Long)m.get("vreme");
            if(milisekunde-vreme>period){  //to treba da se izmeni
                b.update("DELETE FROM promenasifre WHERE email=?", email);
            }

        });
    }
    public void proveriteDaLiJeProdavacOdgovorioNaZalbu(int brojDana){
        long brojMiliSekundi = brojDana*24*60*60*1000;
        this.izvrsiteQuery("SELECT id, datum_kupca FROM zalba WHERE tekst_prodavca IS NULL  ").forEach(m->{
            // korisnik otkazuje plus se skidaju pare od racun
            int id = (Integer)m.get("id");
            long datumKupca = ((Timestamp)m.get("datum_kupca")).getTime();
            if(System.nanoTime()-datumKupca>brojMiliSekundi){

                List<Map<String, Object>> l = b.queryForList("SELECT id FROM naruceniproizvod WHERE zalba_id=?", id);
                int npId = (Integer)l.get(0).get("id");
                NaruceniProizvod np = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(npId);
                naruceniProizvodiRepozitorijum.otkazanNaruceniProizvod(np);
                if(radSaPlacanjem.ponistitePlacanje(np.getNarudzbenica().getChargeId(),np.getCenaPutaKolicina())){
                    this.upravljanjeRacunom.skiniteSaRacunaZaPrenos(np.getCenaPutaKolicina());
                }
            }
        });
    }
    public void proveriteDaLiJeKupacOdgovorioNaPoslatProizvod(int brojDana){
        long brojMiliSekundi = brojDana*24*60*60*1000;
        this.izvrsiteQuery("SELECT id, prodavacpotvrdio_datum from naruceniproizvod WHERE zalba_id is null AND prodavacpotvrdio=? AND kupacpotvrdio=?", true, false).forEach(m->{
            //ja sam ga bio napravio da potvrdi svaki na koji naidje
            int id = (Integer)m.get("id");
            long prodavacPotvrdioDatum = ((Timestamp)m.get("prodavacpotvrdio_datum")).getTime();

            if( System.nanoTime()-prodavacPotvrdioDatum>brojMiliSekundi) {
                NaruceniProizvod np = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(id);
                naruceniProizvodiRepozitorijum.kupacPotvrdjujeNaruceniProizvod(np);
                this.upravljanjeRacunom.prebaciteNaRacunKorisnika(np.getNarudzbenica().getKupac().getEmail(), np.getCenaPutaKolicina());
            }
        });
    }

}
