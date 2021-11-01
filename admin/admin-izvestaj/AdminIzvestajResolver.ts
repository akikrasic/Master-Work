import {AdminSlanjeService} from "../../servisi/admin-slanje.service";
import {ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";
/**
 * Created by aki on 11/18/17.
 */
@Injectable()
export class AdminIzvestajResolver{
  private limit:number=10;
  private offset:number=0;
  constructor(private slanje:AdminSlanjeService){


  }
  resolve(ruta:ActivatedRouteSnapshot, state:RouterStateSnapshot):Observable<any>{
    return new Observable<any>(observer=>{
      this.slanje.uzmite("adminIzvestaj?limitS="+this.limit+"&offsetS="+this.offset).subscribe(r=>{
        let o = r.json();
        observer.next(o.izvestaj);
        observer.complete();
      });
    });
  }


}
