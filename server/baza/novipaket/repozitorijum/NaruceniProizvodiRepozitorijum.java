package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaNaruceniProizvod;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.*;
import srb.akikrasic.korisne.MapaSaListom;

import java.util.*;

/**
 * Created by aki on 12/4/17.
 */
@Component
public class NaruceniProizvodiRepozitorijum extends RepozitorijumOsnovna<Integer, NaruceniProizvod>{


    @Autowired
    public void setBaza(BazaNaruceniProizvod b){
        super.setBazaOsnovna(b);
        this.b=b;
        query="SELECT * FROM naruceniproizvod ORDER BY id";

    }
    private BazaNaruceniProizvod b;

    @Autowired
    private ZalbeRepozitorijum zalbeRepozitorijum;

    @Autowired
    private NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;

    @Autowired
    private  ProizvodiRepozitorijum proizvodiRepozitorijum;


    public void inicijalizacija(){
        super.inicijalizacija();

    }

    public void inicijalizacijaIzRepozitorijumaNarudzbenica(){

        //  zalbeRepozitorijum.inicijalizacijaIzNarucenogProizvoda();
        mapa.values().forEach(np->{
            NaruceniProizvodBezNarudzbenice npBez = NaruceniProizvodBezNarudzbenice.izNarucenogProizvoda(np);
            mapaNaruceniProizvodiZaNarudzbenicu.dodajteUMapu(np.getNarudzbenica().getId(), npBez);
            mapaSaNarucenimProizvodimaBezNarudzbenice.put(npBez.getIdentitet(), npBez);
        });
        narudzbeniceRepozitorijum.inicijalizacijaIzRepozitorijumaNarucenihProizvodaDaSeNapraveNarudzbeniceSaNarucenimProizvodima();

    }

    private MapaSaListom<Integer, NaruceniProizvod> mapaNaruceniProizvodiZaNarudzbenicu = new MapaSaListom<>();
    private HashMap<Integer, NaruceniProizvod> mapaSaNarucenimProizvodimaBezNarudzbenice = new LinkedHashMap<>();

    @Override
    public void dodajteUMapu(NaruceniProizvod np){
        super.dodajteUMapu(np);
        NaruceniProizvodBezNarudzbenice npBez = NaruceniProizvodBezNarudzbenice.izNarucenogProizvoda(np);
        mapaNaruceniProizvodiZaNarudzbenicu.dodajteUMapu(np.getNarudzbenica().getId(), npBez);
        mapaSaNarucenimProizvodimaBezNarudzbenice.put(npBez.getIdentitet(), npBez);

    }
    @Override
    public void obrisiteIzMapu(NaruceniProizvod np){
        super.obrisiteIzMapu(np);
        mapaNaruceniProizvodiZaNarudzbenicu.obrisiteIzMapu(np.getNarudzbenica().getId(), np);
        mapaSaNarucenimProizvodimaBezNarudzbenice.remove(np.getIdentitet());
    }



    public void snimiteNaruceniProizvod(NaruceniProizvod np) {
        b.snimiteNaruceniProizvod(np);
        this.dodajteUMapu(np);

    }

    public boolean obrisiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){
        if(b.obrisiteNaruceneProizvodeZaNarudzbenicu(n)){
            n.getNaruceniProizvodi().forEach(np->{
                this.obrisiteIzMapu(np);
            });
            return true;
        }
        return false;
    }


    public boolean obrisiteNaruceniProizvod(int id){
        if(b.obrisiteNaruceniProizvod(id)){
            NaruceniProizvod np = mapa.get(id);
            obrisiteIzMapu(np);
            return true;
        }
        return false;

    }


    public List<NaruceniProizvod> vratiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){//da ne kvarimo api neka ostane Narudzbenica iako moze id samo
        //radi isto kaoi metoda ispod
        List<Integer> listaIdjeva = b.vratiteNaruceneProizvodeZaNarudzbenicu(n);
        List<NaruceniProizvod> listaNarucenihProizvoda = new ArrayList<>();
        listaIdjeva.forEach(i->{
            listaNarucenihProizvoda.add(mapa.get(i));
        });
        //return mapaNaruceniProizvodiZaNarudzbenicu.uzmite(n.getId());
        return listaNarucenihProizvoda;
    }
    public List<NaruceniProizvod> vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference(Narudzbenica n){
        return mapaNaruceniProizvodiZaNarudzbenicu.uzmite(n.getId());
    }

    public NaruceniProizvod vratiteNaruceniProizvodPoId(int id){
        //mapa sa Narudzbenicu
        return mapa.get(id);
    }
    public List<NaruceniProizvod> vratiteNaruceneProizvodeProdavca(String email){
        List<Integer> rezLista = b.vratiteNaruceneProizvodeProdavca(email);
        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();
        rezLista.forEach(i->{
            naruceniProizvodi.add(this.mapa.get(i));
        });
        return naruceniProizvodi;
    }
    public List<NaruceniProizvod> vratiteNaruceneProizvodeProdavcaLimit(String email, int pocetak, int pomeraj){
        List<Integer> rezLista = b.vratiteNaruceneProizvodeProdavcaLimit(email, pocetak, pomeraj);
        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();
        rezLista.forEach(i->{
            naruceniProizvodi.add(this.mapa.get(i));
        });
        return naruceniProizvodi;

    }
    public boolean prodavacPotvrdjujeNaruceniProizvod(NaruceniProizvod np){
        if(b.prodavacPotvrdjujeNaruceniProizvod(np)){
            this.mapa.get(np.getIdentitet()).setProdavacPotvrdio(true);
            this.mapaSaNarucenimProizvodimaBezNarudzbenice.get(np.getIdentitet()).setProdavacPotvrdio(true);
            return true;
        }
        return false;
    }
    public boolean kupacPotvrdjujeNaruceniProizvod(NaruceniProizvod np){
        if(b.kupacPotvrdjujeNaruceniProizvod(np)){
            this.mapa.get(np.getIdentitet()).setKupacPotvrdio(true);
            this.mapaSaNarucenimProizvodimaBezNarudzbenice.get(np.getIdentitet()).setKupacPotvrdio(true);
            return true;
        }
        return false;
    }
    public boolean otkazanNaruceniProizvod(NaruceniProizvod np){
        if(b.otkazanNaruceniProizvod(np)){
            this.mapa.get(np.getIdentitet()).setOtkazan(true);
            this.mapaSaNarucenimProizvodimaBezNarudzbenice.get(np.getIdentitet()).setOtkazan(true);
            return true;
        }
        return false;
    }
    public List<NaruceniProizvod> izvestajZaAdmina(int limit, int offset) {
        List<Integer> l = b.izvestajZaAdmina(limit, offset);

        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();

        l.forEach(i->{
            naruceniProizvodi.add(mapa.get(i));
        });

        return naruceniProizvodi;
    }


    public Map<Integer, NaruceniProizvod> getMappa(){
        return this.mapa;
    }

    public NaruceniProizvod napraviteNaruceniproizvodSaPodacimaIzBaze(Map<String, Object> m, Narudzbenica n){
        NaruceniProizvod np = NaruceniProizvod.izBaze(m);
        np.setNarudzbenica(n);
        np.setProizvod(proizvodiRepozitorijum.vratiteProizvodPoId((Integer)m.get("proizvod_id")));
        Integer zalbaId=(Integer)m.get("zalba_id");
        if(zalbaId!=null){
            np.setZalba(zalbeRepozitorijum.vratiteZalbuZaNaruceniProizvodPoZalbaId(zalbaId));
        }
        return np;
    }

    @Override
    protected NaruceniProizvod napraviteReferencu(Map<String, Object> m) {


        Narudzbenica n = this.narudzbeniceRepozitorijum.vratiteNarudzbenicuPoIdBezNarucenihProizvoda((Integer)m.get("narudzbenica_id"));
        return napraviteNaruceniproizvodSaPodacimaIzBaze(m,n);
    }


}
