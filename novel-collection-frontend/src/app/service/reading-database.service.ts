import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Chapter, Novel} from "../model/novel";
import { Bookmark, Reading } from '../model/user';
import { map } from "rxjs/operators"; 


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})

export class ReadingService {
  private readonly getAllBookmarksURL;
  private readonly getAllNovelsURL;
  private readonly addNovelURL;
  private readonly changeURL;
  private readonly deleteURL;

  constructor(private http: HttpClient) {
    this.getAllBookmarksURL = 'http://localhost:8080/api/users/bookmarks/all';
    this.getAllNovelsURL = 'http://localhost:8080/api/users/novels/all';
    this.addNovelURL = 'http://localhost:8080/api/users/novels/add';
    this.changeURL = 'http://localhost:8080/api/users/novel/change';
    this.deleteURL = 'http://localhost:8080/api/users/novels/remove';
  } 
  
  addNovel(novel: Novel) {

    this.http.post<Novel>(this.addNovelURL, novel, httpOptions).subscribe(data => {
      console.log(data);
      alert("added. ");
    });
  }

  getAllNovels(): Observable<Reading[]> {
    return this.http.get<Reading[]>(this.getAllNovelsURL);
  }

  change(reading: Reading) {

    this.http.post<Reading>(this.changeURL, reading, httpOptions).subscribe(data => {
      console.log(data);
    });
  }

  changeDirectly(novel_id: number, chapter_n: number) {
    this.http.get<string>(this.changeURL+"/novel="+novel_id+"&chapter="+chapter_n).subscribe();
  }

  delete(reading: Reading) {

    this.http.post<any>(this.deleteURL, reading, httpOptions).subscribe(data => {
      console.log(data)
      alert("deleted "+reading.compositeKey.novel.title+" from collections.");
    });
  }


  addBookmark(chapter: Chapter, content: string, title: string) {

    var bookmark = new Bookmark();
    bookmark.title = title;
    bookmark.chapter = chapter;
    bookmark.content = content;

    this.http.post<any>("http://localhost:8080/api/users/bookmarks/add", bookmark, httpOptions).subscribe(data => {
      console.log(data);
      alert("added. ");
    });
  }


  
  getAllBookmarks(): Observable<Bookmark[]> {
    return this.http.get<Bookmark[]>(this.getAllBookmarksURL);
  }

  editBookmark(bookmark : Bookmark): any {
    return this.http.post<Bookmark>("http://localhost:8080/api/users/bookmarks/edit", bookmark, httpOptions)
  }

  deleteBookmark(bookmark : Bookmark): any {
    return this.http.post<Bookmark>("http://localhost:8080/api/users/bookmarks/remove", bookmark, httpOptions).subscribe(data => {
      console.log(data)
      alert("deleted "+bookmark.id+" from collections.");
    });
  }

  filterBookmarks(words : string[], tags : string[]): Observable<Bookmark[]> {
    return this.getAllBookmarks().pipe(map(data => data.filter(bk => 
      (words.some(word => 
        bk.title!.toLowerCase().includes(word.toLowerCase())
      ) && tags.every(tag => 
        bk.tags!.toLowerCase().includes(tag.substring(1).toLowerCase())
      ))
    )));
  }

  filterBookmarksByTags( tags : string[]): Observable<Bookmark[]> {
    return this.getAllBookmarks().pipe(map(data => data.filter(bk => 
      tags.every(tag => 
        bk.tags!.toLowerCase().includes(tag.substring(1).toLowerCase())
      ))
    ));
  }
}
