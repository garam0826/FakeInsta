package member.ctrl;

import db.util.DBUtil;
import member.dao.MemberDAO;
import member.model.MemberDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteCtrl", value = "/DeleteCtrl")
public class DeleteCtrl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        DBUtil<MemberDTO> dbUtil= null;

        PrintWriter out= response.getWriter();
        try {
            dbUtil = new DBUtil<MemberDTO>();
            MemberDAO dao = new MemberDAO();

            String id = request.getParameter("id");
            //String pw = request.getParameter("pw");

            HttpSession session = request.getSession();

            //delete로 회원탈퇴하기
            String strQuery="DELETE FROM member WHERE id = '"+id+"'";

            delete(strQuery,request,response);



        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(dbUtil!=null){
                dbUtil.Close();
            }
        }
    }

    private void delete(String strQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        DBUtil<MemberDTO> dbUtil = null;

        try{
            dbUtil = new DBUtil<>();
            //delete 로직


        }
        catch(Exception ex){
            response.sendRedirect("delete_fail.jsp");
        }
        finally {
            if(dbUtil !=null){
                dbUtil.Close();
            }
        }
    }
}
