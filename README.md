# RevPlay – Console-Based Music Streaming Application

RevPlay is a **Java console-based music streaming application** developed using **Core Java, JDBC, MySQL, Maven**, and follows a **layered architecture**.

The application supports **two roles — User and Artist** — and provides real-world music platform features such as **song management, playlists, favorites, listening history, logging, and unit testing**.

This project demonstrates **end-to-end backend development**, **clean architecture**, **database interaction**, **logging**, and **mock testing**.

---

## Key Features

### User Features
- User registration and login (password hashing with BCrypt)
- View all available songs
- Search songs by **title / genre / artist / album**
- Browse songs by category
- Play songs using a **console-based music player**
- Add and remove songs from favorites
- View favorite songs
- Create playlists (**public / private**)
- Add songs to playlists
- Remove songs from playlists
- Update and delete playlists
- View public playlists
- Track complete listening history
- View recently played songs
- View play count (per song)

---

### Artist Features
- Artist registration and login
- Create and manage artist profile  
  *(bio, genre, social links)*
- Create albums
- Upload songs (**with or without albums**)
- Add songs to albums
- View uploaded songs
- Update song details
- Update album details
- Delete songs
- View play count statistics for uploaded songs
- View users who favorited artist songs
- Artist-specific data security  
  *(artists can manage only their own data)*

---

## Project Architecture

RevPlay follows a **clean layered architecture** to ensure **separation of concerns**, **maintainability**, and **scalability**.

---

### 1️. Model Layer (`org.example.model`)

Contains **POJO classes** that represent core domain entities.  
These classes map directly to database tables and contain **only fields, getters, and setters**.

**Examples:**
- User  
- Artist  
- Song  
- Album  
- Playlist  
- ListeningHistory  
- Favorite  

---

### 2️. DAO Layer (`org.example.dao`)

Handles all **database operations** using JDBC.

**Responsibilities:**
- CRUD operations  
- SQL execution using `PreparedStatement`  
- Prevent SQL injection  
- Manage database resources using try-with-resources  

**Examples:**
- UserDao  
- ArtistDao  
- SongDao  
- AlbumDao  
- PlaylistDao  
- FavoriteDao  
- ListeningHistoryDao  

---

### 3️. Service Layer (`org.example.service`)

Contains the **business logic** of the application.

**Responsibilities:**
- Input validation  
- Business rule enforcement  
- User/Artist authorization checks  
- Coordination between multiple DAOs  
- Play count and listening history updates  

**Examples:**
- UserService  
- ArtistService  
- SongService  
- AlbumService  
- PlaylistService  
- FavoriteService  
- PlayerService  

---

### 4️. Controller Layer (`org.example.controller`)

Manages the **console-based user interface**.

**Responsibilities:**
- Menu navigation  
- User input handling  
- Displaying output to console  
- Calling appropriate service methods  
- No database logic  

**Examples:**
- Main  
- UserController  
- ArtistController  
- PlayerController  

---

### 5️. Configuration Layer (`org.example.config`)

Handles **application-level configuration**.

**Key Class:**
- `DBConnection` – Manages MySQL database connectivity using JDBC  

---

### 6️. Logging Layer (Log4j 2)

Logging is implemented using **Log4j 2**, replacing `System.out.println`.

**Features:**
- Centralized logging configuration  
- Logs written to both console and file  
- Supports `INFO`, `WARN`, and `ERROR` levels  
- Used across DAO and Service layers  

---

### 7️. Testing Layer (`src/test/java`)

Implements **unit testing and mock testing**.

**Tools Used:**
- JUnit 5  
- Mockito  

**Testing Strategy:**
- DAO layer tested using **real database**  
- Service layer tested using **Mockito (DAO mocked)**  
- Ensures business logic correctness without DB dependency  

---

##  Technologies Used

- Java 21 (LTS)  
- JDBC  
- MySQL  
- Maven  
- Log4j 2  
- JUnit 5  
- Mockito  
- BCrypt (Password Hashing)  
- IntelliJ IDEA  
- Git & GitHub  

---

##  Database Design

**Database Name:** `revplay_db`  
**Database Type:** MySQL  

### Tables Used

- users  
- artists  
- albums  
- songs  
- favorites  
- playlists  
- playlist_songs  
- listening_history  

Foreign key relationships are used to:
- Maintain data integrity  
- Enforce artist/user-specific access  

---

##  Security

- Passwords are hashed before storage  
- No plain-text password storage  
- Secure password verification during login  
- Artist/User access control enforced at service level  

---

##  Running the Application

1. Clone the repository
2. Create MySQL database and tables using SQL scripts
3. Update DB credentials in DBConnection.java
4. Run

###  Run Tests
```bash
mvn test



