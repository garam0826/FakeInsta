package member.ctrl;

import member.model.MemberDTO;
import db.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "login.jsp", value = "/login.jsp")
public class LoginCtrl2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String UserId = request.getParameter("id");

        if(true){

        }
    }
    private void select(String UserId, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        DBUtil<MemberDTO> dbUtil = null;
        List<MemberDTO> memberDTOList= new ArrayList<>();
        try{
            dbUtil = new DBUtil<>();
            memberDTOList=dbUtil.select(UserId,MemberDTO.class);

        }catch(Exception ex){
            request.setAttribute("exception", ex);
            RequestDispatcher rq=request.getRequestDispatcher("login_fail.jsp");
            rq.forward(request,response);

        }finally{
            if(dbUtil !=null){
                dbUtil.Close();
            }
        }
        request.setAttribute("data",memberDTOList);
        RequestDispatcher rq = request.getRequestDispatcher("login_success.jsp");
        rq.forward(request, response);

    }
}
