<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <style>
  </style>

  <script type="text/javascript">
    function post()
    {
      let typeForm	=	document.getElementById("login");


      typeForm.method="post";
      typeForm.action="./loginctrl";
      //typeForm.target = self;
      typeForm.submit();
    }

  </script>

</head>
<body>
<h3> 회원가입</h3>

<form id="login">
  아이디 : <input type="text" name="id"><br/>
  비밀번호 : <input type="password" name="pw"><br/>

  <input type="submit" value="로그인"><br/>


</form>

</body>
</html>
