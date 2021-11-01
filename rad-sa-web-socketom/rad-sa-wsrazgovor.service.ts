import { Injectable } from '@angular/core';
import {AutentikacijaService} from "../servisi/autentikacija.service";
import {Observable} from "rxjs/Observable";
import {Observer} from "rxjs/Observer";

@Injectable()
export class RadSaWSRazgovorService {

  private wsRazgovor;
  private stigaoRazgovorObservable:Observable<any>;
  private observerRazgovori:Observer<any>;


  constructor(private aut:AutentikacijaService) {
    this.wsRazgovor = new WebSocket("wss://localhost:8443/wsRazgovor");

    this.stigaoRazgovorObservable = new Observable<any>(observer=>{
     this.observerRazgovori=observer;
    });

    this.wsRazgovor.onmessage=(e)=> {
      //izgleda ja ovamo svaki put prvim novi observable i tu moze da bude problem
      this.observerRazgovori.next(e.data);
      // observer.complete();
      console.log(e.data);
    }
  }

  prijavaRazgovora(s){
    this.wsRazgovor.send(s);
  }
  getStigaoRazgovorObservable(): Observable<any> {
    return this.stigaoRazgovorObservable;
  }


  public getWSRazgovor():any{
    return this.wsRazgovor;
  }


}
