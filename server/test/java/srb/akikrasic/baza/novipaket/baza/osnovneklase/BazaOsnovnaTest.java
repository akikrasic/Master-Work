package srb.akikrasic.baza.novipaket.baza.osnovneklase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
public class BazaOsnovnaTest {
    private int proizvodId=36;

    private BazaOsnovna b = new BazaOsnovna();
    @Test
    public void izvrsiteQuery() throws Exception {
        b.izvrsiteQuery("SELECT * from proizvod WHERE id=?", proizvodId).forEach(m->{

            assertTrue(((Integer)m.get("id"))== proizvodId);
        });
    }


}