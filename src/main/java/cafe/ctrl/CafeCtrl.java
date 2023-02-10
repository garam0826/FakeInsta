package cafe.ctrl;

import cafe.dao.CafeDAO;
import cafe.model.CafeDTO;
import db.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "cafectrl", value = "/cafectrl")
public class CafeCtrl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String strRunCode   =   request.getParameter("CODE");

        if (strRunCode.equals("S01")) { //커피조회
            try {
                runS01(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    //
    private void runS01(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DBUtil<CafeDTO> dbUtil  =   null;

        try
        {
            String strCoffee    = request.getParameter("drink");
            dbUtil = new DBUtil<>();

            CafeDTO dtd =   new CafeDTO();
            dtd.setDrink(strCoffee);
            List<CafeDTO> cafeDTOList = dbUtil.select(dtd);

            /*
            CafeDAO cafeDAO =   new CafeDAO();
            List<CafeDTO> cafeDTOList = cafeDAO.read(dbUtil, "where drink='" + strCoffee + "'");
            request.setAttribute("cafeList", cafeDTOList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
            requestDispatcher.forward(request, response);
            */
            request.setAttribute("cafeList", cafeDTOList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }


    }
}
