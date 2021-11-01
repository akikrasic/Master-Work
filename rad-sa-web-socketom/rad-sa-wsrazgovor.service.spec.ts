/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RadSaWSRazgovorService } from './rad-sa-wsrazgovor.service';

describe('RadSaWSRazgovorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RadSaWSRazgovorService]
    });
  });

  it('should ...', inject([RadSaWSRazgovorService], (service: RadSaWSRazgovorService) => {
    expect(service).toBeTruthy();
  }));
});
