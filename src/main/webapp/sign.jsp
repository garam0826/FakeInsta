<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <style>
  </style>

  <script type="text/javascript">
    function post()
    {
      let typeForm	=	document.getElementById("sign");


      typeForm.method="post";
      typeForm.action="./sign_check.jsp";
      //typeForm.target = self;
      typeForm.submit();
    }

  </script>

</head>
<body>
  <h3> 회원가입</h3>

<form id="sign">
  아이디 : <input type="text" name="id"><br/>
  비밀번호 : <input type="password" name="pw"><br/>
  비밀번호확인 : <input type="password" name="pwCheck"><br/>
  이름 : <input type ="text" name="name"><br/>
  성별 <input type = "radio" name="gender" value="men">남자
  <input type = "radio" name="gender" value="women">여자<br/>

  생년월일 : <input type="text" name="birth"><br/>
  휴대전화:<input type = "text" name = "phone"><br/>
  이메일: <input type = "text" name="email"><br/>
  주소: <input type="text" name="address"><br/>
  상세주소:<input type="text" name="addressDetail"><br/>
  <input type="submit" value="회원가입하기"><br/>

</form>

</body>
</html>
