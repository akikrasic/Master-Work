package srb.akikrasic.baza.novipaket.repozitorijum.osnovne;

import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.domen.Domen;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aki on 11/27/17.
 */
public abstract  class RepozitorijumOsnovna<K, V extends Domen> {



    protected Map<K,V> mapa;
    private BazaOsnovna bo;
    protected String query="";

    protected void pravljenjeMape(){
         mapa  = new LinkedHashMap<>();
         bo.izvrsiteQuery(query).forEach(m->{
            V obj = (V)  this.napraviteReferencu(m);//ne razumem sto ide konverzija kad imam ovamo sve
            K kljuc = (K)((Domen)obj).getIdentitet();
            mapa.put(kljuc, obj);
        });
    }

    //@PostConstruct
    protected void inicijalizacija(){
        //this.mapa = bo.vratiteHashMapuNaPocetku(query);
        pravljenjeMape();

    }
    public void setBazaOsnovna(BazaOsnovna bo){
        this.bo = bo;
    }

    protected void dodajteUMapu(V objekatZaDodavanje){
        mapa.put((K)objekatZaDodavanje.getIdentitet(), objekatZaDodavanje);
    }
    protected void obrisiteIzMapu(V objekatZaBrisanje){
        mapa.remove((K)objekatZaBrisanje.getIdentitet());
    }

    protected abstract V napraviteReferencu(Map<String, Object> m);

}
