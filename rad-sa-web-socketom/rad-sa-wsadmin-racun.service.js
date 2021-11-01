"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var Observable_1 = require("rxjs/Observable");
var RadSaWSAdminRacunService = (function () {
    function RadSaWSAdminRacunService(aut) {
        var _this = this;
        this.aut = aut;
        this.wsAdminRacun = new WebSocket("wss://localhost:8443/wsAdminRacun");
        this.stigaoAdminRacunObservable = new Observable_1.Observable(function (observer) {
            _this.observerAdminRacun = observer;
        });
        this.wsAdminRacun.onmessage = function (e) {
            //izgleda ja ovamo svaki put prvim novi observable i tu moze da bude problem
            _this.observerAdminRacun.next(e.data);
            // observer.complete();
            console.log(e.data);
        };
    }
    RadSaWSAdminRacunService.prototype.prijavaAdmina = function () {
        var o = {
            token: encodeURIComponent(this.aut.getKorisnikToken())
        };
        this.wsAdminRacun.send(JSON.stringify(o));
    };
    RadSaWSAdminRacunService.prototype.getStigaoAdminRacunObservable = function () {
        return this.stigaoAdminRacunObservable;
    };
    RadSaWSAdminRacunService.prototype.getObserverAdminRacun = function () {
        return this.observerAdminRacun;
    };
    RadSaWSAdminRacunService.prototype.getPocetnoStanjeAdminRacun = function () {
        return this.pocetnoStanjeAdminRacun;
    };
    RadSaWSAdminRacunService.prototype.setPocetnoStanjeAdminRacun = function (vr) {
        this.pocetnoStanjeAdminRacun = vr;
    };
    return RadSaWSAdminRacunService;
}());
RadSaWSAdminRacunService = __decorate([
    core_1.Injectable()
], RadSaWSAdminRacunService);
exports.RadSaWSAdminRacunService = RadSaWSAdminRacunService;
