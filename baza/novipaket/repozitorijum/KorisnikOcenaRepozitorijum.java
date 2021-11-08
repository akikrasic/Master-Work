package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaBlog;
import srb.akikrasic.baza.novipaket.baza.BazaKorisnici;
import srb.akikrasic.baza.novipaket.baza.BazaKorisnikOcena;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.KorisnikOcena;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 2/23/18.
 */
@Component
public class KorisnikOcenaRepozitorijum extends RepozitorijumOsnovna<Integer, KorisnikOcena> {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    public void setBaza(BazaKorisnikOcena b){

        super.setBazaOsnovna(b);
        this.b =b;
        query ="SELECT * FROM korisnikocena ORDER BY datum desc";
    }
    private BazaKorisnikOcena b;

    private Map<String, List<KorisnikOcena>> mapaZaKorisnika = new LinkedHashMap<>();

    private void dodajteUMapuZaKorisnika(KorisnikOcena ko){
        List<KorisnikOcena> l = mapaZaKorisnika.get(ko.getProdavac().getEmail());
        if(l==null){
            l = new ArrayList();
            mapaZaKorisnika.put(ko.getProdavac().getEmail(), l);
        }
        l.add(ko);
    }
    private void obrisiteIzMapuZaKorisnika(KorisnikOcena ko){
        List<KorisnikOcena> l = mapaZaKorisnika.get(ko.getProdavac().getEmail());
        l.remove(ko);
    }

    @Override
    public void inicijalizacija(){
        super.inicijalizacija();
        this.mapa.values().forEach(ko->{
            this.dodajteUMapuZaKorisnika(ko);
        });
    }


    @Override
    protected void dodajteUMapu(KorisnikOcena objekatZaDodavanje){
        super.dodajteUMapu(objekatZaDodavanje);
        this.dodajteUMapuZaKorisnika(objekatZaDodavanje);
    }

    @Override
    protected void obrisiteIzMapu(KorisnikOcena objekatZaBrisanje){
        super.obrisiteIzMapu(objekatZaBrisanje);
        this.obrisiteIzMapuZaKorisnika(objekatZaBrisanje);
    }



    public boolean daLiKupacMozeDaOcenjujeProdavca(String kupac, String prodavac){
        if(kupac.equals(prodavac)){
            return false;
        }
        return b.daLiKupacMozeDaOcenjujeProdavca(kupac, prodavac);
    }
    public int dodajteOcenuKorisnika(int ocena, String kupacEmail, String prodavacEmail){
        LocalDateTime datum = LocalDateTime.now();
        int id=b.dodajteOcenuKorisnika(ocena, datum, kupacEmail, prodavacEmail);
        KorisnikOcena ko = new KorisnikOcena();
        ko.setId(id);
        ko.setDatum(datum);
        ko.setKupac(korisniciRepozitorijum.pretragaKorisnikaPoEmailu(kupacEmail));
        ko.setProdavac(korisniciRepozitorijum.pretragaKorisnikaPoEmailu(prodavacEmail));
        ko.setOcena(ocena);

        this.dodajteUMapu(ko);


        return id;

    }

    public boolean izmeniteOcenuKorisnika(int id, int ocena){
        LocalDateTime datum = LocalDateTime.now();
        boolean rez = b.izmeniteOcenuKorisnika(id, datum,  ocena);
        if(rez){
            KorisnikOcena ko = mapa.get(id);
            ko.setDatum(datum);
            ko.setOcena(ocena);
        }
        return rez;
    }

    public boolean obrisiteOcenuKorisnika(int id){
       boolean rez = b.obrisiteOcenuKorisnika(id);
        if(rez){
            this.obrisiteIzMapu(mapa.get(id));
        }
        return rez;
    }

    public KorisnikOcena vratiteKorisnikuOcenu(int id){
        return mapa.get(id);
    }

    public List<KorisnikOcena> vratiteOceneZaKorisnika(String korisnikEmail){
        return this.mapaZaKorisnika.get(korisnikEmail);
    }


    @Override
    protected KorisnikOcena napraviteReferencu(Map<String, Object> m) {
        KorisnikOcena k =  KorisnikOcena.izBaze(m);
        k.setKupac(korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String)m.get("kupac_email")));
        k.setProdavac(korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String)m.get("prodavac_email")));
        return k;
    }
}
