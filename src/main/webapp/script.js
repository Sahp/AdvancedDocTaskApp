$(document).ready(function() {
    // Load initial list on page load
    updateList();

    // Handle document upload form submission
    $("#docForm").submit(function(event) {
        event.preventDefault();
        let docName = $("#docName").val();
        $.post("/AdvancedDocTaskApp/DocumentTaskServlet", { action: "upload", docName: docName }, function() {
            updateList();
            $("#docName").val(""); // Clear input
        });
    });

    // Handle task assignment form submission
    $("#taskForm").submit(function(event) {
        event.preventDefault();
        let taskName = $("#taskName").val();
        let assignedTo = $("#assignedTo").val();
        $.post("/AdvancedDocTaskApp/DocumentTaskServlet", { action: "assign", taskName: taskName, assignedTo: assignedTo }, function() {
            updateList();
            $("#taskName").val(""); // Clear input
            $("#assignedTo").val(""); // Clear input
        });
    });

    // Function to refresh document and task lists
    function updateList() {
        $.get("/AdvancedDocTaskApp/DocumentTaskServlet", function(data) {
            $("#list").html(data);
        });
    }
});