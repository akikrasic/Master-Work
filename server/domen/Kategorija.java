package srb.akikrasic.domen;

import java.util.Map;

/**
 * Created by aki on 8/20/17.
 */
public class Kategorija implements Domen<Integer>, Comparable<Integer> {
    private int id;
    private String naziv;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdentitet(){
        return this.getId();
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public static Kategorija kategorijaIzBaze(Map<String, Object> mapa){
        Kategorija k = new Kategorija();
        k.setId((Integer)mapa.get("id"));
        k.setNaziv((String)mapa.get("naziv"));
        return k;
    }

    public static Kategorija izBaze(Map<String, Object> m){
        Kategorija k = new Kategorija();
        k.setNaziv((String)m.get("naziv"));
        k.setId((Integer)m.get("id"));
        return k;
    }

    @Override
    public int compareTo(Integer id) {
        return this.id-id;
    }
}
