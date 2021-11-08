package srb.akikrasic.baza.novipaket.baza.osnovneklase;

import srb.akikrasic.domen.Domen;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aki on 11/28/17.
 */
public abstract class BazaOsnovnaSaHesMapom<K,V extends Domen > extends BazaOsnovna {
//neka ostane za uspomenu i za svaki slucaj, poisle ce dju obrisem


    public <K,V> HashMap<K,V> vratiteHashMapuNaPocetku(String query ){
        HashMap<K,V> mapa  = new LinkedHashMap<>();
        this.izvrsiteQuery(query).forEach(m->{
            V obj = (V)  this.napraviteReferencu(m);//ne razumem sto ide konverzija kad imam ovamo sve
            K kljuc = (K)((Domen)obj).getIdentitet();
            mapa.put(kljuc, obj);
        });
        return mapa;
    }//to ne diraj jer je sve lepo rabotilo
    /*
    public <K,V> Map<K,V> vratiteHashMapuNaPocetku(String query ){
        Map<K,V> mapa  = new TreeMap<>();
        this.izvrsiteQuery(query).forEach(m->{
            V obj = (V)  this.napraviteReferencu(m);//ne razumem sto ide konverzija kad imam ovamo sve
            K kljuc = (K)((Domen)obj).getIdentitet();
            mapa.put(kljuc, obj);
        });
        return mapa;
    }*/
    protected abstract V napraviteReferencu(Map<String, Object> m);

}
