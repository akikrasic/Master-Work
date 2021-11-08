package srb.akikrasic.zahtevi.novi.svikorisnici.zahtevzapromenusifre;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.token.GenerisanjeTokena;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.zahtevzapromenusifre.RezultatZahtevaZaPromenuSifre;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class ZahtevZaPromenuSifre {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private JavaMailSenderImpl mail;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    private String url="http://localhost:4200/promenaSifre/";

    private void posaljiteMejlZaPromenuSifre(String email, String token){
        SimpleMailMessage poruka = new SimpleMailMessage();
        poruka.setTo(email);
        poruka.setSubject("Промена шифре");
        String kodiraniToken = Base64.encode(token.getBytes());
        String enkodovaniKodiraniToken = radSaGenerisanjemTokena.enkodujteUURLFormat(kodiraniToken);
        StringBuilder sb = new StringBuilder("Кликните на следећи линк да бисте променили шифру.: ").append(url).append(radSaGenerisanjemTokena.enkodujteUURLFormat(email)).append("/").append(enkodovaniKodiraniToken);
        poruka.setText(sb.toString());
        mail.send(poruka);
    }

    @RequestMapping(value="/zahtevZaPromenuSifre", method= RequestMethod.GET)
    public RezultatZahtevaZaPromenuSifre zahtevZaPromenuSifre(
            @RequestParam String email
    ){
        String token = korisniciRepozitorijum.posaljiteZahtevZaPromenuSifre(email);
        if(token==null){
            return vratiteRezultat(false);
        }
        else{
            posaljiteMejlZaPromenuSifre(email, token);
            return vratiteRezultat(true);
        }
    }

    private RezultatZahtevaZaPromenuSifre vratiteRezultat(boolean zahtevUspesnoPoslat){
        RezultatZahtevaZaPromenuSifre r = new RezultatZahtevaZaPromenuSifre();
        r.setZahtevUspesnoPoslat(zahtevUspesnoPoslat);
        return r;
    }
}
