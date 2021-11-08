package srb.akikrasic.zahtevi.novi.osnovne;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.token.GenerisanjeTokena;

/**
 * Created by aki on 12/20/17.
 */
public class ZahtevOsnovna<T extends RezultatPrivilegovanogKorisnika> {

    @Autowired
    protected GenerisanjeTokena radSaGenerisanjemTokena;

    @Autowired
    protected KonverzijaPodataka konverzija;

    protected T rezZaPostavljanjeTrueIliFalse;
    protected T rezNeovlasceniPristup;

    protected T rezSveJeURedu;

    protected T rezGreska ;


    public T dosloJeDoNeovlascenogPristupa(){
        return rezNeovlasceniPristup;
    }

    public T greska(){
       return rezGreska;
    }

    public T sveJeURedu(){

        return rezSveJeURedu;
    }

    public synchronized T postaviteSvejeURedu(boolean vr){
        //to bas i nije thread safe, bolje da vracam nepromenljivi objekti
        if(vr){
            return rezSveJeURedu;
        }
        else{
            return rezGreska;
        }
    }


    protected String emailIzAutorizacije(String autorizacija) {
        return (radSaGenerisanjemTokena.dekodujteURLFormat(autorizacija)).split(":")[0];
    }


    protected String dinarIliDinara(double broj) {
        if (broj % 10 == 1) return "динар";
        return "динара";
    }

    protected boolean pocetakPomerajuRedu(int pocetak, int pomeraj) {
        if (pocetak > -1 && pomeraj > -1 ) return true;
        return false;

    }

    protected void postaviteVrednostiNaPocetne(){
        rezNeovlasceniPristup.dosloJeDoNeovlascenogPristupa();
        rezSveJeURedu.setDaLiJeURedu(true);
        rezGreska.setDaLiJeURedu(false);
    }
}
