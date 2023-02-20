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

@WebServlet(name="signctrl", value="/signctrl")
public class SignCtrl extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        DBUtil<MemberDTO> dbUtil=null;

        PrintWriter out=response.getWriter();
        try{
            dbUtil= new DBUtil<MemberDTO>();
            MemberDAO dao = new MemberDAO();

            String id = request.getParameter("id");
            String pw = request.getParameter("pw");
            String pwCheck = request.getParameter("pwCheck");
            String name = request.getParameter("name");
            String gender= request.getParameter("gender");
            String birth = request.getParameter("birth");
            String phone= request.getParameter("phone");
            String email=request.getParameter("email");
            String address=request.getParameter("address");
            String addressDetail=request.getParameter("addressDetail");

            HttpSession session = request.getSession();

            if(dao.pwCheckIsOk(dbUtil, pw, pwCheck)){

                MemberDTO dto = new MemberDTO();
                dto.setId(id);
                dto.setPw(pw);
                dto.setPwCheck(pwCheck);
                dto.setName(name);
                dto.setGender(gender);
                dto.setBirth(birth);
                dto.setPhone(phone);
                dto.setEmail(email);
                dto.setAddress(address);
                dto.setAddressDetail(addressDetail);

                //insert 하기

                session.setAttribute("UserId",id);
                session.setAttribute("UserName",name);


                request.setAttribute("UserId", id);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("sign_success.jsp");
                requestDispatcher.forward(request, response);

            }
            else{
                response.sendRedirect("sign_fail.jsp");

            }


        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(dbUtil!=null){
                dbUtil.Close();
            }
        }

    }
}
