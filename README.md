### Command Line Application for Institute Management

This Java-based command line application is designed to manage the various administrative, academic, faculty, and student needs of an institute. The application provides different functionalities, categorized as follows:

![image](https://github.com/user-attachments/assets/4ac446fa-b40d-4c2d-8e62-76ba24ee1ea9)

### Usage

* make sure you have java sdk installed in your system

```sh
java --version
```

* Get the lastest release from **Releases** section of github.
download `.jar` file 
* run jar file
```sh
java -jar /path/to/universityapp.jar
```

---

### Implementation Details:
- **Language**: Fully implemented in Java.
- **Libraries**: Uses only the standard Java library.
- **Data Serialization**: Java's object serialization is used for data management.
- **Data Persistence**: Does not use any modern Database, works purely on Local file system, handles everything with java objects, a simple data access layer written from scratch.
- **Lazy Loading**: Complex relation ships like Sessions and Exams are loaded incrementally as the user proceeds.
- **Cacheing**: Caches recent queries temporarily on RAM (not tested yet).
- **Data Processing**: Leverages Java’s Stream API for sequential and parallel processing.
- **User Interface**: provides a simple and intuitive UI/UX

     > Example:
     > performs intuitive filtering through user inputs over large numbers of items (list of students)
---

#### Features:
1. **Admin Controls**
2. **Academic Controls**
3. **Faculty Facilities**
4. **Student Facilities**

---

### Admin Functionality:
The system offers one admin authority with the following capabilities:
1. **Faculty Management**: Perform CRUD (Create, Read, Update, Delete) operations on faculty records.
2. **Student Management**: Perform CRUD (Create, Read, Update, Delete) operations on student records.

![image](https://github.com/user-attachments/assets/04715172-666b-48ad-bcec-92b867497bd3)
---

### Academic Functionality:
One academic authority is responsible for:
1. **Exams**: Schedule and view exams.

![image](https://github.com/user-attachments/assets/13fe1e45-1b53-47ce-9c82-de0d7fc82319)
![image](https://github.com/user-attachments/assets/a484f4b8-236f-496e-92e1-66ed4b6b059b)
![image](https://github.com/user-attachments/assets/10ca60e5-471d-49d9-b9c5-df71fe4df0db)
![image](https://github.com/user-attachments/assets/15f12bad-4128-49fd-9574-12de3cacd354)

2. **Courses**:
   - Add and view courses.
   - Assign students to courses.
   - Assign faculty to courses.

![image](https://github.com/user-attachments/assets/b2004059-ed0e-4eea-8293-348b500adab0)
![image](https://github.com/user-attachments/assets/c448eb9e-62ca-4ef4-907e-a5e6303036ac)
---

### Faculty Functionality:
Faculty members can:
1. **Login/Logout** of the system.
![image](https://github.com/user-attachments/assets/e588cd7d-a29a-47ee-9a74-68616f60d3c0)
2. **Manage Sessions and Exams**:
   - Add, update, and delete sessions and exams.
   - View scheduled sessions and exams.

![image](https://github.com/user-attachments/assets/47cab5e1-2e91-4c72-83a6-41f279eb32dc)
![image](https://github.com/user-attachments/assets/8cf6b092-3a97-44ff-8d9b-01eae29832c4)
![image](https://github.com/user-attachments/assets/c1cd8054-0abf-479e-a9bf-d72b637ed277)
![image](https://github.com/user-attachments/assets/06a221b7-8f0e-48e8-86ce-7ca87c1df995)
![image](https://github.com/user-attachments/assets/c342de73-4dfe-4367-8bff-8c61c43f5652)
![image](https://github.com/user-attachments/assets/14736c4e-a5c5-46b1-bfd9-6deea2076a5c)
![image](https://github.com/user-attachments/assets/145f42dd-2038-4008-80b8-3fe763f1f732)
---

### Student Functionality:
Students can:
1. **View Attendance**: Access detailed attendance records for each session and course.
2. **View Exam Marks**: Check exam results for enrolled courses.
3. **Access Reports**: (Feature under development).

![image](https://github.com/user-attachments/assets/df3e576d-8483-42a0-bfad-bbaf42f3e0f6)
![image](https://github.com/user-attachments/assets/f11812d9-1b52-4486-be71-a15938e20b8d)
![image](https://github.com/user-attachments/assets/21e6bc55-786a-4e3f-80a2-06a47f4f7d64)
![image](https://github.com/user-attachments/assets/ec433fd5-91cb-46af-b71a-2db1dfbb038b)
![image](https://github.com/user-attachments/assets/903b4718-5339-4494-a747-b78a393a83e8)

---

### Performance Testing:
The application was tested with the following data:
- 100 faculty members
- 1400 students
- 40 courses
- 10 exams per course
- 4 courses per student
- 4 courses per faculty member
- 15 sessions per course per faculty member

During testing, **rare bugs emerged** due to the use of **parallel streams** in Java, which led to unexpected behavior under certain conditions.

---

This command-line application offers a simple tool for managing various academic and administrative tasks in an institute.
