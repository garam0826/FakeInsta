<%--
  Created by IntelliJ IDEA.
  User: 82105
  Date: 2023-02-18
  Time: 오후 4:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%
    String strType = (String) request.getAttribute("type");
    //request.setAttribute("insert_rows", nResultRow);
    int nResultRow =  (int)request.getAttribute("insert_rows");

    switch (strType)
    {
        case "delete" :
        {
            out.print(nResultRow +"건 데이터 삭제를 성공했습니다.");
            break;
        }
        case "update" :
        {
            out.print(nResultRow +"건 데이터 수정을 성공했습니다.");
            break;
        }
        default:
        {
            out.print(nResultRow +"건 데이터 삽입을 성공했습니다.");
            break;
        }

    }

%>
</body>
</html>
