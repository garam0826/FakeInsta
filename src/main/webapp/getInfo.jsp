<%@ page import="member.model.MemberDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
    </style>
    <%
        //로그인한 상태에서만 회원정보수정이 가능
        String a = (String) session.getAttribute("UserId");
        String b = "1234";
        String c = "1234";
        String d = "temp";
        String e = "women";
        String f = "temp";
        String g = "1234";
        String h = "temp";
        String i = "temp";
        String j = "temp";



        //저 아이디에 대한 list값 불러오기(함수만들기)
        /*

        String d = (String) session.getAttribute("name");
        String e = (String) session.getAttribute("gender");
        String f = (String) session.getAttribute("brith");
        String g = (String) session.getAttribute("phone");
        String h = (String) session.getAttribute("email");
        String i = (String) session.getAttribute("address");
        String j = (String) session.getAttribute("addressDetail");*/


    %>
    <script type="text/javascript">
        function post()
        {
            let typeForm	=	document.getElementById("modify");
            typeForm.submit();
        }

    </script>

</head>
<body>
<h3> 회원정보수정</h3>

<form id="modify" method="post" action="./ModifyCtrl" target="_self">
    아이디 : <input type="text" name="id" value="<%=a%>"><br>
    비밀번호 : <input type="password" name="pw" value="<%=b%>" ><br>
    비밀번호확인 : <input type="password" name="pwCheck" value="<%=c%>"><br>
    이름 : <input type ="text" name="name" value="<%=d%>"><br>

    <% if(e.equals("men")){ %>
        성별 <input type = "radio" name="gender" value="men" checked> 남자
        <input type = "radio" name="gender" value="women">여자<br>
    <%}else{%>
        성별 <input type = "radio" name="gender" value="men" > 남자
        <input type = "radio" name="gender" value="women" checked>여자<br>
    <%}%>



    생년월일 : <input type="text" name="birth" value="<%=f%>"><br>
    휴대전화: <input type ="text" name = "phone" value="<%=g%>"><br>
    이메일: <input type = "text" name="email" value="<%=h%>"><br>
    주소: <input type="text" name="address" value="<%=i%>"><br>
    상세주소:<input type="text" name="addressDetail" value="<%=j%>"><br>
    <input type="submit" value="회원정보수정하기"/>

</form>

</body>
</html>
