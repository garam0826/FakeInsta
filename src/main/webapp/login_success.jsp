<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@page import ="member.model.MemberDTO" %>
<%
    String UserId = (String)session.getAttribute("UserId");

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>

<%=UserId %>님 안녕하세요

<a href="logout.jsp">로그아웃</a><br/>
<a href="getInfo.jsp">정보수정</a><br/>
<a href="delete_ok.jsp">회원탈퇴</a><br/>

</body>
</html>