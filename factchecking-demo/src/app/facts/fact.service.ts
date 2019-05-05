import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Injectable } from '@angular/core';
import { catchError} from "rxjs/operators";
import { IFact } from "./fact";

@Injectable({
    providedIn: 'root'
})

export class FactService {

    private backendUrl = 'http://localhost:8081/Web_Server/s_p_o';

    constructor(private http:HttpClient) {

    }
  
    addFact(fact: IFact): Observable<IFact> {
        return this.http.post<IFact>(this.backendUrl, fact).pipe(
            catchError(this.handleError)
        );
    }

    private handleError(err: HttpErrorResponse) {
        let errorMessage = '';  // variable with function scope
        if(err.error instanceof ErrorEvent)
            errorMessage = `An error occured: ${err.error.message}`;
        else
            errorMessage = `Server returned a code: ${err.status}, error message is: ${err.message}`;
        console.log(errorMessage);
        return throwError(errorMessage);
    }
}