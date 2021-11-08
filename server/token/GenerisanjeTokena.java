package srb.akikrasic.token;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by aki on 8/12/17.
 */
@Component
public class GenerisanjeTokena {
    private Random random = new Random(47);
    private String[] selaPirotskogOkruga = {"Гњилан","Барје Чифлик","Расница","Сиња Глава","Пасјач","Блато","Костур","Велики Суводол","Мали Суводол","Понор","Присјан","Камик","Држина","Петровац","Војнеговац","Суково","Јалботина","Горња Држина","Власи","Бело Поље","Чиниглавци","Градиште","Срећковац","Планиница","Крупац","Велико Село","Велики Јовановац","Мали Јовановац","Трњана","Пољска Ржана","Бериловац","Извор","Градашница","Нишор","Добри До","Покровеник","Копривштица","Гостуша","Паклештица","Велика Лукања","Бела","Височка Ржана","Славиња","Росомач","Рсовци","Јеловица","Дојкинци","Брлог","Темска","Куманово","Рудиње","Рагодеш","Топли До","Засковци","Церова","Шугрин","Мирковци","Базовик","Орља","Црноклиште","Осмаково","Враниште","Станичење","Сопот","Ореовица","Црвенчево","Обреновац","Милојковац","Басара","Беровица","Церев Дел",};
    private MessageDigest md;
    public GenerisanjeTokena(){
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
    public String generisiteNoviAdminToken(){

        long vreme = System.nanoTime();
        int slucajniBroj= random.nextInt(71);
        String priprema = new StringBuilder().append(vreme).append("Непромењиви део стринга пошто је време подложно сталној промени").append(selaPirotskogOkruga[slucajniBroj]).toString();
        return new String(md.digest(priprema.getBytes()));
    }
    public String generisiteNoviKorisnikToken(String imeNaziv,String email, String adresa,String mesto, String tel1 ){
        int k = random.nextInt(71);
        StringBuilder sb= new StringBuilder().append(imeNaziv).append(System.nanoTime()).append(mesto).append(email).append(adresa).append(this.selaPirotskogOkruga[k]).append(tel1);
        return new String(md.digest(sb.toString().getBytes()));
    }

    public String generisiteTokenZaNeregistrovanogKorisnika(String imeNaziv, String email, String adresa,String tel1){
        int k = random.nextInt(71);
        StringBuilder sb= new StringBuilder().append(imeNaziv).append(System.nanoTime()).append(email).append(adresa).append(this.selaPirotskogOkruga[k]).append(tel1);
        return new String(md.digest(sb.toString().getBytes()));
    }
    public String enkodujteUURLFormat(String url){
        String enkodovan = null;
        try {
            enkodovan = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enkodovan;
    }
    public String dekodujteURLFormat(String kodiran){
        String dekodiran = null;
        try {
            dekodiran =  URLDecoder.decode(kodiran, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dekodiran;
    }

    public String generisiteTokenZaPromenuSifre(String imeNaziv, String email, byte[] sifra, long timestamp){
        int k = random.nextInt(71);
        String s = new StringBuilder(imeNaziv).append(selaPirotskogOkruga[k]).append(email).append(sifra).append(email).toString();
        return new String(md.digest(s.getBytes()));
    }
}
