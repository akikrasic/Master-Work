package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaProizvod;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumSortiran;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.domen.ProizvodSaKomentarimaIOcenom;
import srb.akikrasic.slike.Slike;

import java.util.*;

/**
 * Created by aki on 11/30/17.
 */
@Component
public class ProizvodiRepozitorijum extends RepozitorijumSortiran<Integer, Proizvod> {

    @Autowired
    private Slike slike;
    @Autowired
    private KomentariIOceneRepozitorijum komentariIOceneRepozitorijum;

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;


    public Proizvod napraviteProizvodSaPodacimaIzBaze(Map<String, Object> m) {
        Proizvod p = Proizvod.izBaze(m);
        p.setKategorija(kategorijeRepozitorijum.vratiteKategorijuPoId((Integer) m.get("kategorija_id")));
        p.setKorisnik(korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String) m.get("korisnik_email")));
        p.setSlike(slike.vratiteSveSlikeProizvoda(p.getId()));
        return p;
    }



    @Autowired
    public void setBaza(BazaProizvod b){
        super.setBazaOsnovna(b);
        this.b = b;
        this.query = "SELECT * FROM proizvod ORDER BY naziv";
    }
    private BazaProizvod b;



    Map<Integer, Proizvod> mapaSaKomentarimaIOcenama = new LinkedHashMap<>();

    private void dodajteUMapuSaKomentarimaIOcenama(Proizvod p){
        ProizvodSaKomentarimaIOcenom pko = ProizvodSaKomentarimaIOcenom.izProizvoda(p);
        pko.setKomentariIOcene(komentariIOceneRepozitorijum.vratiteSveKomentareIOceneZaProizvodNovi(pko.getId()));
        mapaSaKomentarimaIOcenama.put(pko.getId(),pko );
    }
    private void obrisiteIzMapuSaKomentarimaIOcenama(Proizvod p){
        mapaSaKomentarimaIOcenama.remove(p.getIdentitet());

    }
    protected void dodajteUMapu(Proizvod p){
        super.dodajteUMapu(p);
        this.dodajteUMapuSaKomentarimaIOcenama(p);
    }
    protected void obrisiteIzMapu(Proizvod p){
        super.obrisiteIzMapu(p);
        this.obrisiteIzMapuSaKomentarimaIOcenama(p);
    }

    public void inicijalizacijaIzRepozitorijumaKomentaraIOcena(){
        this.mapa.values().forEach(p->{
            dodajteUMapuSaKomentarimaIOcenama(p);
        });
    }

    public void inicijalizacija(){
        super.inicijalizacija();
        komentariIOceneRepozitorijum.inicijalizacija();

        this.mapa.values().forEach(p->{
            dodajteUMapuSaKomentarimaIOcenama(p);
        });


    }


    public Proizvod vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenamaNovi(int proizvodId){
        return mapaSaKomentarimaIOcenama.get(proizvodId);
    }


    public Proizvod vratiteProizvodPoId(Integer id) {
        return mapa.get(id);
    }


    public int dodajteNoviProizvod(String email, String naziv, String opis, double cena, int kategorijaId) {
        int id = b.dodajteNoviProizvod(email, naziv, opis, cena, kategorijaId);
        Proizvod p = new Proizvod();
        p.setNaziv(naziv);
        p.setOpis( opis);
        p.setTrenutnaCena( cena);
        p.setKategorija(this.kategorijeRepozitorijum.vratiteKategorijuPoId(kategorijaId));
        p.setKorisnik(this.korisniciRepozitorijum.pretragaKorisnikaPoEmailu( email));
        p.setAktivan( true);
        p.setId(id);
        this.dodajteUMapu(p);


        //  b.update("INSERT INTO proizvod(naziv, opis, trenutnacena, prodavac_id, kategorija_id) VALUES (?,?,?,?,?)", p.getNaziv(), p.getOpis(), p.getTrenutnaCena(),p.getProdavac().getId(), p.getKategorija().getId())==1;

        return id;
    }
    public void izmeniteVrednosti(Proizvod p){
        Proizvod pko = mapaSaKomentarimaIOcenama.get(p.getIdentitet());

        pko.setNaziv(p.getNaziv());
        pko.setOpis(p.getOpis());
        pko.setTrenutnaCena(p.getTrenutnaCena());
        pko.setKategorija(p.getKategorija());
        pko.setAktivan(p.isAktivan());
    }
//tamo se izmene vrdnosti vec i onda se ovamo samo snimi, zato treba u onu drugu referencu da se menjaju vrednosti
    public boolean izmeniteProizvodPodaci(Proizvod p) {
        if(b.izmeniteProizvodPodaci(p)){
            izmeniteVrednosti(p);
            return true;
        }
        return false;
    }

    public boolean izmeniteProizvodCenu(Proizvod p) {
        if(b.izmeniteProizvodCenu(p)){
            Proizvod pko = mapaSaKomentarimaIOcenama.get(p.getIdentitet());
            pko.setTrenutnaCena(p.getTrenutnaCena());

            return true;
        }
        return false;
    }
    public boolean izmeniteProizvodAktivan(Proizvod p){
        if(b.izmeniteProizvodAktivan(p)){
            Proizvod pko = mapaSaKomentarimaIOcenama.get(p.getIdentitet());
            pko.setAktivan(p.isAktivan());
            return true;
        }
        return false;
    }

    public boolean obrisiteProizvod(Proizvod p) {

        //izbrisite iz kljucne reci
        //izbrisite iz proizvodi
        //samo polako mora prvo kljucne reci da sredim
        boolean rez = b.obrisiteProizvod(p);
        if(!rez){
            return false;
        }
        this.obrisiteIzMapu(p);
        return true;
    }

    public boolean obrisiteProizvod(int id) {
        boolean rez = b.obrisiteProizvod(id);
        if(!rez ){
            return false;
        }
        Proizvod p = mapa.get(id);
        this.obrisiteIzMapu(p);
        return true;
    }

    private  List<Proizvod> napraviteListuProizvodaOdListeId(List<Integer> listaId, Map<Integer,  Proizvod> m){
        List<Proizvod> l = new ArrayList<>();
        listaId.forEach(i->{
            l.add(m.get(i));
        });

        return l;
    }
    private List<Proizvod> napraviteListuProizvodaOdListeIdKomentariIOcene(List<Integer> listaId) {
        List<Proizvod> l = new ArrayList<>();
        listaId.forEach(i->{
            l.add(mapaSaKomentarimaIOcenama.get(i));
        });
        return l;
    }
    public List<Proizvod> vratiteSveProizvodeNekogKorisnikaPoEmailu(String email) {

        if(this.korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email)==null){
            return new ArrayList<>();
        }
        //moze ovo i da se skrati ali zbog citljivosti ipak ne zelim to da radim
        List<Integer> listaId =  b.vratiteSveProizvodeNekogKorisnikaPoEmailu(email);
        List<Proizvod> l = this.napraviteListuProizvodaOdListeId(listaId, sortiranaMapa);
        return l;
    }
    public List<Proizvod> vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(String email) {

        Korisnik k = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        if (k == null) return new ArrayList<>();
        //ovde se svi vracaju i aktivni i neaktivni
        List<Integer> listaId = b.vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(email);
        List<Proizvod> l = napraviteListuProizvodaOdListeId(listaId, mapa);
        return l;
    }


    public List<Proizvod> vratiteSveProizvode(){
        List<Proizvod> lista = this.napraviteListuProizvodaOdListeId(b.vratiteSveProizvode(), mapa);
        return lista;
    }

    public List<Proizvod> vratiteSveProizvodeKorisnika(String email){
        List<Proizvod> lista = this.napraviteListuProizvodaOdListeId( b.vratiteSveProizvodeKorisnika(email), mapa);

        return lista;
    }

    public List<Proizvod> pretragaProizvoda(String imeNazivKorisnika,
                                                       String email,
                                                       String nazivProizvoda,
                                                       double cenaOd,//samo ne sme da bude null sto se pre toga proverava
                                                       double cenaDo,//isto vazi
                                                       List<String> kljucneReci,
                                                       String kategorijaNaziv,
                                                       String opis
    ) {
        //tu jos malko treba da se refaktorise da se ovaj rezultat izbaci napolje
        //to cu posle

        List<Proizvod> lista =this.napraviteListuProizvodaOdListeId( b.pretragaProizvoda(
                imeNazivKorisnika,
                email,
                nazivProizvoda,
                cenaOd,
                cenaDo,
                kljucneReci,
                kategorijaNaziv,
                opis), mapa);//dajem za sad sortiranaMapa ali mozda bi trebalo samo mapa ce vidimo
      /*  List<Proizvod> lista =this.napraviteListuProizvodaOdListeIdKomentariIOcene( b.pretragaProizvoda(
                imeNazivKorisnika,
                email,
                nazivProizvoda,
                cenaOd,
                cenaDo,
                kljucneReci,
                kategorijaNaziv,
                opis));//ako slucajno zatrebaju komentari, a ne bi trebalo, sad me mrzi da trazim, onda
                        //lako ce ukljucim, ako ne odlicno
*/
        return lista;
    }





    public Proizvod vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(int proizvodId){
        return mapaSaKomentarimaIOcenama.get(proizvodId);
    }

    //od ova dva metoda jedan ce se brise
    public List<Proizvod> vratiteStoProizvoda() {
        List<Proizvod> proizvodi = this.napraviteListuProizvodaOdListeIdKomentariIOcene(b.vratiteStoProizvoda());


        return proizvodi;
    }
    public List<Proizvod> vratiteStoProizvodaNovi() {

        List<Proizvod> proizvodi = this.napraviteListuProizvodaOdListeIdKomentariIOcene(b.vratiteStoProizvoda());


        return proizvodi;
    }


    public List<Proizvod> vratiteProizvodeKorisnikaZaPrikazSaKomentarimaIOcenama(String email){
        List<Proizvod> proizvodi = this.napraviteListuProizvodaOdListeIdKomentariIOcene(b.vratiteSveProizvodeNekogKorisnikaPoEmailu(email));
        return proizvodi;
    }


    public void setSlike(Slike slike) {
        this.slike = slike;
    }

    @Override
    protected Proizvod napraviteReferencu(Map<String, Object> m) {
        return this.napraviteProizvodSaPodacimaIzBaze(m);// napraviteProizvodSaPodacimaIzBaze(m);
    }
}
