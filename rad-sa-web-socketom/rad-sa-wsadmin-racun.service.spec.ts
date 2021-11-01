/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RadSaWSAdminRacunService } from './rad-sa-wsadmin-racun.service';

describe('RadSaWSAdminRacunService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RadSaWSAdminRacunService]
    });
  });

  it('should ...', inject([RadSaWSAdminRacunService], (service: RadSaWSAdminRacunService) => {
    expect(service).toBeTruthy();
  }));
});
