<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String UserId = (String)session.getAttribute("UserId");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h3> <%=UserId %>님 회원정보수정 성공</h3>

<a href="login.jsp">로그인 페이지로 이동</a>
<a href="getInfo.jsp">회원정보맞는지 다시 확인</a>

</body>
</html>
