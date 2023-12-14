import {bootstrapApplication} from '@angular/platform-browser';
import {homeConfig} from './app/component/home/home.config';
import {HomeComponent} from './app/component/home/home.component';

bootstrapApplication(HomeComponent, homeConfig)
.catch((err) => console.error(err));
