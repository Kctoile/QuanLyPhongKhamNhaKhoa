<form action="${pageContext.request.contextPath}/LoginServlet" method="post">
    Email: <input type="text" name="email"/><br/>
    Password: <input type="password" name="matKhau"/><br/>
    <button type="submit">Login</button>
</form>

<p style="color:red">${error}</p>