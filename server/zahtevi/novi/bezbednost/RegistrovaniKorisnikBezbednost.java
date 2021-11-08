package srb.akikrasic.zahtevi.novi.bezbednost;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Tokeni;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 12/21/17.
 */
@Aspect
@Component
public class RegistrovaniKorisnikBezbednost {

    @Autowired
    private Tokeni radSaTokenima;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    @Around("execution(* srb.akikrasic.zahtevi.novi.registrovanikorisnik.*.*.*.izvrsiteZahtev(..))")
    public RezultatRegistrovaniKorisnik ispitivanjeKorisnikHederaZaPozivPretrageProizvodaZaIzmenu(ProceedingJoinPoint pjp) throws Throwable {
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        try {
            Object[] argumenti = pjp.getArgs();//uvek ce prvi argument biti heder u svim metodama, to je neka konvencija koje se treba drzati

            String emailToken = radSaGenerisanjemTokena.dekodujteURLFormat((String) argumenti[0]);

            String[] niz = emailToken.split(":");
            String email = niz[0];
            String token = niz[1];


            if (radSaTokenima.daLiJeUlogovanKorisnik(email, token)) {

                r = (RezultatRegistrovaniKorisnik) pjp.proceed(argumenti);

                return r;
            }

        } catch (Exception e) {
            r.setDaLiJeURedu(false);

        }
        r.dosloJeDoNeovlascenogPristupa();
        return r;
    }


    @Around("execution(* srb.akikrasic.zahtevi.novi.registrovanikorisnik.*.*.izvrsiteZahtev(..))")
    public RezultatRegistrovaniKorisnik ispitivanjeKorisnikHederaZaPozivPretrageProizvodaZaIzmenuPaketIspod(ProceedingJoinPoint pjp) throws Throwable {
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        try {
            Object[] argumenti = pjp.getArgs();//uvek ce prvi argument biti heder u svim metodama, to je neka konvencija koje se treba drzati

            String emailToken = radSaGenerisanjemTokena.dekodujteURLFormat((String) argumenti[0]);

            String[] niz = emailToken.split(":");
            String email = niz[0];
            String token = niz[1];



            if (radSaTokenima.daLiJeUlogovanKorisnik(email, token)) {

                r = (RezultatRegistrovaniKorisnik) pjp.proceed(argumenti);


                return r;
            }
        } catch (Exception e) {
            r.setDaLiJeURedu(false);

        }
        r.dosloJeDoNeovlascenogPristupa();
        return r;
    }


    @Around("execution(* srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.*.*.izvrsiteZahtev(..))")
    public RezultatRegistrovaniKorisnik ispitivanjeKorisnikHederaZaPozivSvakeMetodeUploadSlike(ProceedingJoinPoint pjp) throws Throwable {
        Object[] argumenti = pjp.getArgs();//uvek ce prvi argument biti heder u svim metodama, to je neka konvencija koje se treba drzati

        String emailToken = radSaGenerisanjemTokena.dekodujteURLFormat((String) argumenti[0]);

        String[] niz = emailToken.split(":");
        String email = niz[0];
        String token = niz[1];




        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        if (radSaTokenima.daLiJeUlogovanKorisnik(email, token)) {
            try {
                r = (RezultatRegistrovaniKorisnik) pjp.proceed(argumenti);


            } catch (Exception e) {
                r.setDaLiJeURedu(false);

            }
            return r;
        }

        r.dosloJeDoNeovlascenogPristupa();
        return r;
    }

}