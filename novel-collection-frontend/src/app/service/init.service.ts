import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Genre } from '../model/novel';
import {NovelService} from "./novel-database.service";

@Injectable({
  providedIn: 'root'
})
export class InitService {

  public genreOptions : Observable<Genre[]>;
  public statusOptions : String[];

  constructor(private novelService: NovelService) { }

  init() {
    alert("init");
    this.genreOptions = this.novelService.getAllGenres();
    this.statusOptions = this.novelService.getAllStatus();
  }
}