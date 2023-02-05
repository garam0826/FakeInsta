<%@ page import="cafe.model.CafeDTO" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 82105
  Date: 2023-02-05
  Time: 오전 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    List<CafeDTO> cafeDTOList = (List<CafeDTO>) request.getAttribute("cafeList");
    System.out.println("receive"+cafeDTOList);
    String strTitle="고객대기화면";


%>
<html>
<head>

    <title><%=strTitle%></title>
</head>
<body>
    <table>
        <thead>
            <tr>
                <td> 고객명</td>
                <td> 음료명</td>
                <td> 대기번호</td>

            </tr>

        </thead>

        <tbody>

                <tr>
                <%for(CafeDTO cafeDTO : cafeDTOList){ %>

                <td><%=cafeDTO.getCustomer()%></td>
                <td><%=cafeDTO.getDrink()%></td>
                <td><%=cafeDTO.getWaitingNumber()%></td>
                </tr>
                <% } %>


        </tbody>
    </table>

</body>
</html>
