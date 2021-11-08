package srb.akikrasic.baza.novipaket.baza.osnovneklase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

/**
 * Created by aki on 11/24/17.
 */
@Component
public class BazaReferenca extends JdbcTemplate {
    private static  DriverManagerDataSource ds;
    static{
        ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/prodavnica");
        ds.setUsername("postgres");
        ds.setPassword("novasifra");
    }
    public BazaReferenca() {
        super(ds);
    }
}
