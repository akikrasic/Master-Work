/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RadSaWSObavestenjaService } from './rad-sa-wsobavestenja.service';

describe('RadSaWSObavestenjaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RadSaWSObavestenjaService]
    });
  });

  it('should ...', inject([RadSaWSObavestenjaService], (service: RadSaWSObavestenjaService) => {
    expect(service).toBeTruthy();
  }));
});
