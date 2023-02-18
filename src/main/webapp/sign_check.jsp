<%@ page import="member.model.MemberDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String UserId = request.getParameter("id");
  String UserName = request.getParameter("name");

  //session.setAttribute("id",UserId);

  //session.getAttribute("id");
  MemberDTO dto = new MemberDTO();
  dto.setId(UserId);
  dto.setName(UserName);


%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h3>입력된 정보를 확인하세요.</h3>

    <%=dto.getId() %>
    <%=dto.getPw()%>
    <%=dto.getName()%>




</body>
</html>