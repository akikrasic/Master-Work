package srb.akikrasic.domen;

import java.util.Map;

/**
 * Created by aki on 12/5/17.
 */
public class NaruceniProizvodBezNarudzbenice extends NaruceniProizvod{

    public static NaruceniProizvodBezNarudzbenice izNarucenogProizvoda(NaruceniProizvod np1){
        NaruceniProizvodBezNarudzbenice np = new NaruceniProizvodBezNarudzbenice();
        np.setId(np1.getId());
        np.setKolicina(np1.getKolicina());
        np.setCena(np1.getCena());
        np.setCenaPutaKolicina(np1.getCenaPutaKolicina());
        //System.out.println(mapa);
        np.setProdavacPotvrdio(np1.isProdavacPotvrdio());
        np.setKupacPotvrdio(np1.isKupacPotvrdio());
        np.setOtkazan(np1.isOtkazan());
        np.setNarudzbenica(null);
        np.setProizvod(np1.getProizvod());
        np.setZalba(np1.getZalba());
        np.setKomentar(np1.getKomentar());
        return np;
    }
    public static NaruceniProizvodBezNarudzbenice izBaze(Map<String,Object> mapa){
        NaruceniProizvodBezNarudzbenice np = new NaruceniProizvodBezNarudzbenice();
        np.setId((Integer)mapa.get("id"));
        np.setKolicina((Double)mapa.get("kolicina"));
        np.setCena((Double)mapa.get("cena"));
        np.setCenaPutaKolicina((Double)mapa.get("cenaputakolicina"));
        //System.out.println(mapa);
        np.setProdavacPotvrdio((Boolean)mapa.get("prodavacpotvrdio"));
        np.setKupacPotvrdio((Boolean)mapa.get("kupacpotvrdio"));
        np.setOtkazan((Boolean)mapa.get("otkazan"));
        np.setNarudzbenica(null);//eksplicitno
        np.setKomentar((String)mapa.get("komentar"));
        return np;
    }

}
