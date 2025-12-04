📚 Library Management System – Java Spring Boot Project

Links for info and presentations:
https://www.canva.com/design/DAG6iSSHk30/hg7BIoi6v08f6MhLT6TYuA/view?utm_content=DAG6iSSHk30&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h9e8b704472
https://www.canva.com/design/DAG6iX10cTM/tSIjZeM2JEJWllGrzIg4Zw/view?utm_content=DAG6iX10cTM&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h003238b3b7#1

For clean and running locally:
.\mvnw clean
.\mvnw spring-boot:run

A modern Library Management System built with Spring Boot, Spring Security, Thymeleaf, JPA, and H2 Database.
It allows users to browse books, borrow physical books, view history, and for admins to manage the catalog.

🚀 Features
👤 User Features

✔ Login & Register
✔ View all books (E-book / Physical)
✔ Borrow physical books
✔ Read/View uploaded E-book files
✔ View My Borrowed Books
✔ View Loan & Reading History
✔ Logout safely

🛠 Admin Features

✔ Add new books (upload e-book PDF/EPUB)
✔ Mark borrowed books as returned
✔ Remove/disable unavailable books
✔ Automatically hides borrowed physical books for users

🧑‍💻 Tech Stack
Technology	Purpose
Java 21+	Core language
Spring Boot 3+	Backend Framework
Spring Security	Authentication + Authorization
Spring Data JPA	Database ORM
H2 In-Memory Database	Development DB
Thymeleaf	View Engine
Bootstrap 5	UI Styling
📌 Default Login Credentials
Role	Username	Password
Admin	admin	admin123
User	testuser	test123

These are auto-created on first run.

📁 Project Structure
library-manager/
 ├─ src/main/java/com/libraryapp/
 │   ├─ controller/
 │   ├─ service/
 │   ├─ model/
 │   ├─ repository/
 │   ├─ config/
 │   └─ LibraryManagerApplication.java
 ├─ src/main/resources/
 │   ├─ templates/*.html
 │   ├─ static/css | js | images (optional)
 │   └─ application.properties
 ├─ uploads/ (E-book storage generated at runtime)
 └─ pom.xml

▶️ How to Run
Prerequisites

Java JDK 21 or higher

Maven Wrapper already included (mvnw)

Run the project
cd library-manager
./mvnw spring-boot:run

Open Browser:
http://localhost:8082/login

🧪 H2 Database Access

Database is in-memory, resets every restart.

URL → http://localhost:8082/h2-console

JDBC URL → jdbc:h2:mem:librarydb

Username → sa

Password → password

📚 Book Management Logic
Book Type	Borrow?	Seen in Catalog?
Physical Book	Yes	Hidden after borrowed
E-Book	No borrow	Always visible
Returned Book	Yes	Visible again
🛡 Security Rules
Route	Access
/login, /register	Public
/catalog, /mybooks, /history	Logged-in users
/admin/**	Admin only
🎨 UI Highlights

✔ Cleaner, modern Bootstrap UI
✔ Responsive pages
✔ Clear navigation for users/admin

📝 Future Enhancements (Optional)

🔹 Pagination & search
🔹 Book availability count
🔹 Email notifications on borrow/return
🔹 MySQL/PostgreSQL production setup

👨‍🏫 Project Details (Micro Project Submission)

Course: Java Micro Project

Student: Abdullah Saad Sharief

USN: 1SI24CS003

College: Siddaganga Institute of Technology, Tumkur

Department: Computer Science & Engineering

Semester: 3rd Sem

📌 License

This project is created for academic purposes. Free to use and modify.
