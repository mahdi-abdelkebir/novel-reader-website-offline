import { Component, OnInit } from '@angular/core';
import { Bookmark } from 'src/app/model/user';
import { ReadingService } from 'src/app/service/reading-database.service';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';

@Component({
  selector: 'app-bookmark-list',
  templateUrl: './bookmark-list.component.html',
  styleUrls: ['./bookmark-list.component.css']
})
export class BookmarkListComponent implements OnInit {
  searchText: string;
  searchTags: string[] = [];
  bookmarks: Bookmark[];
  collectionLoaded;
  searching : boolean = false;

  constructor(private readingService: ReadingService, private pageTitleService: Title, private router : Router) {
    
    this.pageTitleService.setTitle("My Bookmarks - Webnovel Hub");
    this.populateAll()
  }

  ngOnInit(): void {}

  populateAll() {
    this.readingService.getAllBookmarks().subscribe((result: Bookmark[]) => {
      this.collectionLoaded = Promise.resolve(true);
      this.bookmarks = result;
    });
  }

  search() {
    if (this.searchText == '' && this.searching == true) {
      this.populateAll();
      this.searching = false;

    } else {
      console.log("Bookmark: getting new list...");
      var searchWords = this.searchText.match(/(^|\s)(\w+)/g) || [];
      this.searchTags = this.searchText.match(/#\w+/g) || [];
      console.log("search words : "+searchWords);
      console.log("search tags : "+this.searchTags)
      
      if (searchWords.length > 0) {
      this.readingService.filterBookmarks(searchWords, this.searchTags).subscribe((result: Bookmark[]) => {
        this.bookmarks = result;
      });
    } else {
      this.readingService.filterBookmarksByTags(this.searchTags).subscribe((result: Bookmark[]) => {
        this.bookmarks = result;
      });
    }

      this.searching = true;
    }
  }

  edit(bookmark : Bookmark) {
    this.router.navigate(['/bookmark/edit', {bookmark: JSON.stringify(bookmark)}]);
  }

  delete(bookmark: Bookmark) {
    // const novelDetails: Reading = event;
    if(confirm("Are you sure to delete "+bookmark.title)) {
      this.readingService.deleteBookmark(bookmark);
      this.bookmarks.splice(this.bookmarks.indexOf(bookmark), 1);
    }
  }
}
