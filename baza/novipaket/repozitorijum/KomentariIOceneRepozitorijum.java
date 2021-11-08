package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaKomentarOcena;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.KomentarIOcena;
import srb.akikrasic.domen.Proizvod;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by aki on 11/30/17.
 */
@Component
public class KomentariIOceneRepozitorijum extends RepozitorijumOsnovna<Integer, KomentarIOcena> {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;


    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;


    @Autowired
    public void setBaza(BazaKomentarOcena b){
        super.setBazaOsnovna(b);
        this.b=b;
        this.query="SELECT * FROM komentar_i_ocena ORDER BY id DESC";
    }
    private BazaKomentarOcena b;



    public void inicijalizacija(){
       //namerno se tu nista ne desava da bi migla inicijallizacija da se izvrsi iz ProizvodiRepozitorijum, kad njemu odgovara
        //posto ne poziva super.inicijalizacija onda se lepo izvrsava metoda koja je prazna i ne dogadja se nista

        super.inicijalizacija();
        mapa.values().stream().forEach(ko->{
            dodajteUMapuPretrage(ko);
            dodajteUMapuZaProizvod(ko);
        });
    }

    private HashMap<Integer, Map<String, KomentarIOcena>> mapaZaPretragu= new LinkedHashMap<>();
    private HashMap<Integer, List<KomentarIOcena>> mapaKomentariIOceneZaProizvod = new LinkedHashMap<>();

    private Map<String, KomentarIOcena> prvaPretragaUMapiPretrage(KomentarIOcena ko){
        int proizvodId= ko.getProizvod().getId();
        return mapaZaPretragu.get(proizvodId);
    }
    private Map<String, KomentarIOcena> prvaPretragaUMapiPretrage(int proizvodId){

        return mapaZaPretragu.get(proizvodId);
    }
    private KomentarIOcena pretragaUMapiPretrage(int proizvodId,String email){
        Map<String, KomentarIOcena> rezPrvePretrage = prvaPretragaUMapiPretrage(proizvodId);
        if(rezPrvePretrage ==null){
            return null;
        }
        return rezPrvePretrage.get(email);
    }

    private void dodajteUMapuZaProizvod(KomentarIOcena ko){
        int proizvodID= ko.getProizvod().getId();
        if(mapaKomentariIOceneZaProizvod.get(proizvodID)==null){
            mapaKomentariIOceneZaProizvod.put(proizvodID, new ArrayList<>());
        }
        mapaKomentariIOceneZaProizvod.get(proizvodID).add(0,ko);//da ga doda na pocetak da bude sortirano naopacke od zadnji
    }

    private void dodajteUMapuPretrage(KomentarIOcena ko){
        Map<String, KomentarIOcena> mapaPrviRezultat=  this.prvaPretragaUMapiPretrage(ko);//valjda je teka
        if(mapaPrviRezultat==null){
            mapaPrviRezultat= new LinkedHashMap<>();
            mapaZaPretragu.put(ko.getProizvod().getId(), mapaPrviRezultat);
        }
        mapaPrviRezultat.put(ko.getKorisnik().getEmail(), ko);
    }

    private void obrisiteIzMapePretrage(KomentarIOcena ko){
        Map<String, KomentarIOcena> mapaPrviRezultat=  this.prvaPretragaUMapiPretrage(ko);
        if(mapaPrviRezultat==null){
            return;//ako nesto negde iscuka...Ali ne bi trebalo nikako to da se desi, ali ajde neka bude teka osigurano
        }
        mapaPrviRezultat.remove(ko.getKorisnik().getEmail());
    }
    private void obrisiteIzMapeZaProizvod(KomentarIOcena ko){
        int proizvodId = ko.getProizvod().getId();
        List<KomentarIOcena> lista = mapaKomentariIOceneZaProizvod.get(proizvodId);
        if(lista==null){
            return;//ako nesto negde iscuka...Ali ne bi trebalo nikako to da se desi, ali ajde neka bude teka osigurano
        }
        lista.remove(ko);//O(n) ali sta sad da se radi...
    }
    @Override
    protected void dodajteUMapu(KomentarIOcena ko){
        super.dodajteUMapu(ko);
        this.dodajteUMapuPretrage(ko);
        this.dodajteUMapuZaProizvod(ko);
    }
    @Override
    protected void obrisiteIzMapu(KomentarIOcena ko){
        super.obrisiteIzMapu(ko);
        this.obrisiteIzMapePretrage(ko);
        this.obrisiteIzMapeZaProizvod(ko);
    }

    private void dodajteReferenceUKomentariIOcenaObjekat(KomentarIOcena ko, String korisnikEmail, int proizvodId){
        ko.setProizvod(proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId));
        ko.setKorisnik(korisniciRepozitorijum.pretragaKorisnikaPoEmailu(korisnikEmail));
    }

    public void napraviteIDodajteNoviObjekatKomentar(int id, String tekst, String korisnikEmail, int proizvodId){
        KomentarIOcena ko = new KomentarIOcena();
        ko.setTekst(tekst);
        ko.setDatum(LocalDateTime.now());//nije znacajno nije isti kao u bazi, ali se vrlo malo razlikuje, to je neprimetno...
        ko.setId(id);
        this.dodajteReferenceUKomentariIOcenaObjekat(ko, korisnikEmail, proizvodId);
        this.dodajteUMapu(ko);
    }
    public void napraviteIDodajteNoviObjekatOcena(int id, int ocena, String korisnikEmail, int proizvodId){
        KomentarIOcena ko = new KomentarIOcena();
        ko.setId(id);
        ko.setOcena(ocena);
        this.dodajteReferenceUKomentariIOcenaObjekat(ko, korisnikEmail, proizvodId);
        this.dodajteUMapu(ko);
    }
    public void izracunajteProsecnuOcenu(KomentarIOcena ko){
        Proizvod p = ko.getProizvod();
        double prosecnaOcena = b.vratiteProsecnuOcenuZaProizvod(p.getId());
        p.setProsecnaOcena(prosecnaOcena);
        proizvodiRepozitorijum.vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(p.getId()).setProsecnaOcena(prosecnaOcena);
    }
    //ovo je bilo dosta dosta lakse, nazalost
    public boolean  snimiteKomentarNovaMetoda(String tekst, String korisnikEmail, int proizvodId){
        KomentarIOcena komentarIOcena = this.pretragaUMapiPretrage(proizvodId, korisnikEmail);
        if(komentarIOcena==null){
            //znaci da nije pronadjen u mapi i treba da se doda
            int id = b.snimiteKomentarNovaMetodaPrviPut(tekst, korisnikEmail, proizvodId);
            this.napraviteIDodajteNoviObjekatKomentar(id, tekst, korisnikEmail, proizvodId);
            return true;
        }
        else{
            //postoji u bazi i sad se u postojeci samo menja tj dodaje se
            if( b.snimiteKomentarNovaMetodaAkoPostoji(tekst,komentarIOcena.getId())){
                komentarIOcena.setTekst(tekst);
                komentarIOcena.setDatum(LocalDateTime.now());//neka minimalna razlika postoji, nije znacajna
                return true;
            }
            else{
                return false;
            }
        }

    }
    public boolean snimiteOcenuNovaMetoda(int ocena, String korisnikEmail, int proizvodId){
        KomentarIOcena komentarIOcena = this.pretragaUMapiPretrage(proizvodId, korisnikEmail);
        if(komentarIOcena==null){
            //znaci da nije pronadjen u mapi i treba da se doda
            int id = b.snimiteOcenuNovaMetodaPrviPut(ocena, korisnikEmail, proizvodId);
            this.napraviteIDodajteNoviObjekatOcena(id, ocena, korisnikEmail,proizvodId);
            izracunajteProsecnuOcenu(komentarIOcena);
            return true;
        }
        else{
            //postoji u bazi i sad se u postojeci samo menja tj dodaje se
            if( b.snimiteOcenuNovaMetodaAkoPostoji(ocena, komentarIOcena.getId())){
                komentarIOcena.setOcena(ocena);
                izracunajteProsecnuOcenu(komentarIOcena);
                return true;
            }
            else{
                return false;
            }
        }

    }
    public KomentarIOcena vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(String korisnikEmail, int proizvodId ){
        return this.pretragaUMapiPretrage(proizvodId, korisnikEmail);
    }

    List<KomentarIOcena> vratiteSveKomentareIOceneZaProizvodNovi(int proizvodId){
        return this.mapaKomentariIOceneZaProizvod.get(proizvodId);
    }



    private KomentarIOcena napraviteObjekatKomentarIOcena(Map<String, Object> m){
        KomentarIOcena ko = KomentarIOcena.izBaze(m);
        ko.setProizvod(proizvodiRepozitorijum.vratiteProizvodPoId((Integer)m.get("proizvod_id")));
        ko.setKorisnik(korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        return ko;
    }

    @Override
    protected KomentarIOcena napraviteReferencu(Map<String, Object> m) {

        return this.napraviteObjekatKomentarIOcena(m) ;
    }
    public boolean daLiKorisnikSmeDaKomentariseProizvod(String email, int proizvodId){
       return b.daLiKorisnikSmeDaKomentariseProizvod(email, proizvodId);
    }

}
