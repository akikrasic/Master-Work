import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminPocetnaComponent } from './admin-pocetna/admin-pocetna.component';
import { AdminOdjavaComponent } from './admin-odjava/admin-odjava.component';
import {RouterModule} from "@angular/router";
import { AdminKategorijeComponentComponent } from './admin-kategorije-component/admin-kategorije-component.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { AdminStanjeRacunaComponent } from './admin-stanje-racuna/admin-stanje-racuna.component';
import {AdminStanjenaRacunuResolver} from "./admin-stanje-racuna/AdminStanjenaRacunuResolver";
import {DinarPipe} from "../svi-korisnici/pajpovi/dinar.pipe";
import {SviKorisniciModule} from "../svi-korisnici/svi-korisnici.module";
import { AdminIzvestajComponent } from './admin-izvestaj/admin-izvestaj.component';
import {AdminIzvestajResolver} from "./admin-izvestaj/AdminIzvestajResolver";

@NgModule({
  imports: [
    CommonModule, RouterModule, FormsModule, ReactiveFormsModule, SviKorisniciModule
  ],
  declarations: [AdminPocetnaComponent, AdminOdjavaComponent,  AdminKategorijeComponentComponent, AdminKategorijeComponentComponent, AdminStanjeRacunaComponent, AdminIzvestajComponent],
  exports:[AdminPocetnaComponent],
  providers:[AdminStanjenaRacunuResolver, AdminIzvestajResolver]
})
export class AdminModule { }
