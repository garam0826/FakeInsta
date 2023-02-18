<%--
  Created by IntelliJ IDEA.
  User: 82105
  Date: 2023-02-18
  Time: 오후 4:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
쿼리 수행중 에러가 발생했습니다.<br/>
<%
    //request.setAttribute("exception", ex);
    Exception ex = (Exception) request.getAttribute("exception");
    if(ex != null)
    {
        ex.printStackTrace();
    }

%>
</body>
</html>
