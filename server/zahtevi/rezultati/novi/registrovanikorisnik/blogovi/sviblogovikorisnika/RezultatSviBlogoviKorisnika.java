package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.blogovi.sviblogovikorisnika;

import srb.akikrasic.domen.Blog;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 2/21/18.
 */
public class RezultatSviBlogoviKorisnika extends RezultatRegistrovaniKorisnik {

    private List<Blog> blogovi;

    public List<Blog> getBlogovi() {
        return blogovi;
    }

    public void setBlogovi(List<Blog> blogovi) {
        this.blogovi = blogovi;
    }
}
