package srb.akikrasic.domen;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aki on 8/22/17.
 */
public class NaruceniProizvod implements Domen<Integer> {
    private int id;
    private double kolicina;
    private Proizvod proizvod;
    private Narudzbenica narudzbenica;
    private double cena;
    private double cenaPutaKolicina;
    private boolean prodavacPotvrdio;
    private boolean kupacPotvrdio;
    private boolean otkazan;

    private String komentar;//25.08.2017
    private Zalba zalba;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }

    public Narudzbenica getNarudzbenica() {
        return narudzbenica;
    }

    public void setNarudzbenica(Narudzbenica narudzbenica) {
        this.narudzbenica = narudzbenica;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public double getCenaPutaKolicina() {
        return cenaPutaKolicina;
    }

    public void setCenaPutaKolicina(double cenaPutaKolicina) {
        this.cenaPutaKolicina = cenaPutaKolicina;
    }

    public boolean isProdavacPotvrdio() {
        return prodavacPotvrdio;
    }

    public void setProdavacPotvrdio(boolean prodavacPotvrdio) {
        this.prodavacPotvrdio = prodavacPotvrdio;
    }

    public boolean isKupacPotvrdio() {
        return kupacPotvrdio;
    }

    public void setKupacPotvrdio(boolean kupacPotvrdio) {
        this.kupacPotvrdio = kupacPotvrdio;
    }

    public boolean isOtkazan() {
        return otkazan;
    }

    public void setOtkazan(boolean otkazan) {
        this.otkazan = otkazan;
    }

    public Zalba getZalba() {
        return zalba;
    }

    public void setZalba(Zalba zalba) {
        this.zalba = zalba;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public static NaruceniProizvod izBaze(Map<String,Object> mapa){
        NaruceniProizvod np = new NaruceniProizvod();
        np.setId((Integer)mapa.get("id"));
        np.setKolicina((Double)mapa.get("kolicina"));
        np.setCena((Double)mapa.get("cena"));
        np.setCenaPutaKolicina((Double)mapa.get("cenaputakolicina"));
        //System.out.println(mapa);
        np.setProdavacPotvrdio((Boolean)mapa.get("prodavacpotvrdio"));
        np.setKupacPotvrdio((Boolean)mapa.get("kupacpotvrdio"));
        np.setOtkazan((Boolean)mapa.get("otkazan"));
        np.setKomentar((String)mapa.get("komentar"));
        return np;
    }
    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
