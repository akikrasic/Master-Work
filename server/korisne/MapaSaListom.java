package srb.akikrasic.korisne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aki on 12/6/17.
 */
public class MapaSaListom<K,V> {
    private HashMap<K,List<V>> mapa = new LinkedHashMap<>();

    public void dodajteUMapu(K kljuc, V vrednost){
        List<V> lista = mapa.get(kljuc);
        if(lista==null){
            lista = new ArrayList<V>();
            mapa.put(kljuc, lista);
        }
        lista.add(vrednost);

    }
    public void obrisiteIzMapu(K kljuc, V vrednost){
        List<V> lista = mapa.get(kljuc);
        if(lista ==null){
            return;//za svaki slucaj iako ne bi trebalo da se desava u praksi
        }
        lista.remove(vrednost);//jeste da je ovo sporo, ali to je jedini nacin...
    }
    public List<V> uzmite(K kljuc){
        return mapa.get(kljuc);
    }








}
