package cafe.ctrl;

import cafe.dao.CafeDAO;
import cafe.model.Cafe;
import db.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "cafectrl", value = "/cafectrl")
public class CafeCtrl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {


        String strRunCode   =   request.getParameter("CODE");

        try
        {
            switch (strRunCode)
            {
                case "S01":
                    runS01(request, response); //select - statement
                    break;
                case "S02":
                    runS02(request, response); //select - preparedstatement
                    break;
                case "I01":
                    runI01(request, response); //insert - statement
                    break;
                case "I02":
                    runI02(request, response); //insert - preparedstatement
                    break;
                case "U01":
                    runU01(request, response); //update - statement
                    break;
                case "U02":
                    runU02(request, response); //update - preparedstatement
                    break;
                case "D01":
                    runD01(request, response); //delete - statement
                    break;
                case "D02":
                    runD02(request, response); //delete - preparedstatement
                    break;
                default:
                {
                    break;
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }




    }

    //
    private void runS01(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();
                CafeDAO dao =   new CafeDAO();

                list      = dao.selectbyDrink(dbUtil, strCoffee);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);


    }

    private void runS02(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                //테이블 dto를 생성한다.
                Cafe cafeDto    =   new Cafe();
                //검색하고 싶은 값을 setter로 넣는다.
                cafeDto.setDrink(strCoffee);
                //조회
                list      = dbUtil.select(cafeDto);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }

    private void runI01(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                String strQuery =    "INSERT INTO CAFE (CUSTOMER, DRINK) ";
                strQuery        +=   "VALUES ('%s', '%s') ";

                strQuery        =   String.format(strQuery, "mark",strCoffee );
                //조회
                int nResult      = dbUtil.insert(strQuery);
            }
        }
        catch (Exception ex)
        {
            dbUtil.Rollback();
            ex.printStackTrace();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Commit();
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }

    private void runI02(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                //테이블 dto를 생성한다.
                Cafe cafeDto    =   new Cafe();
                //insert하고 싶은 데이터를 setter로 세팅한다.
                cafeDto.setDrink(strCoffee);
                cafeDto.setCustomer("melody");
                //조회
                int nResult      = dbUtil.insert(cafeDto);
            }
        }
        catch (Exception ex)
        {
            dbUtil.Rollback();
            ex.printStackTrace();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Commit();
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }


    private void runU01(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                String strQuery =    "UPDATE CAFE ";
                strQuery        +=   "SET CUSTOMER = 'orange' ";
                strQuery        +=   "WHERE DRINK= '%s' ";

                strQuery        =   String.format(strQuery, strCoffee );
                //조회
                int nResult      = dbUtil.update(strQuery);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            dbUtil.Rollback();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Commit();
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }

    private void runU02(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                //테이블 dto를 생성한다.
                Cafe cafeDto    =   new Cafe();
                //update하고 싶은 데이터를 setter로 세팅한다.
                cafeDto.setCustomer("orange2");

                //테이블 dto를 생성한다.
                Cafe cafeWhereDto  =   new Cafe();
                //조건절데이터를 setter로 세팅한다.
                cafeWhereDto.setDrink(strCoffee);
                //조회
                int nResult      = dbUtil.update(cafeDto, cafeWhereDto);
            }
        }
        catch (Exception ex)
        {
            dbUtil.Rollback();
            ex.printStackTrace();

        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Commit();
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }


    private void runD01(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                String strQuery =    "DELETE ";
                strQuery        +=   "FROM CAFE ";
                strQuery        +=   "WHERE DRINK= '%s' ";

                strQuery        =   String.format(strQuery, strCoffee );
                //조회
                int nResult      = dbUtil.update(strQuery);
            }
        }
        catch (Exception ex)
        {
            dbUtil.Rollback();
            ex.printStackTrace();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Commit();
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }

    private void runD02(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        DBUtil<Cafe> dbUtil  =   null;
        List<Cafe> list     =   new ArrayList<>();

        try
        {
            String strCoffee    = request.getParameter("drink");

            if (strCoffee != null)
            {
                dbUtil = new DBUtil<>();

                //테이블 dto를 생성한다.
                Cafe cafeWhereDto  =   new Cafe();
                //조건절데이터를 setter로 세팅한다.
                cafeWhereDto.setDrink(strCoffee);
                //조회
                int nResult      = dbUtil.delete(cafeWhereDto);
            }
        }
        catch (Exception ex)
        {
            dbUtil.Rollback();
            ex.printStackTrace();
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Commit();
                dbUtil.Close();
            }
        }

        request.setAttribute("cafeList", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/cafe/selectCafe.jsp");
        requestDispatcher.forward(request, response);
    }


}
