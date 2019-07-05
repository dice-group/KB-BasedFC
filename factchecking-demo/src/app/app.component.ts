import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Fact } from './fact';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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
  
  public factForm: FormGroup;

  title = 'Fact Checking based on Knowledge Graph';
  apiRoot: String = 'http://localhost:8080';
  url = `${this.apiRoot}/factchecking-service/api/execTask/`;
  taskId = 0;
  static FACTS: Fact[] = [];

  constructor(private http: HttpClient) {
    this.createForm();
  }

  /**
   * Does form validation
   */
  createForm() {
    const reg = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';
    this.factForm = new FormGroup({
      subjectURI: new FormControl('', [Validators.pattern(reg)]),
      predicateURI: new FormControl('', [Validators.pattern(reg)]),
      objectURI: new FormControl('', [Validators.pattern(reg)]),
      selectedAlgorithm: new FormControl('kstream', [])
    });
  }

  algorithms: Algorithm[] = [
    {value: 'kstream', viewValue: 'Knowledge Stream (default)'},
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

  /**
   * Creates a fact using form values
   */
  public createFact = (factFormValue) => {
    if (this.factForm.valid) {
      let fact: Fact = {
        taskId: this.taskId++,
        subject: factFormValue.subjectURI,
        predicate: factFormValue.predicateURI,
        object: factFormValue.objectURI,
        algorithm: factFormValue.selectedAlgorithm
      }
      this.submitData(fact);
    }
  }

  /**
   * Resets all the variables value to default.
   */
  resetEverything() {
    this.factForm.setValue({subjectURI: "", predicateURI: "", objectURI: "", selectedAlgorithm: ""});
    console.log('All values have been reset!')
  }

  /**
   * Called when user clicks on submit button.
   */
  submitData(fact: Fact) {
    const factJSON = JSON.stringify(fact);
    console.log('FACT: ' + factJSON);
    this.sendToApi(factJSON);
  }

  /**
   * Sends request to back-end
   * @param factJSON string
   */
  sendToApi(factJSON: string) {
    const promise = new Promise((resolve, reject) => {
      console.log('Request sent to: ' + this.url);
      this.http.post(this.url, factJSON)
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
