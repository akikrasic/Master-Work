package srb.akikrasic.zahtevi.novi.osnovne;

/**
 * Created by aki on 8/19/17.
 */
public class RezultatPrivilegovanogKorisnika {
    private boolean daLiJeURedu;
    private boolean neovlasceniPristup;

    public RezultatPrivilegovanogKorisnika(){
        //daLiJeURedu=true; to treba da sredimo
    }

    public boolean isDaLiJeURedu() {
        return daLiJeURedu;
    }

    public void setDaLiJeURedu(boolean daLiJeURedu) {
        this.daLiJeURedu = daLiJeURedu;
    }

    public boolean isNeovlasceniPristup() {
        return neovlasceniPristup;
    }

    public void setNeovlasceniPristup(boolean neovlasceniPristup) {
        this.neovlasceniPristup = neovlasceniPristup;
    }

    public void dosloJeDoNeovlascenogPristupa(){
        this.daLiJeURedu=false;
        this.neovlasceniPristup=true;
    }

}
