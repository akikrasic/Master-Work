package srb.akikrasic.domen;

import java.util.List;
import java.util.Map;

/**
 * Created by aki on 9/5/17.
 */
public class KljucnaRec implements Domen<Integer> {
    private int id;
    private String naziv;

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
    public static KljucnaRec izBaze(Map<String, Object> m){
        KljucnaRec k = new KljucnaRec();
        k.setId((Integer)m.get("id"));
        k.setNaziv((String)m.get("naziv"));
        return k;
    }

    public int hashCode(){
        return id;
    }
    public boolean equals(Object o){
        KljucnaRec k = (KljucnaRec)o;
        return naziv.equals(k.getNaziv());
    }

    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
