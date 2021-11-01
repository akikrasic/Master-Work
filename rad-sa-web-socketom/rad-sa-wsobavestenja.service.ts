import { Injectable } from '@angular/core';
import {AutentikacijaService} from "../servisi/autentikacija.service";
import {Observable} from "rxjs/Observable";
import {Observer} from "rxjs/Observer";

@Injectable()
export class RadSaWSObavestenjaService {
  private wsObavestenja:any;
  private stiglaObavestenjaObservable:Observable<any>;

  private observerObavestenja:Observer<any>;


  constructor(private aut:AutentikacijaService) {
    this.wsObavestenja= new WebSocket("wss://localhost:8443/wsObavestenja");

    this.stiglaObavestenjaObservable = new Observable<any>(observer=>{
     this.observerObavestenja=observer;
    });

    this.wsObavestenja.onmessage=(e)=> {
      //izgleda ja ovamo svaki put prvim novi observable i tu moze da bude problem
      this.observerObavestenja.next(e.data);
      // observer.complete();
      console.log(e.data);
    }
  }

  public getStiglaObavestenjaObservable():Observable<any>{
    return this.stiglaObavestenjaObservable;
  }

  public prijavaObavestenja(s){
    this.wsObavestenja.send(s);
  }

}
