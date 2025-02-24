import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { StringLiteralType } from 'typescript';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private httpClient = inject(HttpClient);

  getUserId(): Observable<string>{ 
    return this.httpClient.get<string>(`/api/user/id`);
  }

}
