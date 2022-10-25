import { BrowserModule } from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { NovelSearchComponent } from './components/novel-search/novel-search.component';
import { NovelCollectionComponent } from './components/novels-collection/novels-collection.component';
import { AppRoutingModule } from './app-routing.module';
import {HttpClientModule} from '@angular/common/http';
import {NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
// import { PickRandomNovelComponent } from './components/pick-random-novel/pick-random-novel.component';
import {SHARED_DECLARATIONS} from "./shared/app.shared-declarations";
import { RegisterComponent } from './components/user/register/register.component';
import { LoginComponent } from './components/user/login/login.component';
import { ProfileComponent } from './components/user/profile/profile.component';
import { authInterceptorProviders } from './utils/auth.interceptor';
import { sanitizeHtmlPipe } from './components/novel-search/sanitize-html.pipe';
import { ChapterComponent } from './components/novel-chapter/novel-chapter.component';
import { BookmarkListComponent } from './components/bookmark-list/bookmark-list.component';
import { BookmarkEditComponent } from './components/bookmark-list/bookmark-edit/bookmark-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    NovelSearchComponent,
    BookmarkListComponent,
    BookmarkEditComponent,
    ChapterComponent,
    NovelCollectionComponent,
    sanitizeHtmlPipe,
    // PickRandomNovelComponent,
    SHARED_DECLARATIONS
  ],
  imports: [

    BrowserModule,
    FormsModule,
    Ng2SearchPipeModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgbTooltipModule,
    NgMultiSelectDropDownModule.forRoot()
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
