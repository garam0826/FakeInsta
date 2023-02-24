<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@page import ="member.model.MemberDTO" %>
<%@ page import="java.util.List" %>
<%
    String UserId = (String)session.getAttribute("UserId");
    String UserPw = (String)session.getAttribute("UserPw");

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<%
    List<MemberDTO> memberDTOList = (List<MemberDTO>) request.getAttribute("data");

    if(memberDTOList != null){
        if(memberDTOList.size()==1){
            String realPw = memberDTOList.get(0).getPw();
            if(UserPw ==realPw){
                System.out.print("로그인에 성공하셨습니다");
            }

        }
    }


%>

<%=UserId %>님 안녕하세요

<a href="logout.jsp">로그아웃</a><br/>
<a href="getInfo.jsp">정보수정</a><br/>
<a href="delete_ok.jsp">회원탈퇴</a><br/>

</body>
</html>