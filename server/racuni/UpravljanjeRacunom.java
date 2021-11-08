package srb.akikrasic.racuni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaRacunSajta;
import srb.akikrasic.domen.Racun;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by aki on 10/10/17.
 */
@Component
public class UpravljanjeRacunom {

    @Autowired
    private BazaRacunSajta b;
    private double provizija =0.10;
    private double racunSajta;
    private double racunZaPrenos;
    private HashMap<String, Double> mapaKorisnikRacun ;
    @PostConstruct
    public void ucitavanjeizBaze(){
        this.racunSajta=b.vratiteStanjeRacunaSajta();
        this.racunZaPrenos=b.vratiteStanjeRacunaZaPrenos();
        mapaKorisnikRacun = b.vratiteMapuKorisnikaIStanjeRacuna();
    }
    /*
    @PreDestroy
    public void sacuvajteStanjeRacuna(){
        b.dodajteNaRacunSajta(racunSajta);
        b.dodajteNaRacunZaPrenos(racunZaPrenos);

    }
    //nema potrebe zato sto posle svakog upisa osvezavamo Bazu podataka
    */
    public void dodajteNaRacunSajta(double kolicina){
        racunSajta+=kolicina;
        b.snimiteNaRacunSajta(racunSajta);
    }
    public void dodajteNaRacunZaPrenos(double kolicina){
        racunZaPrenos+=kolicina;
        b.snimiteNaRacunZaPrenos(racunZaPrenos);
    }

    public void skiniteSaRacunaZaPrenos(double kolicina){
        racunZaPrenos-=kolicina;
        b.snimiteNaRacunZaPrenos(racunZaPrenos);
    }

    public void dodajteNaRacunKorisnika(String email, double kolicina){
        Double vrednost = mapaKorisnikRacun.get(email);
        vrednost+=kolicina;
        mapaKorisnikRacun.put(email,vrednost);
        b.snimiteStanjeNaRacunKorisnika(email, vrednost);
    }



    public double vratiteStanjeRacunaKorisnika(String email){
        return mapaKorisnikRacun.get(email);
    }

    public double vratiteStanjeRacunaSajta(){
        return racunSajta;
    }
    public double vratiteStanjeRacunaZaPrenos(){
        return racunZaPrenos;
    }

    public void prebaciteNaRacunKorisnika(String email, double kolicina){
        this.skiniteSaRacunaZaPrenos(kolicina);
        Double ideSajtu = provizija*kolicina;
        Double ideProdavcu = kolicina-ideSajtu;
        this.dodajteNaRacunSajta(ideSajtu);
        this.dodajteNaRacunKorisnika(email, ideProdavcu);
    }

    private Racun r = new Racun();

    public Racun vratiteStanjeRacuna(){
        r.setRacunZaPrenos(this.racunZaPrenos);
        r.setRacunSajta(this.racunSajta);
        return r;
    }
}
