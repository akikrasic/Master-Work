package srb.akikrasic.zahtevi.stari.admin.bezbednost;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import srb.akikrasic.zahtevi.rezultati.novi.admin.odjava.RezultatOdjaveAdmin;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Tokeni;


/**
 * Created by aki on 8/19/17.
 */
//@Aspect
//@Component
public class AdminBezbednost {
    @Autowired
    private Tokeni radSaTokenima;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;
    //znaci promenjeno * umesto za klasu!
    @Around("execution(* srb.akikrasic.zahtevi.stari.admin.*.*(..))")
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
