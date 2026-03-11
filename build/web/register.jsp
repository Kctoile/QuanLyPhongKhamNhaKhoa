<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <title>Register</title>

        <link rel="stylesheet" href="css/register.css">

    </head>

    <body>

        <div class="auth-container">
            <div class="auth-card">

                <h2>Register Account</h2>

                <form class="auth-form" action="${pageContext.request.contextPath}/RegisterServlet" method="post">

                    Full Name:
                    <input type="text" name="fullName" required /><br />

                    Email:
                    <input type="email" name="email" required /><br />

                    Password:
                    <input type="password" name="password" required /><br />

                    Phone Number:
                    <input type="text" name="phone" required /><br />

                    Gender:
                    <select name="gender">
                        <option value="M">Nam</option>
                        <option value="F">Nữ</option>
                    </select><br />

                    Date of Birth:
                    <input type="date" name="dob" /><br />

                    Address:
                    <input type="text" name="address" /><br />

                    <button type="submit">Register</button>

                </form>

                <p class="error">${error}</p>
                <p class="success">${message}</p>

                <p>Already have an account?
                    <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
                </p>

                <p>
                    <a href="${pageContext.request.contextPath}/">Home</a>
                </p>

            </div>
        </div>

    </body>
</html>