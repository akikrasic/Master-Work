package srb.akikrasic.domen;

import java.util.Map;

/**
 * Created by aki on 9/17/17.
 */
public class Ocena implements Domen<Integer> {

    private int id;
    private Korisnik korisnik;
    private Proizvod proizvod;
    private int ocena;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
    public static Ocena izBaze(Map<String, Object> mapa){
        Ocena o = new Ocena();
        o.setId((Integer)mapa.get("ocena_id"));
        o.setOcena((Integer)mapa.get("ocena"));
        return o;
    }
    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
