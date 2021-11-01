import { Injectable } from '@angular/core';
import {AutentikacijaService} from "../servisi/autentikacija.service";
import {Observable} from "rxjs/Observable";
import {Observer} from "rxjs/Observer";

@Injectable()
export class RadSaWSAdminRacunService {
  private wsAdminRacun:any;
  private stigaoAdminRacunObservable:Observable<any>;
  private observerAdminRacun:Observer<any>;

  private pocetnoStanjeAdminRacun;

  constructor(private aut:AutentikacijaService) {
    this.wsAdminRacun= new WebSocket("wss://localhost:8443/wsAdminRacun");
    this.stigaoAdminRacunObservable = new Observable<any>(observer=>{
      this.observerAdminRacun=observer;
    });

    this.wsAdminRacun.onmessage=(e)=> {
      //izgleda ja ovamo svaki put prvim novi observable i tu moze da bude problem
      this.observerAdminRacun.next(e.data);
      // observer.complete();
      console.log(e.data);
    }
  }

  public prijavaAdmina(){
    let o = {
      token:encodeURIComponent(this.aut.getKorisnikToken())
    };
    this.wsAdminRacun.send(JSON.stringify(o));
  }

  getStigaoAdminRacunObservable():Observable<any>{
    return this.stigaoAdminRacunObservable;
  }

  getObserverAdminRacun(){
    return this.observerAdminRacun;
  }

  getPocetnoStanjeAdminRacun(){
    return this.pocetnoStanjeAdminRacun;
  }
  setPocetnoStanjeAdminRacun(vr){
    this.pocetnoStanjeAdminRacun=vr;
  }

}
