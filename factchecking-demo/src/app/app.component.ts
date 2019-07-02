import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Fact } from './fact';

export interface Algorithm {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  
  title = 'Fact Checking based on Knowledge Graph';
  apiRoot: String = 'http://localhost:8080';
  url = `${this.apiRoot}/factchecking-service/api/execTask/`;
  subjectURI = '';
  predicateURI = '';
  objectURI = '';
  algorithm = '';
  taskId = 0;
  // trueValue: number;
  static FACTS: Fact[] = [];

  constructor(private http: HttpClient) {

  }

  algorithms: Algorithm[] = [
    {value: 'kstream', viewValue: 'Knowledge Stream'},
    {value: 'relklinker', viewValue: 'Relational Knowledge Linker'},
    {value: 'klinker', viewValue: 'Knowledge Linker'},
    {value: 'predpath', viewValue: 'Predicate Path Mining'},
    {value: 'pra', viewValue: 'Path Ranking Algorithm'},
    {value: 'katz', viewValue: 'Katz'},
    {value: 'pathent', viewValue: 'PathEnt'},
    {value: 'simrank', viewValue: 'SimRank'},
    {value: 'adamic_adar', viewValue: 'Adamic & Adar'},
    {value: 'jaccard', viewValue: 'Jaccard'},
    {value: 'degree_product', viewValue: 'Degree Product'},
    {value: 'all', viewValue: 'All of the above'}
  ];

  selectedAlgorithm(value) {
    this.algorithm = value;
  }

  /**
   * Resets all the variables value to default.
   */
  resetEverything() {
    this.subjectURI = '';
    this.predicateURI = '';
    this.objectURI = '';
    this.algorithm = '';
    console.log('All values have been reset!')
  }

  /**
   * Called when user clicks on submit button.
   */
  submitData() {
    let obj;
    obj = {'taskId': this.taskId, 'subject': this.subjectURI, 'predicate': this.predicateURI, 'object': this.objectURI, 'algorithm': this.algorithm}
    const myJSON = JSON.stringify(obj);
    this.sendToApi(myJSON);
    this.taskId++;
  }

  /**
   * Sends request to back-end
   * @param myJSON string
   */
  sendToApi(myJSON: string) {
    const promise = new Promise((resolve, reject) => {
      console.log('Send Request to: ' + this.url);
      this.http.post(this.url, myJSON)
        .toPromise()
        .then(
          res => {
            try {
              console.log(JSON.stringify(res));
              let fact: Fact = JSON.parse(JSON.stringify(res));
              console.log(fact.taskId);
              console.log(fact.subject);
              console.log(fact.results);
              AppComponent.FACTS.push(fact);
              resolve();
            } catch (e) {
              console.log('Exception: ' + e);
            }
          },
          msg => {
            console.log('rejected');
            reject(msg);
          }
        );
    });
    return promise;
  }
}
