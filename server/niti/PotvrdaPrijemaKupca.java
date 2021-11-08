package srb.akikrasic.niti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.baza.novipaket.baza.BazaNiti;

/**
 * Created by aki on 11/3/17.
 */
@Component
public class PotvrdaPrijemaKupca extends PeriodicniPosao {
    @Autowired
    private BazaNiti b;

    public static int BROJ_MINUTA_ZA_PONAVLJANJE=12*60;
    public static int BROJ_DANA_ZA_PERIOD=30;

    public PotvrdaPrijemaKupca() {
        super(BROJ_MINUTA_ZA_PONAVLJANJE);
    }

    public void izvrsavanjeZadatka(){
        b.proveriteDaLiJeKupacOdgovorioNaPoslatProizvod(BROJ_DANA_ZA_PERIOD);
    }
}
