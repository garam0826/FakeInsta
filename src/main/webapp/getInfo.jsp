<%@ page import="member.model.MemberDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
    </style>
    <%

        List<Map<String, Object>> listUser = (List<Map<String, Object>>) request.getAttribute("userInfo");

        //로그인한 상태에서만 회원정보수정이 가능
        String strUser = (String) session.getAttribute("UserId");

        Map<String, Object> userInfo    =   listUser.get(0);
        String strName = (String) userInfo.get("name");
        String strSex =  (String) userInfo.get("gender");
        String strBirth =  (String) userInfo.get("birth");
        String strPhone =  (String) userInfo.get("phone");
        String strEmail =  (String) userInfo.get("email");
        String strAddress = (String) userInfo.get("address");
        String strAddr_Detail = (String) userInfo.get("addressDetail");



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
    아이디 : <input type="text" name="id" value="<%=strUser%>"><br>
    비밀번호 : <input type="password" name="pw" /> ><br>
    비밀번호확인 : <input type="password" name="pwCheck"/> <br>
    이름 : <input type ="text" name="name" value="<%=strName%>"><br>

    <% if(strSex.equals("men")){ %>
        성별 <input type = "radio" name="gender" value="men" checked> 남자
        <input type = "radio" name="gender" value="women">여자<br>
    <%}else{%>
        성별 <input type = "radio" name="gender" value="men" > 남자
        <input type = "radio" name="gender" value="women" checked>여자<br>
    <%}%>



    생년월일 : <input type="text" name="birth" value="<%=strBirth%>"><br>
    휴대전화: <input type ="text" name = "phone" value="<%=strPhone%>"><br>
    이메일: <input type = "text" name="email" value="<%=strEmail%>"><br>
    주소: <input type="text" name="address" value="<%=strAddress%>"><br>
    상세주소:<input type="text" name="addressDetail" value="<%=strAddr_Detail%>"><br>
    <input type="submit" value="회원정보수정하기"/>

</form>

</body>
</html>
