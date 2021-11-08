package srb.akikrasic.zahtevi.stari.svikorisnici;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.registracija.RezultatRegistracije;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created by aki on 8/2/17.
 */
//@RestController
public class Registracija {
    @Autowired
    private Baza b;

    @Autowired
    private Sifra radSaSifrom;
    @Autowired
    private Tokeni radSaTokenima;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;



    //napomena vrednost regularnog izraza je preuzeta sa adrese: http://emailregex.com/
    private Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private Pattern sestIVisePattern = Pattern.compile(".{6,}");
    private Pattern maloSlovoPattern = Pattern.compile("^.{0,}[a-zа-ш]{1,}.{0,}");
    private Pattern velikoSlovoPattern =Pattern.compile("^.{0,}[A-ZА-Ш]{1,}.{0,}");
    private Pattern brojPattern = Pattern.compile("^.{0,}[0-9]{1,}.{0,}");
    private Pattern specKarakteriPattern = Pattern.compile("^.{0,}[!@#$%^&*()]{1,}.{0,}");
    private Pattern telefonPattern = Pattern.compile("^[0-9]{8,}$");

    private String url="http://localhost:4200/potvrdaRegistracije/";
    /* @RequestMapping(value = "/registracijaKorisnika/{imeNaziv}/{email}",method= RequestMethod.GET)
     public String registrujteKorisnika(@PathVariable(value = "imeNaziv") String imeNaziv,

                                        @PathVariable String email
                                        /*@RequestParam String sifra,
                                        @RequestParam String adresa,
                                        @RequestParam String mesto,
                                        @RequestParam String tel1,
                                        @RequestParam String tel2,
                                        @RequestParam String tel3
     ){


         System.out.println(imeNaziv);
         System.out.println(email);
         System.out.println("radi");
         return email;
     }*/
   /*
    @RequestMapping(value = "/registracijaKorisnika",method= RequestMethod.GET)
    public String registrujteKorisnika1(@RequestParam String imeNaziv,
                                        @RequestParam String email,
                                        @RequestParam String sifra,
                                        @RequestParam String adresa,
                                        @RequestParam String mesto,
                                        @RequestParam String tel1,
                                        @RequestParam String tel2,
                                        @RequestParam String tel3){
       baza.snimiteNovogKorisnika(imeNaziv, email,sifra, adresa, mesto, tel1, tel2, tel3, null);
       return "radi";
    }*/




    public boolean sigurnosnaProvera(String imeNaziv,String email, String sifra,String adresa, String mesto,String tel1, String tel2,String tel3){
        //b.proveraValidnostiEMaila();

        if(imeNaziv==null||email==null||sifra==null||adresa==null||mesto==null||tel1==null||tel2==null||tel3==null)return false;
        if(imeNaziv.equals(""))return false;

        if(emailPattern.matcher(email).matches()){
            if(!b.daLiJeEmailSlobodanUObeTabele(email))return false;

        }
        else return false;

        if(!sestIVisePattern.matcher(sifra).matches())return false;
        if(!maloSlovoPattern.matcher(sifra).matches())return false;
        if(!velikoSlovoPattern.matcher(sifra).matches())return false;
        if(!brojPattern.matcher(sifra).matches())return false;
        if(!specKarakteriPattern.matcher(sifra).matches())return false;

        if(adresa.equals(""))return false;
        if(mesto.equals(""))return false;
        if(tel1.equals(""))return false;
        if(!telefonPattern.matcher(tel1).matches())return false;
        if(!tel2.equals(""))
            if(!telefonPattern.matcher(tel2).matches())return false;
        if(!tel3.equals(""))
            if(!telefonPattern.matcher(tel3).matches())return false;

        return true;
    }

    @Autowired
    private JavaMailSenderImpl mail;
    private void posaljiteMejl(String email, String token){
        SimpleMailMessage poruka = new SimpleMailMessage();
        poruka.setTo(email);
        poruka.setSubject("Потврда регистрације");
        String kodiraniToken = Base64.encode(token.getBytes());
        String enkodovaniKodiraniToken = radSaGenerisanjemTokena.enkodujteUURLFormat(kodiraniToken);
        StringBuilder sb = new StringBuilder("Потврдите регистрацију кликом на следећи линк: ").append(url).append(radSaGenerisanjemTokena.enkodujteUURLFormat(email)).append("/").append(enkodovaniKodiraniToken);
        poruka.setText(sb.toString());
        mail.send(poruka);

    }
    public void napraviteDirektorijumZaSmestajSlikaKorisnika(String email){

    }
    @RequestMapping(value = "/registracijaKorisnika",method= RequestMethod.POST)
    public RezultatRegistracije registrujteKorisnika(@RequestBody LinkedHashMap<String, Object> parametri){
        String imeNaziv = (String)parametri.get("imeNaziv");
        String email= (String)parametri.get("email");
        String sifra = ((String)parametri.get("sifra"));
        String adresa= (String)parametri.get("adresa");
        String mesto = (String)parametri.get("mesto");
        String tel1 = (String)parametri.get("tel1");
        String tel2 = (String)parametri.get("tel2");
        String tel3 = (String)parametri.get("tel3");
        System.out.println(imeNaziv+" "+ email+" "+ sifra+" "+ adresa+" "+ mesto+" "+ tel1+" "+ tel2+" "+tel3);
        RezultatRegistracije rr = new RezultatRegistracije();
        //tu treba da ubacim jedan aspect
        if(sigurnosnaProvera(imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3)){
            byte[] hesiranaSifra = radSaSifrom.hesirajteSifru(sifra.getBytes());
            String token = radSaGenerisanjemTokena.generisiteTokenZaNeregistrovanogKorisnika(imeNaziv, email, adresa,tel1);
            posaljiteMejl(email, token);


            b.snimiteNovogNepotvrdjenogKorisnika(imeNaziv,email, hesiranaSifra, adresa, mesto, tel1, tel2, tel3, token);
            rr.setRegistracija(true);
        }
        else{
            //greska
            rr.setRegistracija(false);
        }
        return rr;
    }

    @RequestMapping(value = "/daLiJeEmailSlobodan",method= RequestMethod.GET)
    public String proveraEmaila(@RequestParam String email){
        if(b.daLiJeEmailSlobodanUObeTabele(email)){
            return  "true";
        }
        return "false";
    }



}
