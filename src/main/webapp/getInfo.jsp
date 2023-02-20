//select
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원정보조회</title>
</head>
<body>
<%

    List<Cafe> cafeList = (List<Cafe>) request.getAttribute("data");
    for (Cafe data : cafeList)
    {
        out.print(data.getCustomer() + "  ");
        out.print(data.getDrink() + "  ");
        out.print(data.getWaitingNumber() + "<br/>");
    }
%>

</body>
</html>
