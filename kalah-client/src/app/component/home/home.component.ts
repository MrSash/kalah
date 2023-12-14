import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {KalahApiService} from "../../service/kalah-api.service";

@Component({
  selector: 'home-component',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './home.component.html'
})
export class HomeComponent {

  constructor(private kalahApiService: KalahApiService) {
  }

  sendStubRequest() {
    this.kalahApiService.sendStubRequest().then(value => {console.log(value.ok)})
  }
}
