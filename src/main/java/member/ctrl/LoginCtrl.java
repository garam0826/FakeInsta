package member.ctrl;

import db.util.DBUtil;
import member.dao.MemberDAO;
import member.model.MemberDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="loginCtrl", value="/loginctrl")
public class LoginCtrl extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        DBUtil<MemberDTO> dbUtil= null;

        PrintWriter out= response.getWriter();
        try {
            dbUtil = new DBUtil<MemberDTO>();
            MemberDAO dao = new MemberDAO();

            String id = request.getParameter("id");
            String pw = request.getParameter("pw");

            HttpSession session = request.getSession();

            if(dao.selectIsMember(dbUtil,id, pw)){
                session.setAttribute("UserId",id);

                request.setAttribute("UserId", id);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("login_success.jsp");
                requestDispatcher.forward(request, response);
            }
            else{
                response.sendRedirect("login_fail.jsp");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(dbUtil!=null){
                dbUtil.Close();
            }
        }
        }

}
