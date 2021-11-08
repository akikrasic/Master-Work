package srb.akikrasic.niti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.baza.novipaket.baza.BazaNiti;

/**
 * Created by aki on 11/4/17.
 */
@Component
public class ProdavacNijeOdgovorioNaZalbu extends PeriodicniPosao {
    @Autowired
    private BazaNiti b;

    public static int BROJ_MINUTA_ZA_PONAVLJANJE=12*60;
    public static int BROJ_DANA_ZA_PERIOD=5;

    ProdavacNijeOdgovorioNaZalbu(){
        super(BROJ_MINUTA_ZA_PONAVLJANJE);
    }
    @Override
    public void izvrsavanjeZadatka() {
        b.proveriteDaLiJeProdavacOdgovorioNaZalbu(BROJ_DANA_ZA_PERIOD);
    }
}
