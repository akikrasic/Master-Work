package srb.akikrasic.token;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by aki on 9/13/17.
 */
@Component
public class ProizvodSmeDaSeMenja {
    private LinkedHashSet<Integer> proizvodiUNarudzbinama;

    public ProizvodSmeDaSeMenja(){
        proizvodiUNarudzbinama= new LinkedHashSet<>();
    }

    public void inicijalizacija(List<Integer> lista){
        lista.forEach(i->{
            proizvodiUNarudzbinama.add(i);
        });
    }

    public boolean smeDaSeMenja(int proizvodId){
        //ako se nalazi ne sme da se menja, a ko se ne nalazi, sme da se menja, zato je not
        return !proizvodiUNarudzbinama.contains(proizvodId);
    }
}
