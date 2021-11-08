package srb.akikrasic.niti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.baza.novipaket.baza.BazaNiti;

/**
 * Created by aki on 11/3/17.
 */
@Component
public class ResetovanjeTokenaZaPrivremenuSifru extends PeriodicniPosao {
    @Autowired
    private BazaNiti b;

    public static int BROJ_MINUTA_ZA_PONAVLJANJE=5;
    public static int BROJ_MINUTA_ZA_PERIOD=20;

    @Autowired
    public ResetovanjeTokenaZaPrivremenuSifru(){
        super(BROJ_MINUTA_ZA_PONAVLJANJE);
    }
    public void izvrsavanjeZadatka(){
        b.proveriteDaLiSuTokeniZaPromenuSifreValidni(BROJ_MINUTA_ZA_PERIOD);
    }

}
