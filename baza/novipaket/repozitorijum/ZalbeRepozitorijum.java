package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaZalba;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Zalba;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aki on 12/4/17.
 */
@Component

public class ZalbeRepozitorijum extends RepozitorijumOsnovna<Integer, Zalba> {


    public void inicijalizacija(){
        super.inicijalizacija();

    }
    private HashMap<Integer, Zalba> mapaNaruceniProizvodIdZaZalbu = new HashMap<>();
    @Autowired
    public void setBaza(BazaZalba b){
        this.b = b;
        super.setBazaOsnovna(b);
        query =" SELECT * FROM zalba";
    }
    private BazaZalba b;

    public Zalba vratiteZalbuZaNaruceniProizvodPoZalbaId(int zalbaId){
        return mapa.get(zalbaId);
    }
    public Zalba vratiteZalbuZaNaruceniProizvod(NaruceniProizvod np){
        return np.getZalba();//ajde da vidimo, jeste da je malo bez veze ali bi trebalo da radia u sustini teka si je
    }
    public boolean korisnikSeZalio(NaruceniProizvod np, String tekstKupca){
        int id= b.korisnikSeZalio(np, tekstKupca);
        Zalba z = new Zalba();
        z.setTekstKupca(tekstKupca);
        z.setDatumKupca(LocalDateTime.now());
        z.setId(id);
        np.setZalba(z);
        mapa.put(z.getIdentitet(), z);
        return true;//pa teka si je nema kvo
    }
    public boolean prodavacOdgovaraNaZalbu(Zalba z){
        boolean rez = this.b.prodavacOdgovaraNaZalbu(z);
        if(rez){
            z.setDatumProdavca(LocalDateTime.now());
        }
        return rez;

        //trebalo bi da je sve vec lepo postavljeno iz metode koja poziva, tako da nema problema
    }
    public boolean obrisiteZalbu(Zalba z){
        this.mapa.remove(z.getIdentitet());//nema potrebe za dodajteu mapi i izbacite iz mapu elegenatno je resenje i cao
        return b.obrisiteZalbu(z);
    }
    @Override
    protected Zalba napraviteReferencu(Map<String, Object> m) {
        return  Zalba.izBaze(m);

    }

}
