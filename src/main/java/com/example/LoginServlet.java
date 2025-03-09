package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Handle POST request for login
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get username and password from form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Simulate authentication (replace with database check in production)
        if ("admin".equals(username) && "123".equals(password)) {
            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            // Redirect to dashboard
            response.sendRedirect("dashboard.jsp");
        } else {
            // Redirect back to login page if credentials are wrong
            response.sendRedirect("login.html");
        }
    }
}