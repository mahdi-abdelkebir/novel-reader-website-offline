import { Component } from '@angular/core';
import { User } from './model/user';
import { TokenStorageService } from './service/user/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'novel-collection-frontend';

  private roles: string[] = [];
  isLoggedIn = false;
  adminRights = false;
  modRights = false;
  username?: string;

  constructor(private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      var user = this.tokenStorageService.getUser();
      this.roles = user.roles;

      this.adminRights = this.roles.includes('ROLE_ADMIN');
      this.modRights = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
  }
}
