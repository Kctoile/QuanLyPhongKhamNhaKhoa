<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
        Email: <input type="email" name="email" required /><br />
        Password: <input type="password" name="password" required /><br />
        <button type="submit">Login</button>
    </form>

    <p>Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp">Register</a></p>
    <p><a href="${pageContext.request.contextPath}/">Home</a></p>

    <p style="color:red">${error}</p>