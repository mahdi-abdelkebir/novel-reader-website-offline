import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Genre, Novel} from "../../model/novel";
import {FormControl, Validators} from "@angular/forms";
import {NovelService} from "../../service/novel-database.service";
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { ReadingService } from 'src/app/service/reading-database.service';
import { TokenStorageService } from 'src/app/service/user/token-storage.service';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';


@Component({
  selector: 'app-novel-search',
  templateUrl: './novel-search.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./novel-search.component.css']
})
export class NovelSearchComponent implements OnInit {
  novels: Novel[];
  searchText = new FormControl('', Validators.required);

  // $statusOptions : Observable<String[]>;
  $statusOptions : String[];
  selectedStatus = new FormControl('');

  $genreOptions : Genre[];
  selectedGenres = [];
  dropdownSettings:IDropdownSettings = {};

  isReadMore = {};

  isLoggedIn = false;
  adminRights = false;

  constructor(private novelService: NovelService,
              private readingService: ReadingService,
              private tokenStorage: TokenStorageService,
              private router: Router,
              private titleService: Title) {
    
    this.titleService.setTitle("Search - Webnovel Hub");
    this.isLoggedIn = !!this.tokenStorage.getToken();

    if (this.isLoggedIn) {
      this.adminRights = this.tokenStorage.getUser().roles.includes('ROLE_ADMIN');
    }

    this.$statusOptions = this.novelService.getAllStatus();
  }


  searchForNovels() {
    //title={title}&genres={genres}&status={status}&type={type}
    var genreString = ""
    this.selectedGenres.forEach(item => {
      genreString += item.genre+",";
    });

    var searchFilter = "title="+this.searchText.value+"&genres="+genreString.slice(0, -1)+"&status="+this.selectedStatus.value+"&type=";

    this.novelService.search(searchFilter).subscribe((result: Novel[]) => {
      this.novels = result;
    });
  }

  addGame(novel: Novel) {
    if (this.isLoggedIn) {
      this.readingService.addNovel(novel);
      novel.added = true;
    } else {
      this.router.navigate(['login']);
    }
  }
  
  ngOnInit() {

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'genre',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };

    this.novelService.getAllGenres().subscribe((data) => {
      this.$genreOptions = data;
    });
  }

  downloadNovel() {
    this.novelService.downloadByTitle(this.searchText.value);
  }
}
