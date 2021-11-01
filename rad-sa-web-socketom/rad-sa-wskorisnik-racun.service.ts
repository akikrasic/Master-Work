import { Injectable } from '@angular/core';
import {AutentikacijaService} from "../servisi/autentikacija.service";
import {Observable} from "rxjs/Observable";
import {Observer} from "rxjs/Observer";

@Injectable()
export class RadSaWSKorisnikRacunService {
  private wsKorisnikRacun;
  private stigaoKorisnikRacunObservable:Observable<any>;
  private observerKorisnikRacun:Observer<any>;

  private pocetnoStanjeKorisnikRacun;

  constructor(private aut:AutentikacijaService) {
    this.wsKorisnikRacun = new WebSocket("wss://localhost:8443/wsKorisnikRacun");

    this.stigaoKorisnikRacunObservable = new Observable<any>(observer=>{
     this.observerKorisnikRacun=observer;
    });

    this.wsKorisnikRacun.onmessage=(e)=> {
      //izgleda ja ovamo svaki put prvim novi observable i tu moze da bude problem
      this.observerKorisnikRacun.next(e.data);
      // observer.complete();
      console.log(e.data);
    }
  }

  public prijavaKorisnikRacun(s){
    this.wsKorisnikRacun.send(s);

  }
  getStigaoKorisnikRacunObservable(){
    return this.stigaoKorisnikRacunObservable;
  }
  getObserverKorisnikRacun(){
    return this.observerKorisnikRacun;
  }

  getPocetnoStanjeKorisnikRacun(){
    return this.pocetnoStanjeKorisnikRacun;
  }
  setPocetnoStanjeKorisnikRacun(vr){
    this.pocetnoStanjeKorisnikRacun=vr;
  }

}
