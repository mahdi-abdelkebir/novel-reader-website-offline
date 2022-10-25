import { DOCUMENT } from '@angular/common';
import {AfterViewInit, Component, ElementRef, HostListener, Inject, OnInit, QueryList, ViewChildren, ViewEncapsulation} from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ReadingService } from 'src/app/service/reading-database.service';
import { TokenStorageService } from 'src/app/service/user/token-storage.service';
import {Chapter, Novel} from "../../model/novel";
import {NovelService} from "../../service/novel-database.service";

@Component({
  selector: 'app-novel-chapter',
  templateUrl: './novel-chapter.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./novel-chapter.component.css']
})
export class ChapterComponent implements OnInit, AfterViewInit {
  chapter: Chapter;
  novel: Novel;
  chapterN: number;
  adminRights: boolean;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private novelService: NovelService, private titleService: Title, private readingService: ReadingService, @Inject(DOCUMENT) private document: Document, private tokenStorage: TokenStorageService) {

    this.adminRights = this.tokenStorage.getUser().roles.includes('ROLE_ADMIN');
  }

  ngOnInit(): void {
    this.titleService.setTitle("Loading...");

    var slug = this.activatedRoute.snapshot.paramMap.get("slug");
    var chNumber = this.activatedRoute.snapshot.paramMap.get("chapter");


    this.novelService.getChapter(slug, chNumber).subscribe(result => {

      this.chapter = result;
      this.novel = result.compositeKey.novel;
      this.chapterN = result.compositeKey.number;
      this.readingService.changeDirectly(this.novel.id, this.chapterN);
      
      this.titleService.setTitle("Chapter "+this.chapterN+" - "+this.novel.title+" - Readers Hub");
    });
  }

  pressedArrow: string = "";
  interval;

  @HostListener('window:keyup', ['$event'])
  keyEvent(event: KeyboardEvent) {
    var key = event.key;
    if (key == "ArrowRight" || key == "ArrowLeft") {
      if (key == this.pressedArrow) {

        if (key == "ArrowRight") {
          this.onNext();
        } else {
          this.onPrev();
        }

      } else {
        this.pressedArrow = key;

        this.interval = setInterval(() => {
          this.pressedArrow = "";
          clearInterval(this.interval);
        },1000)
      }
    }
  }

  onNavigate(chapter : number) : void {
    this.router.navigate(["/novel/"+ this.novel.slug+ "/"+ chapter]).then(() => {
        window.location.reload();
        this.document.documentElement.scrollTop = 0;
    });
  }

  onPrev() : void {
    this.router.navigate(["/novel/"+ this.novel.slug+ "/"+ (this.chapterN-1)]).then(() => {
        window.location.reload();
        this.document.documentElement.scrollTop = 0;
    });
  }

  onNext() : void {
    this.router.navigate(["/novel/"+ this.novel.slug+ "/"+ (this.chapterN+1)]).then(() => {
        window.location.reload();
        this.document.documentElement.scrollTop = 0;
    });
  }

  sliderDiv : HTMLElement;
  @ViewChildren("itemElement") private itemElements: QueryList<ElementRef>;
  
  ngAfterViewInit(): void {
    this.sliderDiv =  this.document.getElementById("slider_div");
    this.itemElements.changes.subscribe(() => {
      console.log("Item elements are now in the DOM!", this.itemElements.length);

      var current = this.document.getElementById("chapter-"+this.chapterN);
      current.classList.add("active");
      console.log(current);
      this.sliderDiv.scrollTop = current.offsetTop;
    });
  }

  chapterList : Chapter[];
  firstLoad: boolean = false;
  chaptersLoaded : Promise<boolean>;
  
  slider_open() : void {
    if (this.firstLoad == false) {
      this.firstLoad = true;

      this.novelService.getChapterList(this.novel.slug).subscribe(result => {
        this.chaptersLoaded = Promise.resolve(true);
        this.chapterList = result;
      });
    }

    this.sliderDiv.style.display = "block";
  }
  
  slider_close() : void {
    this.sliderDiv.style.display = "none";
  }


  slider_download(): void {
    this.novelService.downloadAllChapters(this.novel.slug, this.chapterN);
  }

  bookmark(): void {
    var title = window.prompt("Write a title for this bookmark: ")
    
    if (title != null && title.replace(/ /g, '') != "") {

      var range = window.getSelection().getRangeAt(0),
      content = range.cloneContents(),
      span = document.createElement('span');
      
      span.appendChild(content);

      var htmlContent = span.innerHTML;

      if (htmlContent.replace(/ /g, '') != "") {
        alert("Bookmark saved.")
        this.readingService.addBookmark(this.chapter, htmlContent, title);
      }
    }
  }
}
