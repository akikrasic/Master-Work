/**
 * Created by aki on 11/5/17.
 */
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from "@angular/router";
import {AdminSlanjeService} from "../../servisi/admin-slanje.service";
import {ZajednickaKlasa} from "../../zajednicki/zajednicka-klasa";
@Injectable()
export class AdminStanjenaRacunuResolver{

  constructor(private slanje:AdminSlanjeService){

  }

  resolve(ruta:ActivatedRouteSnapshot, stanje:RouterStateSnapshot):Observable<any> {
    return new Observable(observer=>{
      this.slanje.uzmite("adminVratiteStanjeRacuna").subscribe(r=>{
        let o = r.json();
        observer.next(o);
        observer.complete();
      });
    });
  }
  }
