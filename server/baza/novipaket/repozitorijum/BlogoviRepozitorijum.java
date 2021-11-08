package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaBlog;
import srb.akikrasic.baza.novipaket.baza.BazaKategorija;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.Blog;
import srb.akikrasic.domen.Korisnik;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 2/20/18.
 */
@Component
public class BlogoviRepozitorijum extends RepozitorijumOsnovna<Integer, Blog> {


    @Autowired
    public void setBaza(BazaBlog b){

        super.setBazaOsnovna(b);
        this.b =b;
        query ="SELECT * FROM blog ORDER BY datum desc";
    }
    private BazaBlog b;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    private Map<String, List<Blog>> korisnikBlogoviMapa = new LinkedHashMap<>();

    private void dodajteBlogUListuKorisnika(Blog b){
        List<Blog> l = korisnikBlogoviMapa.get(b.getKorisnik().getEmail());
        if(l==null){
            l = new ArrayList<>();
            korisnikBlogoviMapa.put(b.getKorisnik().getEmail(), l);
        }
        l.add(b);
    }
    private void obrisiteBlogIzListeKorisnika(Blog b){
        List<Blog> l = korisnikBlogoviMapa.get(b.getKorisnik().getEmail());
        l.remove(b);
    }

    public void inicijalizacija(){
        super.inicijalizacija();
        mapa.values().forEach((b->{
            dodajteBlogUListuKorisnika(b);
        }));
    }

    @Override
    protected void dodajteUMapu(Blog b){
        super.dodajteUMapu(b);
        dodajteBlogUListuKorisnika(b);
    }
    @Override
    protected void obrisiteIzMapu(Blog b){
        super.obrisiteIzMapu(b);
        obrisiteBlogIzListeKorisnika(b);
    }

    public int snimiteBlog(String naslov, String tekst, Korisnik k){
        LocalDateTime datum =LocalDateTime.now();
        int id = b.snimiteBlog(naslov, tekst, k, datum);
        Blog blog = new Blog();
        blog.setId(id);
        blog.setNaslov(naslov);
        blog.setTekst(tekst);
        blog.setKorisnik(k);
        blog.setDatum(datum);
        this.dodajteUMapu(blog);
        return id;
    }
    public boolean izmeniteBlog(int id,String naslov, String tekst){
       Blog blog = this.vratiteBlog(id);
       if(blog==null){
           return false;
       }
       LocalDateTime datum = LocalDateTime.now();
       if(!b.izmeniteBlog(blog.getId(), naslov, tekst, datum)){
           return false;
       }
       blog.setDatum(datum);
       blog.setNaslov(naslov);
       blog.setTekst(tekst);
       return true;
    }


    public boolean obrisiteBlog(int id)
    {
        this.obrisiteIzMapu(this.vratiteBlog(id));
        return  b.obrisiteBlog(id);
    }

    public Blog vratiteBlog(int id){
        return mapa.get(id);
    }

    public List<Blog> blogoviKorisnika(String korisnik_email){
        return korisnikBlogoviMapa.get(korisnik_email);
    }

    @Override
    protected Blog napraviteReferencu(Map<String, Object> m) {
        Blog b = Blog.izBaze(m);
        Korisnik k = korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email"));
        b.setKorisnik(k);
        return b;
    }
    public List<Blog> vratiteBlogoveKorisnikaSaPomerajem(String email, int pocetak, int pomeraj){
        List<Blog> lista = new ArrayList<>();
        b.vratiteBlogoveKorisnikaSaPomerajem(email,pocetak, pomeraj).forEach(i->{
            lista.add(this.vratiteBlog(i));
        });
        return lista;
    }
}
