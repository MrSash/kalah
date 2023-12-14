import {Injectable} from '@angular/core';
import {kalahApiEnv} from "../enivronment/kalah-api-env";

@Injectable({
  providedIn: 'root'
})
export class KalahApiService {

  async sendStubRequest(): Promise<Response> {
    return fetch(`${kalahApiEnv.url}/${kalahApiEnv.path.players}/stub`)
  }
}