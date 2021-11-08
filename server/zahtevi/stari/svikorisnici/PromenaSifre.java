package srb.akikrasic.zahtevi.stari.svikorisnici;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.promenasifre.RezultatPromeneSifre;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.zahtevzapromenusifre.RezultatZahtevaZaPromenuSifre;
import srb.akikrasic.token.GenerisanjeTokena;

import java.util.LinkedHashMap;

/**
 * Created by aki on 8/14/17.
 */
//@RestController
public class PromenaSifre {
    @Autowired
    private Baza b;
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

    @RequestMapping(value="/zahtevZaPromenuSifre", method=RequestMethod.GET)
    public RezultatZahtevaZaPromenuSifre zahtevZaPromenuSifre(
            @RequestParam String email
    ){
        RezultatZahtevaZaPromenuSifre r = new RezultatZahtevaZaPromenuSifre();
        String token = b.posaljiteZahtevZaPromenuSifre(email);
        if(token==null)r.setZahtevUspesnoPoslat(false);
        else{
            r.setZahtevUspesnoPoslat(true);
            posaljiteMejlZaPromenuSifre(email, token);
        }
        return r;

    }
    @RequestMapping(value="/promenaSifre", method=RequestMethod.PUT)
    public RezultatPromeneSifre promeniteSifru(@RequestBody LinkedHashMap<String, Object> parametri ){
        String email = (String)parametri.get("email");
        String kodiraniToken = (String)parametri.get("token");
        String token = new String(Base64.decode(kodiraniToken));
        byte[] novaSifra = ((String)parametri.get("novaSifra")).getBytes();
        RezultatPromeneSifre r = new RezultatPromeneSifre();
        r.setUspesnoPromenjenaSifra(b.promenaSifre(email, token, novaSifra));
        return r;

    }


}
