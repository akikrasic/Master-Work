"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var zajednicka_klasa_1 = require("../../zajednicki/zajednicka-klasa");
var AdminIzvestajComponent = (function (_super) {
    __extends(AdminIzvestajComponent, _super);
    function AdminIzvestajComponent(ref, slanje, ruter, ruta) {
        var _this = _super.call(this, ruter) || this;
        _this.ref = ref;
        _this.slanje = slanje;
        _this.limit = 10;
        _this.offset = 10;
        _this.izvestaj = [];
        _this.daLiSeVecIzvrsava = false;
        _this.izvestaj = ruta.snapshot.data["adminIzvestaj"];
        _this.daLiImaJos = _this.izvestaj.length === _this.limit;
        document.onscroll = function (e) {
            _this.fjaZaEvent();
        };
        return _this;
    }
    AdminIzvestajComponent.prototype.fjaZaEvent = function () {
        if (window.scrollY >= 0.75 * window.outerHeight) {
            console.log(window.scrollY + " " + window.outerHeight);
            if (this.daLiImaJos) {
                if (this.daLiSeVecIzvrsava)
                    return;
                this.daLiSeVecIzvrsava = true;
                this.posaljite(this.slanje.uzmite("adminIzvestaj?limitS=" + this.limit + "&offsetS=" + this.offset));
            }
        }
    };
    AdminIzvestajComponent.prototype.ngOnInit = function () {
    };
    AdminIzvestajComponent.prototype.sveJeURedu = function (o) {
        var brojac = 0;
        for (var _i = 0, _a = o.izvestaj; _i < _a.length; _i++) {
            var np = _a[_i];
            this.izvestaj.push(np);
            brojac++;
        }
        this.daLiImaJos = (brojac === this.limit);
        this.offset += this.limit;
        this.daLiSeVecIzvrsava = false;
        this.ref.detectChanges();
    };
    return AdminIzvestajComponent;
}(zajednicka_klasa_1.ZajednickaKlasa));
AdminIzvestajComponent = __decorate([
    core_1.Component({
        selector: 'app-admin-izvestaj',
        templateUrl: './admin-izvestaj.component.html',
        styleUrls: ['./admin-izvestaj.component.scss']
    })
], AdminIzvestajComponent);
exports.AdminIzvestajComponent = AdminIzvestajComponent;
