import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NovelCollectionComponent} from './components/novels-collection/novels-collection.component';
import {NovelSearchComponent} from './components/novel-search/novel-search.component';
import { ProfileComponent } from './components/user/profile/profile.component';
import { RegisterComponent } from './components/user/register/register.component';
import { LoginComponent } from './components/user/login/login.component';
import { ChapterComponent } from './components/novel-chapter/novel-chapter.component';
import { BookmarkListComponent } from './components/bookmark-list/bookmark-list.component';
import { BookmarkEditComponent } from './components/bookmark-list/bookmark-edit/bookmark-edit.component';

const routes: Routes = [
  { path: 'novels-collection', component: NovelCollectionComponent },
  { path: 'bookmark-list', component: BookmarkListComponent },
  { path: 'bookmark/edit', component: BookmarkEditComponent },
  { path: 'novel-search', component: NovelSearchComponent },
  
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'novel/:slug/:chapter', component: ChapterComponent },

  { path: '', redirectTo: 'novel-search', pathMatch: 'full' }

  // { path: 'pick-random', component: PickRandomGameComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
