import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ZajednickaKlasa} from "../../zajednicki/zajednicka-klasa";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminSlanjeService} from "../../servisi/admin-slanje.service";

@Component({
  selector: 'app-admin-izvestaj',
  templateUrl: './admin-izvestaj.component.html',
  styleUrls: ['./admin-izvestaj.component.scss']
})
export class AdminIzvestajComponent extends ZajednickaKlasa {

  limit:number=10;
  offset:number=10;
  izvestaj:any=[];
  daLiImaJos;
  constructor(private ref: ChangeDetectorRef,private slanje:AdminSlanjeService,ruter:Router, ruta:ActivatedRoute) {
    super(ruter);
    this.izvestaj = ruta.snapshot.data["adminIzvestaj"];
    this.daLiImaJos=this.izvestaj.length===this.limit;
    document.onscroll=(e)=>{
      this.fjaZaEvent();

    }
    }

    fjaZaEvent(){
      if(window.scrollY>= 0.75*window.outerHeight){
        console.log(window.scrollY+" "+window.outerHeight);
        if(this.daLiImaJos){
          if(this.daLiSeVecIzvrsava)return;
          this.daLiSeVecIzvrsava=true;
          this.posaljite(this.slanje.uzmite("adminIzvestaj?limitS="+this.limit+"&offsetS="+this.offset));
        }
      }
    }

  ngOnInit() {
  }
  daLiSeVecIzvrsava= false;

  sveJeURedu(o){
    let brojac=0;
    for(let np of o.izvestaj){
      this.izvestaj.push(np);
      brojac++;
    }
    this.daLiImaJos=(brojac===this.limit);
    this.offset+=this.limit;
    this.daLiSeVecIzvrsava=false;
    this.ref.detectChanges();
  }

}
