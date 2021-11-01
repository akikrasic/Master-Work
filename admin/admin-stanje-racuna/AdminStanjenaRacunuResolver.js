"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
/**
 * Created by aki on 11/5/17.
 */
var core_1 = require("@angular/core");
var Observable_1 = require("rxjs/Observable");
var AdminStanjenaRacunuResolver = (function () {
    function AdminStanjenaRacunuResolver(slanje) {
        this.slanje = slanje;
    }
    AdminStanjenaRacunuResolver.prototype.resolve = function (ruta, stanje) {
        var _this = this;
        return new Observable_1.Observable(function (observer) {
            _this.slanje.uzmite("adminVratiteStanjeRacuna").subscribe(function (r) {
                var o = r.json();
                observer.next(o);
                observer.complete();
            });
        });
    };
    return AdminStanjenaRacunuResolver;
}());
AdminStanjenaRacunuResolver = __decorate([
    core_1.Injectable()
], AdminStanjenaRacunuResolver);
exports.AdminStanjenaRacunuResolver = AdminStanjenaRacunuResolver;
