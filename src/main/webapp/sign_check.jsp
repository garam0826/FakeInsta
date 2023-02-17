<%@ page import="member.model.MemberDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String UserId = request.getParameter("id");

  session.setAttribute("id",UserId);
  MemberDTO dto = new MemberDTO();
  dto.setId(UserId);


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