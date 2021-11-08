package srb.akikrasic.baza.novipaket.repozitorijum.osnovne;

import srb.akikrasic.domen.Domen;

import java.util.TreeMap;

/**
 * Created by aki on 12/1/17.
 */
public abstract class RepozitorijumSortiran<K, V extends Domen> extends RepozitorijumOsnovna<K, V >{

    public void inicijalizacija(){
        super.inicijalizacija();
        mapa.values().forEach(v->{
            sortiranaMapa.put((K)v.getIdentitet(), v);
        });
    }
    protected TreeMap<K,V> sortiranaMapa = new TreeMap<>();
    @Override
    protected void dodajteUMapu(V objekatZaDodavanje){
        super.dodajteUMapu(objekatZaDodavanje);
        sortiranaMapa.put((K)objekatZaDodavanje.getIdentitet(), objekatZaDodavanje);
    }
    @Override
    protected void obrisiteIzMapu(V objekatZaBrisanje){
        super.obrisiteIzMapu(objekatZaBrisanje);
        sortiranaMapa.remove(objekatZaBrisanje);
    }
}
