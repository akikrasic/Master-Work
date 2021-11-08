package srb.akikrasic.domen;

import java.util.List;
import java.util.Map;

/**
 * Created by aki on 8/22/17.
 */
public class Proizvod implements Domen<Integer>, Comparable<Integer> {

    private Korisnik korisnik;
    private int id;
    private String naziv;
    private String opis;
    private double trenutnaCena;
    private Kategorija kategorija;
    private List<String> slike;
    private List<String> kljucneReci;
    private List<Ocena> ocene;
    private List<Komentar> komentari;
    private List<KomentarIOcena> komentariIOcene;
    private boolean aktivan;
    private boolean smeDaSeMenja;
    private double prosecnaOcena;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getTrenutnaCena() {
        return trenutnaCena;
    }

    public void setTrenutnaCena(double trenutnaCena) {
        this.trenutnaCena = trenutnaCena;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public boolean isSmeDaSeMenja() {
        return smeDaSeMenja;
    }

    public void setSmeDaSeMenja(boolean smeDaSeMenja) {
        this.smeDaSeMenja = smeDaSeMenja;
    }

    public List<String> getSlike() {
        return slike;
    }

    public void setSlike(List<String> slike) {
        this.slike = slike;
    }

    public List<String> getKljucneReci() {
        return kljucneReci;
    }

    public void setKljucneReci(List<String> kljucneReci) {
        this.kljucneReci = kljucneReci;
    }

    public double getProsecnaOcena() {
        return prosecnaOcena;
    }

    public void setProsecnaOcena(double prosecnaOcena) {
        this.prosecnaOcena = prosecnaOcena;
    }

    public List<Ocena> getOcene() {
        return ocene;
    }

    public void setOcene(List<Ocena> ocene) {
        this.ocene = ocene;
    }

    public List<Komentar> getKomentari() {
        return komentari;
    }

    public void setKomentari(List<Komentar> komentari) {
        this.komentari = komentari;
    }

    public List<KomentarIOcena> getKomentariIOcene() {
        return komentariIOcene;
    }

    public void setKomentariIOcene(List<KomentarIOcena> komentariIOcene) {
        this.komentariIOcene = komentariIOcene;
    }

    public static Proizvod izBaze(Map<String, Object> m){
        Proizvod p = new Proizvod();
        p.setId((Integer)m.get("id"));
        p.setNaziv((String)m.get("naziv"));
        p.setOpis((String)m.get("opis"));
        //stalno je bacalo gresku da je obj tipa float pa greska da treba da bude double, a kad se promeni onda je isla obrnuta greska
        //zato kao string pa od njega double
        String trenutnaCena=  m.get("trenutnacena").toString();
        p.setTrenutnaCena(Double.parseDouble(trenutnaCena));
        p.setAktivan((Boolean)m.get("aktivan"));
        p.setProsecnaOcena((Double)m.get("prosecna_ocena"));
        return p;
    }

    public String toString(){
        return new StringBuilder().append(id).append(":").append(naziv).toString() ;
    }
    public boolean equals(Object o){
        return this.id==((Proizvod)o).getId();
    }

    @Override
    public Integer getIdentitet() {
        return getId();
    }

    @Override
    public int compareTo(Integer o) {
        return this.id-o;
    }
}
