# TERMINAL::BLOG — Angular 17 Frontend

Minimalist CRT-terminal themed blog frontend for the Spring Boot backend.

## Quick Start

```bash
npm install
ng serve        # dev server at http://localhost:4200
ng build        # production build
```

Dev server proxies `/api/*` → `http://localhost:8080` (see `proxy.conf.json`).

## Features

| Feature | Details |
|---------|---------|
| Auth | Login / Register with JWT, role-based routing |
| Feed | Posts from subscribed users (auth required) |
| Explore | All posts with live search (auth required) |
| Posts | Create / Edit / Delete with media (image/video/audio) |
| Media | Local blob preview — file only sent on form submit |
| Media Player | Custom video player with progress, volume, fullscreen |
| Comments | Add / Edit / Delete inline |
| Likes | Toggle like per post |
| Profiles | View any user, follow/unfollow, edit own bio |
| Reports | Report a user or post with reason (modal) |
| Notifications | Unread badge in navbar, mark read, dismiss |
| Admin Panel | 3 tabs — Users, Posts, Reports — ban/delete/resolve |

## Auth & Routing

All routes except `/login` and `/register` require authentication.  
Admin routes additionally require `ROLE_ADMIN`.

## CRT Aesthetic

- **Font**: VT323 (display) + Share Tech Mono (data/code)
- **Color**: Phosphor green (`#5bf870`) on dark background
- **Effects**: CSS scanlines, flicker animation, text-glow, vignette

## Tech

- Angular 17 standalone components
- Signals for state (`signal`, `computed`)
- Lazy-loaded routes
- Functional HTTP interceptor for JWT
