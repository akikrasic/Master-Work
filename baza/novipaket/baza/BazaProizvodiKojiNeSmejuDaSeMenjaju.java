package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.token.ProizvodSmeDaSeMenja;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaProizvodiKojiNeSmejuDaSeMenjaju extends BazaOsnovna {

    @Autowired
    private ProizvodSmeDaSeMenja proizvodSmeDaSeMenja;

    @PostConstruct
    public void inic(){
        //pravi se sa bazom zato sto koriste bazu za inicijalizaciju
        //posto nema Lazy anotaciju pravi se na pocetku, u sustini je svejedno
        proizvodSmeDaSeMenja.inicijalizacija(this.proizvodiKojiNeSmejuDaSeMenjaju());


    }
    public List<Integer> proizvodiKojiNeSmejuDaSeMenjaju(){
        List<Integer> rezultat = new ArrayList<>();
        List<Map<String, Object>> l = b.queryForList("SELECT DISTINCT proizvod_id FROM naruceniproizvod ");
        l.forEach(m->{
            rezultat.add((Integer)m.get("proizvod_id"));
        });
        //System.out.println(rezultat);
        return rezultat;
    }
    public ProizvodSmeDaSeMenja getProizvodSmeDaSeMenja() {
        return proizvodSmeDaSeMenja;
    }

    public void setProizvodSmeDaSeMenja(ProizvodSmeDaSeMenja proizvodSmeDaSeMenja) {
        this.proizvodSmeDaSeMenja = proizvodSmeDaSeMenja;
    }


}
