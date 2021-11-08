package srb.akikrasic.baza.novipaket.baza.nesredjeni;

import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;

/**
 * Created by aki on 11/26/17.
 *///stari api prevazidjeno, nema sta da se dira
public class BazaOcena extends BazaOsnovna {
/*
    public void izracunajteProsecnuOcenu(int proizvodId){
        List<Map<String, Object>> rez = b.queryForList("SELECT avg(ocena) as prosek FROM ocena WHERE proizvod_id=?", proizvodId);
        if(rez.isEmpty())return;
        BigDecimal prosek = (BigDecimal)rez.get(0).get("prosek");
        b.update("UPDATE proizvod SET prosecna_ocena=? WHERE id=?", prosek, proizvodId);
    }
    public boolean daLiKorisnikSmeDaOceniProizvod(String korisnikEmail, int proizvodId){  //nije dobro!!!
        List<Map<String, Object>> rez = b.queryForList("SELECT narudzbenica.id FROM narudzbenica inner join naruceniproizvod on narudzbenica.id=naruceniproizvod.narudzbenica_id WHERE narudzbenica.korisnik_email=?", korisnikEmail);
        return  !rez.isEmpty();
    }

    public boolean snimiteOcenu(int ocena, String korisnikEmail, int proizvodId) {
        return b.update("INSERT INTO ocena(ocena, korisnik_email, proizvod_id) VALUES(?,?,?)", ocena, korisnikEmail, proizvodId)==1;

    }
    public void snimiteOcenuSaRacunanjemProseka(int ocena, String korisnikEmail, int proizvodId){
        snimiteOcenu(ocena, korisnikEmail, proizvodId);
        izracunajteProsecnuOcenu(proizvodId);
    }
    public boolean izmeniteOcenu(int ocena, String korisnikEmail, int proizvodId){
        return b.update("UPDATE ocena SET ocena=? WHERE korisnik_email=? AND proizvod_id=?",ocena, korisnikEmail, proizvodId )==1;
    }
    public boolean izmeniteOcenuSaRacunanjemProseka(int ocena, String korisnikEmail, int proizvodId){
        boolean rez = this.izmeniteOcenu(ocena, korisnikEmail, proizvodId);
        this.izracunajteProsecnuOcenu(proizvodId);
        return rez;
    }
    public boolean obrisiteOcenu(String korisnikEmail, int proizvodId){
        //eee a prosek Bajo moj
        if( b.update("DELETE FROM ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId )==1){
            //sad se racuna prosek
            this.izracunajteProsecnuOcenu(proizvodId);//moze i kveri
            //model  UPDATE proizvod SET prosecna_ocena=vrednost.vrednost  FROM (SELECT AVG(ocena.ocena) as vrednost FROM ocena WHERE proizvod_id=36) as  kveri  WHERE proizvod.id=36;
        }
        else{
            return false;
        }
        return true;

    }

    public Ocena vratiteOcenu(String korisnikEmail, int proizvodId){
        List<Map<String, Object>> l = b.queryForList("SELECT * FROM ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId);
        if(l.isEmpty())return null;
        Map<String, Object> m = l.get(0);
        Ocena o = Ocena.izBaze(m);
        o.setKorisnik(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        o.setProizvod(this.vratiteProizvodPoId((Integer)m.get("proizvod_id")));
        return o;
    }


    public List<Ocena> vratiteSveOceneZaProizvod(int proizvodId){
        List<Ocena> ocene = new ArrayList<>();
        List<Map<String, Object>> oc = b.queryForList("SELECT * FROM ocena WHERE proizvod_id=?", proizvodId);
        oc.forEach(m->{
            Ocena o = Ocena.izBaze(m);
            o.setKorisnik(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
            ocene.add(o);
        });
        return ocene;
    }

*/
}
