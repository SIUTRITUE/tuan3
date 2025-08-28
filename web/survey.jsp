<%-- Bỏ dòng taglib JSTL --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>
<h1>Thanks for taking our survey!</h1>

<p>Here is the information that you entered:</p>

<label>Email:</label>
<span>${user.email}</span><br>

<label>First Name:</label>
<span>${user.firstName}</span><br>

<label>Last Name:</label>
<span>${user.lastName}</span><br>

<label>Date of Birth:</label>
<span>${user.dateOfBirth}</span><br>

<label>Heard From:</label>
<span>${user.heardFrom}</span><br>

<label>Updates:</label>
<span>${user.wantsUpdates}</span><br>

<label>Email OK:</label>
<span>${user.emailOK}</span><br>

<%-- Thay cho c:if: ẩn/hiện bằng điều kiện EL --%>
<div style="display:${user.wantsUpdates eq 'Yes' ? 'block' : 'none'}">
    <label>Contact Via:</label>
    <span>${user.contactVia}</span><br>
</div>
</body>
</html>
