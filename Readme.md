# 🎼 Genius Clone - JavaFX Project

## ✨ Project Purpose
Genius Clone is an Advanced Programming course project developed to replicate the essential features of the Genius.com platform using Java. Designed as a desktop application, it offers an interactive GUI experience for users, artists, and administrators to explore, create, manage, and engage with music content. The project highlights strong Java OOP fundamentals, intuitive JavaFX interface design, and robust data persistence using Gson.

## 🌟 Key Features

### 🔑 User Roles
- **User**: Browse and discover songs, follow artists, engage with comments, and suggest edits.
- **Artist**: Create and manage songs and albums, edit lyrics, and moderate user submissions.
- **Admin**: Perform administrative operations, oversee permissions, and manage data integrity.

### 🎶 Songs & Albums Management
- Create songs with genre, tags, contributors, and lyrics.
- Organize songs into albums.
- Display metadata including views, artists, and lyrics.

### 🔍 Explore Functionality
- Full-text search by song title.
- Sorting options: A–Z, most viewed, least viewed.
- Advanced filters: genre, tags, and contributing artists.

### 💬 Interactive Comments
- Users can add, view, and like comments.
- Comment sorting by likes or chronological order.

### 🌿 Lyric Suggestion System
- Users submit lyric changes.
- Artists receive, review, and approve/reject changes.

### 🛡️ Permission Control
- Fine-grained permission handling (e.g., READ, EDIT, FOLLOW, COMMENT).
- Each account has a role-based permission set.

### 💾 Persistent Local Storage
- All data is stored in a JSON file (`data-store.json`).
- Fully restorable between sessions.

## ⚙️ Setup & Execution Guide

### 🔧 Prerequisites
- Java JDK 17 or above
- JavaFX SDK version 23 or newer
- Maven (dependency management)

### ▶️ JavaFX VM Arguments
```bash
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```
> Replace `/path/to/javafx-sdk/lib` with your JavaFX SDK path.

### 📦 Maven Dependencies
- All required dependencies are listed in `pom.xml`.
- Includes JavaFX and Gson.

### 🏁 Launch Main Class
```bash
com.genius.ui.Main
```
> This starts the application from the designated entry point.

## 🚀 Technologies Used
- Java 17+
- JavaFX (Graphical Interface)
- Gson (JSON handling)
- Maven (Project build system)

## 👨‍💻 Authors & Licensing
Created by MohammadFazel Sadeghizad for the Advanced Programming course at Shahid Beheshti University.

License: **MIT** — Open-source and freely extensible.

---
> “Where words fail, music speaks.”

