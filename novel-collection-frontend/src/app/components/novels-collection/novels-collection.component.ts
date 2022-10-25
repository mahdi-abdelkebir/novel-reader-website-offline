import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {Novel} from "../../model/novel";
import { NovelService } from 'src/app/service/novel-database.service';
import { UserService } from 'src/app/service/user/user.service';
import { Reading } from 'src/app/model/user';
import { ReadingService } from 'src/app/service/reading-database.service';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-novels-collection',
  templateUrl: './novels-collection.component.html',
  styleUrls: ['./novels-collection.component.css']
})
export class NovelCollectionComponent implements OnInit {
  searchText: string;
  novels: Reading[];
  $options : String[];
  collectionLoaded;

  constructor(private router: Router, private novelService: NovelService, private readingService: ReadingService, private titleService: Title) {
    
    this.titleService.setTitle("My Collection - Webnovel Hub");

    this.$options = this.novelService.getAllReadingStatus();
    this.readingService.getAllNovels().subscribe((result: Reading[]) => {
      this.collectionLoaded = Promise.resolve(true);
      this.novels = result;
    });;
  }

  ngOnInit(): void {}

  deleteReading(reading : Reading) {
    // const novelDetails: Reading = event;
    if(confirm("Are you sure to delete "+reading.compositeKey.novel.title)) {
      this.readingService.delete(reading);
      this.novels.splice(this.novels.indexOf(reading), 1);
    }
  }

  changeReading(reading : Reading) {
    this.readingService.change(reading);
  }
}
