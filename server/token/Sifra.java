package srb.akikrasic.token;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by aki on 8/1/17.
 */
@Component
 public class Sifra {
    private MessageDigest md;
    private byte[] saltPrednji = "Служи за хеширање шифре и додаје се са предње стране стринга".getBytes();
    private byte[] saltZadnji = "Додаје се са задње стране".getBytes();
    public Sifra(){
        try {
             md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] nadoveziteNiz(byte[] sifra){
        byte[] niz = new byte[saltPrednji.length+sifra.length+saltZadnji.length];
        for(int i=0;i<saltPrednji.length;i++){
            niz[i]=saltPrednji[i];
        }
        int k = saltPrednji.length+sifra.length;
        for(int j=0 ,i=saltPrednji.length;i<k;i++,j++){
            niz[i]= sifra[j];
        }
        int l=k+saltZadnji.length;
        for(int j=0,i=k;i<l;i++,j++){
            niz[i]=saltZadnji[j];
        }
        return niz;
    }

    public byte[] hesirajteSifru(byte[] sifra){
        byte[] nizZaHesiranje = nadoveziteNiz(sifra);
        return md.digest( nizZaHesiranje);

    }
    public boolean proveriteSifru(byte[] sifraZaProveru, byte[] hesIzBaze){
        return Arrays.equals(hesirajteSifru(sifraZaProveru), hesIzBaze);

    }

    public byte[] getSaltPrednji(){
        return this.saltPrednji;
    }
    public byte[] getSaltZadnji(){
        return this.saltZadnji;
    }
}
