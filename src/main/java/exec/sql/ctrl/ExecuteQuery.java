package exec.sql.ctrl;

import cafe.model.Cafe;
import db.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "exec_sql.html", value = "/exec_sql.html")
public class ExecuteQuery extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String strQuery =   request.getParameter("query_txt");

        //if (!"".equals(strQuery))
        //{

        //}

        //if("query_txt".equals(strQuery))

        if (strQuery != null)
        {
            strQuery    = strQuery.trim(); //좌우 공백제거
            strQuery    = strQuery.replaceAll("\n", " ").replaceAll("\r", " ");
            System.out.println(strQuery);

            String strCompare = strQuery.toLowerCase();

            if (strCompare.startsWith("select"))
            {
                select(strQuery, request, response);
            }
            else if (strCompare.startsWith("insert"))
            {
                insert(strQuery, request, response);
            }
            else if (strCompare.startsWith("update"))
            {
                update(strQuery, request, response);
            }
            else if (strCompare.startsWith("delete"))
            {
                delete(strQuery, request, response);
            }
        }
        else
        {

        }


    }

    private void select(String strQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DBUtil<Cafe> dbUtil=    null;
        List<Cafe> cafeList = new ArrayList<>();
        try
        {
            dbUtil = new DBUtil<>();
            cafeList = dbUtil.select(strQuery, Cafe.class);

        }
        catch (Exception ex)
        {
            request.setAttribute("exception", ex);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/sql_exec/sql_error.jsp");
            requestDispatcher.forward(request, response);
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }

        request.setAttribute("data", cafeList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/sql_exec/select_sql.jsp");
        requestDispatcher.forward(request, response);
    }

    private void insert(String strQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DBUtil<Cafe> dbUtil=    null;
        int nResultRow  =   0;
        try
        {
            dbUtil = new DBUtil<>();

            nResultRow = dbUtil.insert(strQuery);
        }
        catch (Exception ex)
        {
            response.sendRedirect("/sql_exec/sql_error.jsp");
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }

        if (nResultRow > 0)
        {
            request.setAttribute("insert_rows", nResultRow);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/sql_exec/insert_sql.jsp");
            requestDispatcher.forward(request, response);
        }
        else {
            response.sendRedirect("/sql_exec/sql_error.jsp");
        }

    }

    private void update(String strQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DBUtil<Cafe> dbUtil=    null;
        int nResultRow  =   0;
        try
        {
            dbUtil = new DBUtil<>();

            nResultRow = dbUtil.update(strQuery);
        }
        catch (Exception ex)
        {
            response.sendRedirect("/sql_exec/sql_error.jsp");
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }

        if (nResultRow > 0)
        {
            request.setAttribute("insert_rows", nResultRow);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/sql_exec/insert_sql.jsp");
            requestDispatcher.forward(request, response);
        }
        else {
            response.sendRedirect("/sql_exec/sql_error.jsp");
        }

    }

    private void delete(String strQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DBUtil<Cafe> dbUtil=    null;
        int nResultRow  =   0;
        try
        {
            dbUtil = new DBUtil<>();

            nResultRow = dbUtil.delete(strQuery);
        }
        catch (Exception ex)
        {
            response.sendRedirect("/sql_exec/sql_error.jsp");
        }
        finally
        {
            if(dbUtil != null)
            {
                dbUtil.Close();
            }
        }

        if (nResultRow > 0)
        {
            request.setAttribute("insert_rows", nResultRow);
            request.setAttribute("type", "delete");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/sql_exec/insert_sql.jsp");
            requestDispatcher.forward(request, response);
        }
        else {
            response.sendRedirect("/sql_exec/sql_error.jsp");
        }

    }
}
