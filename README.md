# Advanced Document and Task Management System
A web app with login, document upload, task assignment, and status tracking using H2 database.

## Features
- User login authentication.
- Document management: upload and list documents.
- Task management: assign tasks with status (open, in-progress, done).
- Data stored in H2 in-memory database.

## Technologies
- Backend: Java 8, Servlet, JSP
- Frontend: HTML, CSS, JavaScript, jQuery
- Database: H2 (in-memory)

## How to Run
1. Import into Eclipse with Tomcat 9 and Java 8 configured.
2. Add H2 JAR (h2-2.1.214.jar) to Build Path.
3. Run on server and access: `http://localhost:8080/AdvancedDocTaskApp/login.html`.
4. Login with username: `admin`, password: `123`.

## Notes
- Compatible with Java 8.
- Replace H2 with Oracle in production using JDBC.
