package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaKategorija;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.KonfiguracijaRepozitorijuma;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumSortiran;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.domen.uporedjivaci.KategorijaUporedjivac;

import java.util.*;

/**
 * Created by aki on 11/29/17.
 */
@Component
public class KategorijeRepozitorijum extends RepozitorijumSortiran<Integer, Kategorija> {


    private void napraviteSortiranuListuKategorija(){
        this.sortiranaListaKategorija= new ArrayList<>();
        sortiranaListaKategorija.addAll(this.mapaNazivKljuc.values());
    }

    public void inicijalizacija(){
        super.inicijalizacija();
        sortiranaMapa.values().forEach(k->{
            mapaNazivKljuc.put(k.getNaziv(), k);
        });
        napraviteSortiranuListuKategorija();
        //konfig.toString();
    }
    private TreeMap<String, Kategorija> mapaNazivKljuc = new TreeMap<>();
    private List<Kategorija> sortiranaListaKategorija;

    protected void dodajteUMapu(Kategorija k){
        super.dodajteUMapu(k);
        mapaNazivKljuc.put(k.getNaziv(), k);
        napraviteSortiranuListuKategorija();
    }
    protected void obrisiteIzMapu(Kategorija k){
       super.obrisiteIzMapu(k);
        mapaNazivKljuc.remove(k.getNaziv());
        napraviteSortiranuListuKategorija();

    }

    @Override
    protected Kategorija napraviteReferencu(Map<String, Object> m) {
        return Kategorija.izBaze(m);
    }


    @Autowired
    public void setBaza(BazaKategorija b){
        super.setBazaOsnovna(b);
        this.b =b;
        query ="SELECT * FROM kategorija ORDER BY naziv";
    }

    private BazaKategorija b;

    public boolean dodajteKategoriju(String naziv){
        if(mapaNazivKljuc.get(naziv)!=null)return false;
        int id=b.dodajteKategoriju(naziv);
        Kategorija k = new Kategorija();
        k.setId(id);
        k.setNaziv(naziv);
        this.dodajteUMapu(k);
        return true;
    }


    public List<Kategorija> vratiteSveKategorije() {

        return this.sortiranaListaKategorija;
    }

    public boolean izmenaKategorije(int id, String noviNaziv) {
        if(mapaNazivKljuc.get(noviNaziv)!=null){
            return false;
        }
        if( b.izmenaKategorije(id, noviNaziv)){
            Kategorija k = mapa.get(id);
            mapaNazivKljuc.remove(k.getNaziv());
            k.setNaziv(noviNaziv);
            mapaNazivKljuc.put(noviNaziv,k);//a tamo si sve ostaje isto jer je ista referenca
            return true;
        }
        return false;

    }

    public boolean obrisiteKategoriju(int id) {
        boolean rez = b.obrisiteKategoriju(id);
        if(!rez)return false;
        this.obrisiteIzMapu(mapa.get(id));
        return true;
    }

    public Kategorija vratiteKategorijuPoId(int id) {
        return mapa.get(id);
    }

    public boolean daLiSeNazivKategorijeNalaziUBazi(String naziv) {
        //to ce da se resi s drugi hash elegantno nema kvo
        return mapaNazivKljuc.get(naziv)!=null;

    }
    public Kategorija vratiteKategorijuPoNazivu(String naziv){
        return mapaNazivKljuc.get(naziv);
    }
}
