import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: '../auth-container/auth-container.component.css'
})
export class LoginComponent {

  isHidden = true;

  visibilidadePassword() {
    this.isHidden = !this.isHidden;
  }
}
