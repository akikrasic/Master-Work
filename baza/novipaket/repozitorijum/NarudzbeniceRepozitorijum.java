package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaNaruceniProizvod;
import srb.akikrasic.baza.novipaket.baza.BazaNarudzbenica;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.*;

import java.util.*;

/**
 * Created by aki on 12/4/17.
 */
@Component
public class NarudzbeniceRepozitorijum extends RepozitorijumOsnovna<Integer, Narudzbenica> {

    @Autowired
    public void setBaza(BazaNarudzbenica b){
        super.setBazaOsnovna(b);
        this.b = b;
        query="SELECT * FROM narudzbenica ORDER BY id";
    }

    private BazaNarudzbenica b;
    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;


    public void setKorisniciRepozitorijum(KorisniciRepozitorijum korisniciRepozitorijum) {
        this.korisniciRepozitorijum = korisniciRepozitorijum;
    }

    private HashMap<Integer, Narudzbenica> narudzbenicaSaNarucenimProizvodima = new LinkedHashMap<>();



    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;



    private HashMap<Integer, NarudzbenicaBezNarucenihProizvoda>mapaBezNarucenihProizvoda= new LinkedHashMap<>();

    public void inicijalizacija(){
        super.inicijalizacija();
    }
    public void inicijalizacijaIzRepozitorijumaNarucenihProizvodaDaSeNapraveNarudzbeniceSaNarucenimProizvodima(){

        mapa.values().forEach(nbp->{
            Narudzbenica n = Narudzbenica.izNarudzbeniceBezNarucenihProizvoda((NarudzbenicaBezNarucenihProizvoda) nbp);
            vratiteNaruceneProizvodeZaNarudzbenicu(n);
            narudzbenicaSaNarucenimProizvodima.put(n.getIdentitet(), n);

        });
    }
    private void vratiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){
        n.setNaruceniProizvodi(naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeZaNarudzbenicu(n));

    }




    protected void dodajteUMapu(Narudzbenica n,NarudzbenicaBezNarucenihProizvoda nBez){

        super.dodajteUMapu(nBez);
        vratiteNaruceneProizvodeZaNarudzbenicu(n);
        this.narudzbenicaSaNarucenimProizvodima.put(n.getIdentitet(), n);

        //sad da se srede reference

    }

    protected void obrisiteIzMapu(Narudzbenica n){
        super.obrisiteIzMapu(n);
        this.narudzbenicaSaNarucenimProizvodima.remove(n.getIdentitet());
    }


    public void snimiteNarudzbenicu(Narudzbenica n){
        //to treba da se dovrsi sve lepo
        b.snimiteNarudzbenicu(n);
        //this.dodajteUMapu(n);
        //e sad naruceni proizvodi se snimau preko njihov si repozitorijum, jedan po jedan i uvezuju se elegantno
        NarudzbenicaBezNarucenihProizvoda nBez = NarudzbenicaBezNarucenihProizvoda.izNarudzbenice(n);

        n.getNaruceniProizvodi().forEach(np->{
            np.setNarudzbenica(nBez);
            naruceniProizvodiRepozitorijum.snimiteNaruceniProizvod(np);
        });

        this.dodajteUMapu(n, nBez);
    }


    public Narudzbenica vratiteNarudzbenicuPoId(int id){
       return this.narudzbenicaSaNarucenimProizvodima.get(id);
    }
    public Narudzbenica vratiteNarudzbenicuPoIdBezNarucenihProizvoda(int id){
        return mapa.get(id);
    }


    public boolean obrisiteNarudzbenicu(Narudzbenica n){
        naruceniProizvodiRepozitorijum.obrisiteNaruceneProizvodeZaNarudzbenicu(n);
        if(b.obrisiteNarudzbenicu(n)){
            this.obrisiteIzMapu(n);
            return true;
        }

        return false;
    }

    public List<Narudzbenica> vratiteNarudzbeniceKupcu(String email){
        List<Narudzbenica> l = new ArrayList<>();
        b.vratiteNarudzbeniceKupcu(email).forEach(i->{
            l.add(this.narudzbenicaSaNarucenimProizvodima.get(i));
        });
        return l;
    }
    public List<Narudzbenica> vratiteNarudzbeniceKupcuLimit(String email, int pocetak, int pomeraj){
        List<Narudzbenica> l = new ArrayList<>();
        b.vratiteNarudzbeniceKupcuLimit(email, pocetak, pomeraj).forEach(i->{
            l.add(this.narudzbenicaSaNarucenimProizvodima.get(i));
        });
        return l;
    }

    public Map<Integer, Narudzbenica> getMapa(){
        return this.mapa;
    }

    public NarudzbenicaBezNarucenihProizvoda napraviteNarudzbenicuOdPodatakaBezNarucenihProizvoda(Map<String, Object>m){
        NarudzbenicaBezNarucenihProizvoda n = NarudzbenicaBezNarucenihProizvoda.izBaze((m));
        n.setKupac(korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        return n;
    }


    @Override
    protected Narudzbenica napraviteReferencu(Map<String, Object> m) {
        return napraviteNarudzbenicuOdPodatakaBezNarucenihProizvoda(m);
    }

}
