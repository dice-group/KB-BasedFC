import { OnInit, Component } from '@angular/core';
import { Fact } from '../fact';
import { AppComponent } from '../app.component';

@Component({
    selector: 'app-results',
    templateUrl: './results.component.html',
    styleUrls: ['./results.component.css']
})

export class ResultsComponent implements OnInit {
 
    facts = AppComponent.FACTS;
    selectedFact: Fact;

    ngOnInit() {

    }

    onSelect(fact: Fact): void {
        this.selectedFact = fact;
    } 
}