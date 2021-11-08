package srb.akikrasic.zahtevi.novi.sigurnost;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by aki on 12/22/17.
 */
@Component
public class SigurnostProizvod {
    public boolean sigurnosnProveraNazivaProizvoda(String nazivProizvoda) {
        if (nazivProizvoda == null || nazivProizvoda.equals("")) return false;
        return true;
    }

    public boolean sigurnosnProveraOpisaProizvoda(String opisProizvoda) {
        if (opisProizvoda == null || opisProizvoda.equals("")) return false;
        return true;
    }

    public boolean sigurnosnaProveraKategorije(String idKategorije) {
        if (idKategorije == null || idKategorije.equals("")) return false;
        return /*!b.daLiSeNazivKategorijeNalaziUBazi(nazivKategorije);*/true;
    }

    public boolean sigurnosnaProveraCene(String cenaString) {
        if (cenaString == null || cenaString.equals("")) return false;
        return true;
    }


    public boolean sigurnosnaProveraUnosaProizvoda(String nazivProizvoda, String opisProizvoda, String cenaProizvoda, String idKategorije) {
        if (!this.sigurnosnProveraNazivaProizvoda(nazivProizvoda)) return false;
        if (!this.sigurnosnProveraOpisaProizvoda(opisProizvoda)) return false;
        if (!this.sigurnosnaProveraCene(cenaProizvoda)) return false;
        return this.sigurnosnaProveraKategorije(idKategorije);
    }


    public boolean sigurnosnaProveraListeKljucnihReci(List<String> lista) {
        if (lista == null || lista.isEmpty()) return false;
        for (String s : lista) {
            if (s == null || s.equals("")) return false;
        }
        return true;
    }



}
