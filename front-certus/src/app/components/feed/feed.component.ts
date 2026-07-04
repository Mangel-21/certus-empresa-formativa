import { Component, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { Card } from 'primeng/card';
import { Button } from 'primeng/button';
import { InputTextarea } from 'primeng/inputtextarea';
import { SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, RouterLink, Card, Button, InputTextarea, FormsModule],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent implements OnInit {
  user: SocialUser | null = null;
  postText: string = '';
  posts: any[] = [
    {
      authorName: 'Juan Pérez',
      authorPicture: '',
      timeAgo: 'Hace 2 horas',
      content: '¡Acabo de terminar mi proyecto de Spring Boot y Angular! Aquí les comparto el repositorio.'
    }
  ];

  constructor(
    private authService: SocialAuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.user = user;
    });
  }

  publicar(): void {
    if (this.postText.trim().length > 0 && this.user) {
      this.posts.unshift({
        authorName: this.user.name,
        authorPicture: this.user.photoUrl,
        timeAgo: 'Justo ahora',
        content: this.postText
      });
      this.postText = ''; // Limpiar el área de texto
    }
  }

  logout() {
    this.authService.signOut().then(() => {
      this.router.navigate(['/login']);
    }).catch((err) => {
      console.error(err);
      this.router.navigate(['/login']);
    });
  }
}
