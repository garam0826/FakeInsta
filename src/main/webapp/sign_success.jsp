<%--
  Created by IntelliJ IDEA.
  User: 82105
  Date: 2023-02-20
  Time: 오후 4:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String UserId = (String)session.getAttribute("UserId");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h3> <%=UserId %>님 회원가입 성공</h3>

<a href="login.jsp">로그인 페이지로 이동</a>

</body>
</html>
