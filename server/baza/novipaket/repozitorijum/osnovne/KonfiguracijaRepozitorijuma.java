package srb.akikrasic.baza.novipaket.repozitorijum.osnovne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.repozitorijum.*;

import javax.annotation.PostConstruct;

/**
 * Created by aki on 12/9/17.
 */


@Component
public class KonfiguracijaRepozitorijuma implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private NepotvrdjeniKorisniciRepozitorijum nepotvrdjeniKorisniciRepozitorijum;

    @Autowired
    private ZalbeRepozitorijum zalbeRepozitorijum;

    @Autowired
    private  KomentariIOceneRepozitorijum komentariIOceneRepozitorijum;

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private  NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;

    @Autowired
    private BlogoviRepozitorijum blogoviRepozitorijum;

    @Autowired
    private KorisnikOcenaRepozitorijum korisnikOcenaRepozitorijum;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        kategorijeRepozitorijum.inicijalizacija();
        korisniciRepozitorijum.inicijalizacija();

        //ubaceno 21.2.2018.
        blogoviRepozitorijum.inicijalizacija();

        nepotvrdjeniKorisniciRepozitorijum.inicijalizacija();
        zalbeRepozitorijum.inicijalizacija();
        //komentariIOceneRepozitorijum.inicijalizacija();
        proizvodiRepozitorijum.inicijalizacija();
        narudzbeniceRepozitorijum.inicijalizacija();
        naruceniProizvodiRepozitorijum.inicijalizacija();
        naruceniProizvodiRepozitorijum.inicijalizacijaIzRepozitorijumaNarudzbenica();
        korisnikOcenaRepozitorijum.inicijalizacija();
    }
    /*
    @Bean
    public KategorijeRepozitorijum napraviteKategorijeRepozitorijum(){
        return new KategorijeRepozitorijum();
    }
    @Bean
    public KomentariIOceneRepozitorijum napraviteKomentariIOceneRepozitorijum(){
        KomentariIOceneRepozitorijum ko = new KomentariIOceneRepozitorijum();
        ko.setKorisniciRepozitorijum(this.napraviteKorisniciRepozitorijum());
        ko.setProizvodiRepozitorijum(this.napraviteProizvodiRepozitorijum());
        return ko;
    }
    @Bean
    public KorisniciRepozitorijum napraviteKorisniciRepozitorijum(){
        return new KorisniciRepozitorijum();

     }
    @Bean
    public NaruceniProizvodiRepozitorijum napraviteNaruceniProizvodiRepozitorijum(){
        NaruceniProizvodiRepozitorijum npr = new NaruceniProizvodiRepozitorijum();
        npr.setNarudzbeniceRepozitorijum(this.napraviteNarudzbeniceRepozitorijum());
        npr.setProizvodiRepozitorijum(this.napraviteProizvodiRepozitorijum());
        npr.setZalbeRepozitorijum(this.napraviteZalbeRepozitorijum());
        return npr;
    }
    @Bean
    public NarudzbeniceRepozitorijum napraviteNarudzbeniceRepozitorijum(){
        NarudzbeniceRepozitorijum nr = new NarudzbeniceRepozitorijum();
        nr.setKorisniciRepozitorijum(this.napraviteKorisniciRepozitorijum());
        nr.setNaruceniProizvodiRepozitorijum(this.napraviteNaruceniProizvodiRepozitorijum());
        return nr;
    }
    @Bean
    public NepotvrdjeniKorisniciRepozitorijum napraviteNepotvrdjeniKorisniciRepozitorijum(){
       NepotvrdjeniKorisniciRepozitorijum npr= new NepotvrdjeniKorisniciRepozitorijum();
       npr.setKorisniciR(this.napraviteKorisniciRepozitorijum());
       return npr;
    }
    @Bean
    public ProizvodiRepozitorijum napraviteProizvodiRepozitorijum(){
       ProizvodiRepozitorijum pr = new ProizvodiRepozitorijum();
       pr.setKategorijeRepozitorijum(this.napraviteKategorijeRepozitorijum());
       pr.setKomentariIOceneRepozitorijum(this.napraviteKomentariIOceneRepozitorijum());
       pr.setKorisniciRepozitorijum(this.napraviteKorisniciRepozitorijum());
       return pr;
    }
    @Bean
    public ZalbeRepozitorijum napraviteZalbeRepozitorijum(){
        return new ZalbeRepozitorijum();
    }
*/

}
