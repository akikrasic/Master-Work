package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Blog;
import srb.akikrasic.domen.Korisnik;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 2/21/18.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BlogoviRepozitorijumTest {
    @Autowired
    private BlogoviRepozitorijum blogoviRepozitorijum;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    private String email = "email@testPotvrdePotrda";

    @Test
    public void jedanVelikiTest(){
        String tekst="Текст блога за тест" ;
        String naslov="Наслов блога за тест";
        int id = blogoviRepozitorijum.snimiteBlog(naslov, tekst , korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email));
        assertTrue(id>0);
        List<Blog> blogovi = blogoviRepozitorijum.vratiteBlogoveKorisnikaSaPomerajem(email, 0, 10);
        Blog b = blogovi.get(0);
        assertTrue(blogovi.size()==1);
        assertTrue(blogovi.get(0).getId()==id);
        assertTrue(b.getNaslov().equals(naslov));
        assertTrue(b.getTekst().equals(tekst));

        String naslovIzmenjen="Измењен наслов";
        String tekstIzmenjen="Измењен текст";
        assertTrue(blogoviRepozitorijum.izmeniteBlog(b.getId(),naslovIzmenjen, tekstIzmenjen));

        assertFalse(blogoviRepozitorijum.izmeniteBlog(-1, "", ""));

        b = blogoviRepozitorijum.vratiteBlog(b.getId());
        assertTrue(b.getTekst().equals(tekstIzmenjen));
        assertTrue(b.getNaslov().equals(naslovIzmenjen));

        blogoviRepozitorijum.obrisiteBlog(id);
        blogovi = blogoviRepozitorijum.vratiteBlogoveKorisnikaSaPomerajem(email, 0, 10);
        assertTrue(blogovi.isEmpty());
        assertNull(blogoviRepozitorijum.vratiteBlog(id));
    }
}