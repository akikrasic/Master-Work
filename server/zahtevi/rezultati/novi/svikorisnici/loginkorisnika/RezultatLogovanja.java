package srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika;

/**
 * Created by aki on 8/7/17.
 */
public class RezultatLogovanja {
    private boolean uspesno;
    private String razlogNeuspesnogLogovanja;
    private String token;
    private String email;
    private double stanjeRacuna;

    public boolean isUspesno() {
        return uspesno;
    }

    public void setUspesno(boolean uspesno) {
        this.uspesno = uspesno;
    }

    public String getRazlogNeuspesnogLogovanja() {
        return razlogNeuspesnogLogovanja;
    }

    public void setRazlogNeuspesnogLogovanja(String razlogNeuspesnogLogovanja) {
        this.razlogNeuspesnogLogovanja = razlogNeuspesnogLogovanja;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getStanjeRacuna() {
        return stanjeRacuna;
    }

    public void setStanjeRacuna(double stanjeRacuna) {
        this.stanjeRacuna = stanjeRacuna;
    }
}