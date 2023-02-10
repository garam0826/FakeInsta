<%@ page import="java.util.List" %>
<%@ page import="cafe.model.CafeDTO" %>
<%--
  Created by IntelliJ IDEA.
  User: jeong-wonjin
  Date: 2023/02/05
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<CafeDTO> cafeDTOList   =   (List<CafeDTO>) request.getAttribute("cafeList");
    String strTitle =   "고객대기화면";
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <table>
        <thead>
            <tr>
                <td>고객명</td>
                <td>음료명</td>
                <td>대기번호</td>
            </tr>
        </thead>
        <tbody>

                <% for (CafeDTO cafeDTO:cafeDTOList) { %>
                <tr>
                    <td><%=cafeDTO.getCustomer() %></td>
                    <td><%=cafeDTO.getDrink() %></td>
                    <td><%=cafeDTO.getWaitingNumber() %></td>
                </tr>
                <% } %>

        </tbody>

    </table>

</body>
</html>
