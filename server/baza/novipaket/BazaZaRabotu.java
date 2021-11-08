package srb.akikrasic.baza.novipaket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;

import javax.annotation.PreDestroy;

/**
 * Created by aki on 11/24/17.
 */
public class BazaZaRabotu {

    /**
     * Created by aki on 7/28/17.
     */
    @Component
    public class Baza {
        private JdbcTemplate b;
        @Autowired
        private Sifra radSaSifrom;
        @Autowired
        private Tokeni radSaTokenima;
        @Autowired
        private GenerisanjeTokena radSaGenerisanjemTokena;









        public Baza() {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl("jdbc:postgresql://localhost:5432/prodavnica");
            ds.setUsername("postgres");
            ds.setPassword("novasifra");
            b = new JdbcTemplate(ds);
        }
























        @PreDestroy
        public void kraj() {// nema tu nista da se upisuje samo u slika predestroy
        }














        //novi api za komentare i ocene




        public JdbcTemplate getB() {
            return b;
        }

        public void setB(JdbcTemplate b) {
            this.b = b;
        }

        public Sifra getRadSaSifrom() {
            return radSaSifrom;
        }

        public void setRadSaSifrom(Sifra radSaSifrom) {
            this.radSaSifrom = radSaSifrom;
        }

        public Tokeni getRadSaTokenima() {
            return radSaTokenima;
        }

        public void setRadSaTokenima(Tokeni radSaTokenima) {
            this.radSaTokenima = radSaTokenima;
        }

        public GenerisanjeTokena getRadSaGenerisanjemTokena() {
            return radSaGenerisanjemTokena;
        }

        public void setRadSaGenerisanjemTokena(GenerisanjeTokena radSaGenerisanjemTokena) {
            this.radSaGenerisanjemTokena = radSaGenerisanjemTokena;
        }








    }

}
