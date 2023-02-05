package cafe.ctrl;
import db.util.DBUtil;
import cafe.model.CafeDTO;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "cafectrl", value = "/catectrl")
public class CafeCtrl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //로직은 무조건 post, 보안이 더 좋기 떄문
        DBUtil<CafeDTO> dbUtil = null;
        System.out.println("Hello");
        System.out.println("Hello");

        PrintWriter out = response.getWriter();
        try{
            dbUtil = new DBUtil<CafeDTO>();

            List<CafeDTO> cafeDTOList = dbUtil.select("select*from cafe",CafeDTO.class);
            System.out.println("send==>"+cafeDTOList);

            //보낸값
            request.setAttribute("cafeList",cafeDTOList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
            requestDispatcher.forward(request,response);
            /*
            for(CafeDTO cafeDTO: cafeDTOList){
                out.print("customer"+ cafeDTO.getCustomer()+"<br>");
                out.print("drink"+ cafeDTO.getDrink()+"<br>");
                out.print("waitingNumber"+ cafeDTO.getWaitingNumber()+"<br>");

            }
            */

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            if(dbUtil !=null){
                dbUtil.Close();
            }

        }


    }
}
