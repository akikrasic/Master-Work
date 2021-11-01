/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RadSaWSKorisnikRacunService } from './rad-sa-wskorisnik-racun.service';

describe('RadSaWSKorisnikRacunService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RadSaWSKorisnikRacunService]
    });
  });

  it('should ...', inject([RadSaWSKorisnikRacunService], (service: RadSaWSKorisnikRacunService) => {
    expect(service).toBeTruthy();
  }));
});
