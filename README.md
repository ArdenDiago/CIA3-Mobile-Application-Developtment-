# Task Manager - CIA3 Mobile Application

A **Personal Task Manager** Android application built with **Kotlin** and **Jetpack Compose**, following the **MVVM (Model-View-ViewModel)** architecture pattern.

## 📱 Features

- **Add Tasks** – Create new tasks with title, description, and due date
- **View Tasks** – Browse all tasks in a scrollable list with reactive updates
- **Update Tasks** – Edit existing task details
- **Delete Tasks** – Remove individual tasks or clear all at once
- **Persistent Storage** – All data stored locally using Room Database

## 🏗️ Architecture (MVVM)

```
┌──────────────────────────────────────────────┐
│                  UI Layer                     │
│  (Screens, Components, Theme)                │
│  - TaskListScreen, AddTaskScreen             │
│  - EditTaskScreen, ManageTasksScreen         │
│  - TopAppBar, BottomNav, TaskCard, FAB       │
├──────────────────────────────────────────────┤
│              ViewModel Layer                  │
│  - TaskViewModel (Flow, StateFlow)           │
│  - TaskViewModelFactory                      │
├──────────────────────────────────────────────┤
│             Repository Layer                  │
│  - TaskRepository                            │
├──────────────────────────────────────────────┤
│               Data Layer                      │
│  - TaskDao (CRUD Operations)                 │
│  - TaskDatabase (Room, Singleton)            │
│  - Task Entity (Model)                       │
└──────────────────────────────────────────────┘
```

## 📂 Project Structure

```
com.example.cia3/
├── data/
│   ├── model/
│   │   └── Task.kt                 # Room Entity
│   ├── dao/
│   │   └── TaskDao.kt              # Data Access Object
│   ├── database/
│   │   └── TaskDatabase.kt         # Room Database
│   └── repository/
│       └── TaskRepository.kt       # Repository Layer
├── viewmodel/
│   └── TaskViewModel.kt            # ViewModel + Factory
├── navigation/
│   ├── Screen.kt                   # Route Definitions
│   └── TaskNavGraph.kt             # Navigation Graph
├── ui/
│   ├── components/
│   │   ├── TaskTopAppBar.kt        # Top App Bar
│   │   ├── BottomNavigationBar.kt  # Bottom Navigation
│   │   └── TaskCard.kt             # Task Card Component
│   ├── screens/
│   │   ├── AddTaskScreen.kt        # Add Task Form
│   │   ├── TaskListScreen.kt       # Task List (LazyColumn)
│   │   ├── EditTaskScreen.kt       # Edit Task Form
│   │   └── ManageTasksScreen.kt    # Manage/Delete Tasks
│   └── theme/
│       ├── Color.kt                # Color Palette
│       ├── Theme.kt                # Material Theme
│       └── Type.kt                 # Typography
├── MainActivity.kt                  # Entry Point
└── TaskManagerApplication.kt        # Application Class
```

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Kotlin | Primary language |
| Jetpack Compose | Modern declarative UI |
| Room Database | Local SQLite persistence |
| Navigation Compose | Screen navigation |
| ViewModel | UI state management |
| Kotlin Flow | Reactive data streams |
| Material 3 | Design system |
| KSP | Annotation processing |

## 🚀 How to Run

1. Open the project in **Android Studio**
2. Sync Gradle dependencies
3. Run on an emulator or physical device (min SDK 24)

## 📋 Course Information

- **Course:** Mobile Application Development (MCA518-3)
- **Programme:** MCA - PG III Trimester
- **Institution:** CHRIST (Deemed to be University), Bangalore
- **Date:** March 2026
