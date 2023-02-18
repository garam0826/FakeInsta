<%@ page import="cafe.model.Cafe" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: 82105
  Date: 2023-02-18
  Time: 오후 3:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%
    //request.setAttribute("data", cafeList);
    List<Cafe> cafeList = (List<Cafe>) request.getAttribute("data");
    for (Cafe data : cafeList)
    {
        out.print(data.getCustomer() + "  ");
        out.print(data.getDrink() + "  ");
        out.print(data.getWaitingNumber() + "<br/>");
    }
%>
난 select
</body>
</html>
