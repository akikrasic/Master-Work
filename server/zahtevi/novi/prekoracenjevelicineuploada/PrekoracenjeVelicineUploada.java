package srb.akikrasic.zahtevi.novi.prekoracenjevelicineuploada;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.unosslikeprofila.RezultatPostavljanjaSlikeProfila;

/**
 * Created by aki on 8/28/17.
 */
@ControllerAdvice
public class PrekoracenjeVelicineUploada extends ResponseEntityExceptionHandler {
    //za sad se iskljucuje posto ce za svaku mogucu gresku, jer nasledjuje Exception klasu, da baca ovo, sto u sustini i treba da radi,
    //ali posto se jos uvek aplikacija izradjuje bice iskljuceno, kad se aplikacija pusti u rad, onda ce biti ukljuceno, kad se zavrsi sa razvojem aplikacije

    //@ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        System.out.println("kao radi");
        RezultatPostavljanjaSlikeProfila rpsp = new RezultatPostavljanjaSlikeProfila();
        rpsp.setDaLiJeURedu(false);
        return handleExceptionInternal(ex, rpsp,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


}
