package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DocumentTaskServlet")
public class DocumentTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // H2 in-memory database configuration
    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    // Initialize database tables when servlet starts
    @Override
    public void init() throws ServletException {
        Connection conn = null;
        try {
            // Manually register H2 driver to ensure it loads in Java 8
            Class.forName("org.h2.Driver");
            System.out.println("H2 Driver loaded successfully"); // Debug to confirm driver registration
            // Establish connection to H2 database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to H2 database"); // Debug to confirm connection
            // Create documents table if it doesn't exist
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS documents (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), uploaded_by VARCHAR(50), upload_date DATE)");
            // Create tasks table if it doesn't exist
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS tasks (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), assigned_to VARCHAR(50), status VARCHAR(20), created_date DATE)");
        } catch (ClassNotFoundException e) {
            // Log error if H2 driver is missing from classpath
            System.err.println("H2 Driver not found in classpath: " + e.getMessage());
            throw new ServletException("H2 Driver not found", e);
        } catch (SQLException e) {
            // Log error if SQL fails during initialization
            System.err.println("SQL Error during initialization: " + e.getMessage());
            throw new ServletException("Database initialization failed", e);
        } finally {
            // Close connection safely
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Handle GET request to display documents and tasks
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rsDocs = null;
        ResultSet rsTasks = null;

        try {
            // Connect to H2 database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Fetch and display documents
            rsDocs = stmt.executeQuery("SELECT * FROM documents");
            response.getWriter().write("<h3>Documents</h3><table border='1'><tr><th>ID</th><th>Name</th><th>Uploaded By</th><th>Date</th></tr>");
            while (rsDocs.next()) {
                response.getWriter().write("<tr><td>" + rsDocs.getInt("id") + "</td><td>" + rsDocs.getString("name") +
                    "</td><td>" + rsDocs.getString("uploaded_by") + "</td><td>" + rsDocs.getDate("upload_date") + "</td></tr>");
            }
            response.getWriter().write("</table>");

            // Fetch and display tasks
            rsTasks = stmt.executeQuery("SELECT * FROM tasks");
            response.getWriter().write("<h3>Tasks</h3><table border='1'><tr><th>ID</th><th>Name</th><th>Assigned To</th><th>Status</th><th>Date</th></tr>");
            while (rsTasks.next()) {
                response.getWriter().write("<tr><td>" + rsTasks.getInt("id") + "</td><td>" + rsTasks.getString("name") +
                    "</td><td>" + rsTasks.getString("assigned_to") + "</td><td>" + rsTasks.getString("status") +
                    "</td><td>" + rsTasks.getDate("created_date") + "</td></tr>");
            }
            response.getWriter().write("</table>");
        } catch (SQLException e) {
            // Handle SQL errors during data retrieval
            System.err.println("SQL Error in doGet: " + e.getMessage());
            response.getWriter().write("Error retrieving data: " + e.getMessage());
        } finally {
            // Close ResultSet, Statement, and Connection safely
            if (rsDocs != null) {
                try {
                    rsDocs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rsTasks != null) {
                try {
                    rsTasks.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Handle POST request to add documents or tasks
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String user = (String) request.getSession().getAttribute("user");
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Connect to H2 database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            if ("upload".equals(action)) {
                // Insert new document into database
                String docName = request.getParameter("docName");
                if (docName != null && !docName.trim().isEmpty()) {
                    ps = conn.prepareStatement("INSERT INTO documents (name, uploaded_by, upload_date) VALUES (?, ?, ?)");
                    ps.setString(1, docName);
                    ps.setString(2, user);
                    ps.setDate(3, new java.sql.Date(new Date().getTime()));
                    ps.executeUpdate();
                }
            } else if ("assign".equals(action)) {
                // Insert new task into database
                String taskName = request.getParameter("taskName");
                String assignedTo = request.getParameter("assignedTo");
                if (taskName != null && !taskName.trim().isEmpty() && assignedTo != null && !assignedTo.trim().isEmpty()) {
                    ps = conn.prepareStatement("INSERT INTO tasks (name, assigned_to, status, created_date) VALUES (?, ?, ?, ?)");
                    ps.setString(1, taskName);
                    ps.setString(2, assignedTo);
                    ps.setString(3, "open"); // Default status
                    ps.setDate(4, new java.sql.Date(new Date().getTime()));
                    ps.executeUpdate();
                }
            }
            // Refresh list after adding
            doGet(request, response);
        } catch (SQLException e) {
            // Handle SQL errors during data insertion
            System.err.println("SQL Error in doPost: " + e.getMessage());
            response.getWriter().write("Error adding data: " + e.getMessage());
        } finally {
            // Close PreparedStatement and Connection safely
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}