import { Component, Input, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Bookmark } from 'src/app/model/user';
import { ReadingService } from 'src/app/service/reading-database.service';
import { AuthService } from 'src/app/service/user/auth.service';

@Component({
  selector: 'app-bookmark-edit',
  templateUrl: './bookmark-edit.component.html',
  styleUrls: ['./bookmark-edit.component.css']
})
export class BookmarkEditComponent implements OnInit {
  
  bookmark : Bookmark;

  isSuccessful = false;
  isEditFailed = false;
  errorMessage = '';

  constructor(private readingService: ReadingService, private router: ActivatedRoute, private pageTitleService: Title) { 
    this.bookmark = JSON.parse(this.router.snapshot.paramMap.get('bookmark'));
    this.pageTitleService.setTitle("Bookmark Edit - Webnovel Hub");
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.readingService.editBookmark(this.bookmark).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isEditFailed = false;
      },

      error: err => {
        // this.errorMessage = err.error.message;
        // this.isEditFailed = true;

        this.isSuccessful = true;
        this.isEditFailed = false;
      }
    });
  }
}
