import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Chapter, Genre, Novel} from "../model/novel";

const novel_status : String[] = ["", "Completed", "OnGoing", "Cancelled", "On Hold"];
const reading_status : String[] = ["Unread", "Reading", "Dropped"];

@Injectable({
  providedIn: 'root'
})

export class NovelService {
  private readonly sourceURL;

  constructor(private http: HttpClient) {
    this.sourceURL = "http://localhost:8080"
  }

  getAllNovels(): Observable<Novel[]> {
    return this.http.get<Novel[]>(this.sourceURL+"/api/res/novels/all");
  }

  getAllGenres(): Observable<Genre[]> {
    return this.http.get<Genre[]>(this.sourceURL+"/api/res/genres/all");
  }

  getAllStatus(): String[] {
     return novel_status;
  }

  getAllReadingStatus(): String[] {
    return reading_status;
 }

  public search(searchParams: string): Observable<Novel[]> {
    return this.http.get<Novel[]>(this.sourceURL+ "/api/res/search/" + searchParams);
  }

  public downloadByTitle(title: string): void {
    this.http.get<void>(this.sourceURL+ "/api/res/download/novels/title=" + title).subscribe();
  }

  public getChapter(slug: string, chapter: string): Observable<Chapter> {
    return this.http.get<Chapter>(this.sourceURL + "/api/res/novel/"+slug+"/chapter-"+chapter);
  }

  public getChapterList(slug: string): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(this.sourceURL+ "/api/res/novel/"+slug+"/chapter-all");
  }

  public downloadAllChapters(slug: string, startFrom: number): void {
    this.http.get<void>(this.sourceURL+ "/api/res/download/chapters/name="+slug+"/start="+startFrom).subscribe();
  }

  
}
