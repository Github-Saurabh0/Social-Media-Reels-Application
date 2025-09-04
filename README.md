
# Social Media Reels Application  

> A modern Instagram Reels–style application with **Java Spring Boot backend**, **CRUD APIs**, and an **AI-modern UI theme** (React/Tailwind or Android).  

---

## Features  

- Upload, view, edit, and delete short videos (CRUD)  
- Like & view counters in real-time  
- Public & Private reels  
- AI features: auto-caption, thumbnail generation, hashtag suggestions  
- Modern AI-inspired UI (glassmorphism, gradient, animations)  
- Personalized feed system (future scope)  

---

##  Architecture  

```
Frontend (React / Android)
        ↓ REST APIs
Backend (Spring Boot + Java)
        ↓
Database (MySQL) + Cloud Storage (S3/CDN)
```

---

## Database Schema  

### Users Table  
| Column     | Type        | Description |
|------------|------------|-------------|
| id         | BIGINT PK  | User ID |
| username   | VARCHAR    | Unique username |
| email      | VARCHAR    | Email |
| password   | VARCHAR    | Encrypted password |
| created_at | TIMESTAMP  | Created time |

### Reels Table  
| Column       | Type        | Description |
|--------------|------------|-------------|
| id           | BIGINT PK  | Reel ID |
| user_id      | BIGINT FK  | Reel owner |
| title        | VARCHAR    | Reel title |
| description  | TEXT       | Reel description |
| video_url    | VARCHAR    | Stored video link |
| thumbnail_url| VARCHAR    | Thumbnail image |
| duration_ms  | BIGINT     | Video length |
| likes_count  | INT        | Likes |
| views_count  | INT        | Views |
| is_private   | BOOLEAN    | Public/Private |
| created_at   | TIMESTAMP  | Created time |
| updated_at   | TIMESTAMP  | Updated time |

---

## API Endpoints  

### Reels CRUD  
- `POST /api/reels` → Create new reel  
- `GET /api/reels` → Get all reels (feed)  
- `GET /api/reels/{id}` → Get single reel  
- `PUT /api/reels/{id}` → Update reel  
- `DELETE /api/reels/{id}` → Delete reel  

###️ Engagement  
- `POST /api/reels/{id}/like` → Like a reel  
- `POST /api/reels/{id}/view` → Increase view count  

---
 

### Reel Entity  
```java
@Entity
@Table(name = "reels")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long durationMs;
    private Integer likesCount = 0;
    private Integer viewsCount = 0;
    private boolean isPrivate = false;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
```

### Reel Controller  
```java
@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
public class ReelController {
    private final ReelService reelService;

    @PostMapping
    public ResponseEntity<ReelDTO> createReel(@RequestBody ReelDTO reelDTO) {
        return ResponseEntity.ok(reelService.createReel(reelDTO));
    }

    @GetMapping
    public ResponseEntity<List<ReelDTO>> getAllReels() {
        return ResponseEntity.ok(reelService.getAllReels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReelDTO> getReel(@PathVariable Long id) {
        return ResponseEntity.ok(reelService.getReel(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReelDTO> updateReel(@PathVariable Long id, @RequestBody ReelDTO reelDTO) {
        return ResponseEntity.ok(reelService.updateReel(id, reelDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReel(@PathVariable Long id) {
        reelService.deleteReel(id);
        return ResponseEntity.ok("Reel deleted successfully");
    }
}
```

---

## AI-Modern UI Theme  

- **Feed Page** → Swipeable reels, floating like/comment/share buttons  
- **Upload Page** → Drag & drop videos, AI-caption & AI-thumbnail generator  
- **Profile Page** → User reels, follower count, settings  
- **Theme** →  
  - Gradient (`from-purple-500 via-pink-500 to-red-500`)  
  - Glassmorphism (blur, transparency)  
  - Dark mode default  
  - Framer Motion animations  

---

##️ Deployment  

- Backend: Spring Boot → AWS EC2 / GCP App Engine  
- Database: MySQL (AWS RDS)  
- Video Storage: AWS S3 / Cloudinary  
- CDN: CloudFront for fast video streaming  
- Docker + Kubernetes for scaling  

---

## Security  

- JWT Authentication  
- Role-based access (User/Admin)  
- File type & size validation  
- HTTPS enforced  

---

## Future Scope  

-  Auto-caption (speech-to-text)  
-  AI thumbnail generation  
-  AI-based personalized feed  
-  AI hashtag recommendations  

---

## Author  

Developed by **Saurabhh**  
🔗 [LinkedIn](https://www.linkedin.com/in/saurabh884095/)  

---

## License  

This project is licensed under the MIT License.  



