package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.pretragaproizvodazaizmenu;

import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aki on 9/8/17.
 */
public class RezultatPretrageProizvoda extends RezultatRegistrovaniKorisnik{
    private List<Proizvod> lista ;
    private long ukBrojProizvoda;


    public RezultatPretrageProizvoda(){
        lista = new ArrayList<>();
    }

    public List<Proizvod> getLista() {
        return lista;
    }

    public void setLista(List<Proizvod> lista) {
        this.lista = lista;
    }

    public long getUkBrojProizvoda() {
        return ukBrojProizvoda;
    }

    public void setUkBrojProizvoda(long ukBrojProizvoda) {
        this.ukBrojProizvoda = ukBrojProizvoda;
    }
}
