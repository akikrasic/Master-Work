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
var RadSaWSRazgovorService = (function () {
    function RadSaWSRazgovorService(aut) {
        var _this = this;
        this.aut = aut;
        this.wsRazgovor = new WebSocket("wss://localhost:8443/wsRazgovor");
        this.stigaoRazgovorObservable = new Observable_1.Observable(function (observer) {
            _this.observerRazgovori = observer;
        });
        this.wsRazgovor.onmessage = function (e) {
            //izgleda ja ovamo svaki put prvim novi observable i tu moze da bude problem
            _this.observerRazgovori.next(e.data);
            // observer.complete();
            console.log(e.data);
        };
    }
    RadSaWSRazgovorService.prototype.prijavaRazgovora = function (s) {
        this.wsRazgovor.send(s);
    };
    RadSaWSRazgovorService.prototype.getStigaoRazgovorObservable = function () {
        return this.stigaoRazgovorObservable;
    };
    RadSaWSRazgovorService.prototype.getWSRazgovor = function () {
        return this.wsRazgovor;
    };
    return RadSaWSRazgovorService;
}());
RadSaWSRazgovorService = __decorate([
    core_1.Injectable()
], RadSaWSRazgovorService);
exports.RadSaWSRazgovorService = RadSaWSRazgovorService;
