package srb.akikrasic.zahtevi.stari.registrovanikorisnik;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.odjava.RezultatOdjaveRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Tokeni;

/**
 * Created by aki on 8/19/17.
 */
//@Component
//@Aspect
public class RegistrovaniKorisnikBezbednost {
    @Autowired
    private Tokeni radSaTokenima;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    @Around("execution(* srb.akikrasic.zahtevi.stari.registrovanikorisnik.RegistrovaniKorisnikPrijemZahteva.*(..))")
    public RezultatRegistrovaniKorisnik ispitivanjeKorisnikHederaZaPozivSvakeMetode(ProceedingJoinPoint pjp) throws Throwable {
        Object[] argumenti = pjp.getArgs();//uvek ce prvi argument biti heder u svim metodama, to je neka konvencija koje se treba drzati
        String emailToken = radSaGenerisanjemTokena.dekodujteURLFormat((String) argumenti[0]);

        String[] niz = emailToken.split(":");
        String email = niz[0];
        String token = niz[1];
        if (radSaTokenima.daLiJeUlogovanKorisnik(email, token)) {
            return (RezultatRegistrovaniKorisnik) pjp.proceed(argumenti);

        }
        RezultatRegistrovaniKorisnik r = new RezultatOdjaveRegistrovaniKorisnik();

        r.dosloJeDoNeovlascenogPristupa();
        return r;
    }


    @Around("execution(* srb.akikrasic.zahtevi.stari.registrovanikorisnik.RegistrovaniKorisnikUploadSlike.*(..))")
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

    @Around("execution(* srb.akikrasic.zahtevi.stari.registrovanikorisnik.RegistrovaniKorisnikPretragaProizvodaZaIzmenu.*(..))")
    public RezultatRegistrovaniKorisnik ispitivanjeKorisnikHederaZaPozivPretrageProizvodaZaIzmenu(ProceedingJoinPoint pjp) throws Throwable {
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
