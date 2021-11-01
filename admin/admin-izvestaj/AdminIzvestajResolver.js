"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("rxjs/Observable");
var core_1 = require("@angular/core");
/**
 * Created by aki on 11/18/17.
 */
var AdminIzvestajResolver = (function () {
    function AdminIzvestajResolver(slanje) {
        this.slanje = slanje;
        this.limit = 10;
        this.offset = 0;
    }
    AdminIzvestajResolver.prototype.resolve = function (ruta, state) {
        var _this = this;
        return new Observable_1.Observable(function (observer) {
            _this.slanje.uzmite("adminIzvestaj?limitS=" + _this.limit + "&offsetS=" + _this.offset).subscribe(function (r) {
                var o = r.json();
                observer.next(o.izvestaj);
                observer.complete();
            });
        });
    };
    return AdminIzvestajResolver;
}());
AdminIzvestajResolver = __decorate([
    core_1.Injectable()
], AdminIzvestajResolver);
exports.AdminIzvestajResolver = AdminIzvestajResolver;
