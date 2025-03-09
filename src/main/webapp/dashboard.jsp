<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="script.js"></script>
</head>
<body>
    <h2>Welcome, <%= session.getAttribute("user") %></h2>
    
    <!-- Form to upload document -->
    <form id="docForm">
        <label>Upload Document:</label>
        <input type="text" id="docName" placeholder="Document name">
        <button type="submit">Upload</button>
    </form>

    <!-- Form to assign task -->
    <form id="taskForm">
        <label>Task Name:</label>
        <input type="text" id="taskName" placeholder="Task name">
        <label>Assign To:</label>
        <input type="text" id="assignedTo" placeholder="Username">
        <button type="submit">Assign</button>
    </form>

    <!-- Display area for lists -->
    <div id="list"></div>
</body>
</html>