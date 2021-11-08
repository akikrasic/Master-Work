package srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.stoproizvoda;

import srb.akikrasic.domen.Proizvod;

import java.util.List;

/**
 * Created by aki on 9/17/17.
 */
public class RezultatStoProizvoda {
    private List<Proizvod> proizvodi;
    private int ukBrojProizvoda;


    public int getUkBrojProizvoda() {
        return ukBrojProizvoda;
    }

    public void setUkBrojProizvoda(int ukBrojProizvoda) {
        this.ukBrojProizvoda = ukBrojProizvoda;
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }
}
