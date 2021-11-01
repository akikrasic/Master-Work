import { Component, OnInit } from '@angular/core';
import {ZajednickaKlasa} from "../../zajednicki/zajednicka-klasa";
import {Router} from "@angular/router";
import {AdminSlanjeService} from "../../servisi/admin-slanje.service";
import {RadSaWSAdminRacunService} from "../../rad-sa-web-socketom/rad-sa-wsadmin-racun.service";

@Component({
  selector: 'app-admin-stanje-racuna',
  templateUrl: './admin-stanje-racuna.component.html',
  styleUrls: ['./admin-stanje-racuna.component.scss']
})
export class AdminStanjeRacunaComponent extends ZajednickaKlasa {

  racunSajta:number=0;
  racunZaPrenos:number=0;


  constructor(private slanje:AdminSlanjeService,wsServis:RadSaWSAdminRacunService, ruter:Router) {
    super(ruter);
    //zakomentarisano 29.1.2018.
    //this.posaljite(this.slanje.uzmite("adminVratiteStanjeRacuna"));

    let vr = wsServis.getPocetnoStanjeAdminRacun();
    this.racunZaPrenos= vr.racunZaPrenos;
    this.racunSajta=vr.racunSajta;

    wsServis.getStigaoAdminRacunObservable().subscribe(r=>{
      let o = JSON.parse(r);
      this.racunSajta=o.racun.racunSajta;
      this.racunZaPrenos= o.racun.racunZaPrenos;

    });
  }

  ngOnInit() {
  }
/* zakonmentarisano 29.1.2018.
  sveJeURedu(o){
    if(this.racunSajta==0){
      this.racunSajta=o.racun.racunSajta;

    }
    if(this.racunZaPrenos==0){
      this.racunZaPrenos=o.racun.racunZaPrenos;
    }
  }
*/
}
