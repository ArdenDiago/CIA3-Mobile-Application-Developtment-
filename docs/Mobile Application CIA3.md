# CHRIST (Deemed to be University), Bangalore - 560029
## Department of Computer Science  
### CIA III - March 2026  
**PG III Trimester**

**Programme Name:** MCA  
**Max Marks:** 20  

**Course Name:** Mobile Application Development  
**Course Code:** MCA518-3  
**Date:** 11/03/2026  
**Time:** 2 Hours

---

# General Instructions

- Verify the **Course code**, **Course title**, and **number of pages** in the question paper.
- Ensure your **mobile phone is switched off** and placed in the designated area.
- **Malpractices will be viewed very seriously.**
- **All programs are mandatory.**
- After completion, students must upload the **PDF and Source Code** to **Google Classroom (GCR)** and **GitHub**.

### Submission Requirements

1. **Single PDF File** containing:
   - Screenshots of the executed outputs
   - Source code for each question (pasted or inserted clearly)

2. **ZIP File** containing:
   - All source code files  
   - Example: `.html`, `.js`, `.css`, `.jsx`, etc.

3. **File Naming Convention**
   - Use your **Roll Number**
   - Example: `2547123.zip`

---

# Course Outcomes (COs)

Students will be able to:

**CO1:** Design interactive Android apps with effective UI/UX and navigation.  
**CO2:** Analyze Activity and Fragment lifecycles for performance optimization.  
**CO3:** Evaluate data storage and network communication for efficient handling.  
**CO4:** Implement asynchronous programming for responsiveness and background tasks.  
**CO5:** Assess Android security and best practices for scalable, secure apps.

---

# Question 1

## Case Study: Personal Task Manager App

You are required to develop a **simple Android Mobile Application** using **Kotlin and Jetpack Compose** to manage daily tasks.

The application should allow users to:

- Add tasks
- View tasks
- Update tasks
- Delete tasks

All tasks must be stored using the **Room Database**.

The application must follow the **MVVM (Model–View–ViewModel)** architecture and include modern Android UI components such as:

- **Top App Bar**
- **Bottom Navigation**
- **Floating Action Button (FAB)**

---

# Functional Requirements

## 1. Top App Bar (3 Marks)

- Add a **Top App Bar (TopBar)** displaying the title:


Task Manager


- Include an **icon** on the TopBar (menu/settings icon).
- The **TopBar must remain visible across all screens**.

---

## 2. Room Database with MVVM Architecture (10 Marks)

Implement the **Room Database** using **MVVM architecture**.

---

### Model Layer

Create a **Task Entity** with the following attributes:

| Field | Type |
|------|------|
| taskId | Int (Primary Key – AutoGenerate) |
| title | String |
| description | String |
| dueDate | String |

---

### DAO Layer

Implement the following **CRUD operations**:

- Insert a new task
- Retrieve all tasks
- Update an existing task
- Delete a task

The retrieve operation should return data using **Flow or LiveData**.

---

### Repository Layer

Create a **Repository class** that:

- Acts as an intermediary between **DAO and ViewModel**
- Handles database operations
- Exposes task data to the **ViewModel**

---

### ViewModel Layer

Create a **TaskViewModel** that:

- Interacts with the **Repository**
- Provides task data to the **UI**
- Handles **insert, update, and delete operations**
- Observes data using **Flow or LiveData**

---

## 3. Floating Action Button (FAB) – Add Task (2 Marks)

- Add a **Floating Action Button (FAB)** at the **bottom-right corner**.
- When clicked:
  - Navigate to a **screen or dialog to add a new task**.
  - Save the task using the **ViewModel**, which communicates with the **Repository and Room database**.

---

## 4. Bottom Navigation (3 Marks)

Add **Bottom Navigation** with **two tabs**:

### Tasks
- Displays the list of tasks retrieved through **ViewModel**

### Completed / Manage
Either:

- Display a message:


Manage Tasks Here


**OR**

- Provide options to **delete tasks**

---

## 5. Task Display and Operations (2 Marks)

- Display all tasks using **LazyColumn / RecyclerView**
- Observe data using **Flow or LiveData** from the **ViewModel**
- Provide options to:
  - Update task
  - Delete task

The **UI should automatically update** when the database changes.

---

# Evaluation Rubric (20 Marks)

| Component | Marks |
|----------|------|
| Top App Bar Implementation | 3 |
| Room Database with MVVM (Entity + DAO + Repository + ViewModel) | 10 |
| Floating Action Button (Add Task) | 2 |
| Bottom Navigation | 3 |
| Task Display & Update/Delete using ViewModel | 2 |

---

# Course Outcomes Covered

**CO1, CO2, CO3, CO4**

---

# Revised Bloom’s Taxonomy (RBT) Levels

| Level | Description |
|------|-------------|
| L1 | Remembering |
| L2 | Understanding |
| L3 | Applying |
| L4 | Analyzing |
| L5 | Evaluating |
| L6 | Creating |
