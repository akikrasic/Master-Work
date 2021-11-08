package srb.akikrasic.niti;

import java.util.concurrent.TimeUnit;

/**
 * Created by aki on 11/3/17.
 */
public abstract class PeriodicniPosao implements Runnable {
    protected int brojMinutaZaPonavljanjePeriodicnogPosla;

    public PeriodicniPosao(int brojMinutaZaPonavljanjePeriodicnogPosla){
        this.brojMinutaZaPonavljanjePeriodicnogPosla=brojMinutaZaPonavljanjePeriodicnogPosla;
    }
    public void run(){
        while(true) {
            izvrsavanjeZadatka();
            try {
                TimeUnit.MINUTES.sleep(brojMinutaZaPonavljanjePeriodicnogPosla);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public abstract void izvrsavanjeZadatka();
}
