package srb.akikrasic.zahtevi.novi.bezbednost;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Tokeni;
import srb.akikrasic.zahtevi.rezultati.novi.admin.odjava.RezultatOdjaveAdmin;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 12/21/17.
 */

@Aspect
@Component
public class AdminBezbednost {

    @Autowired
    private Tokeni radSaTokenima;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;
    //znaci promenjeno * umesto za klasu!
    @Around("execution(* srb.akikrasic.zahtevi.stari.admin.*.izvrsiteZahtev(..))")
    public RezultatAdmin ispitivanjeAdminHederaZaPozivSvakeMetode(ProceedingJoinPoint pjp) throws Throwable {
        Object[] argumenti = pjp.getArgs();//uvek ce prvi argument biti heder u svim metodama, to je neka konvencija koje se treba drzati
        String token = radSaGenerisanjemTokena.dekodujteURLFormat((String)argumenti[0]);
        if(radSaTokenima.daLiJeUlogovanAdmin(token)){
            return (RezultatAdmin) pjp.proceed(argumenti);

        }
        RezultatAdmin r = new RezultatOdjaveAdmin();

        r.dosloJeDoNeovlascenogPristupa();
        return r;
    }
}
