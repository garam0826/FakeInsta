package member.ctrl;

import db.util.DBUtil;
import member.dao.MemberDAO;
import member.model.MemberDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ModifyCtrl", value = "/ModifyCtrl")
public class ModifyCtrl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            //update로 쿼리문 수정하기
            String strQuery= "UPDATE member set pw ='"+ pw +"'," +
                    "pwCheck =' "+ pwCheck.trim() +"',"+
                    "name ='"+ name.trim() +" ',"+
                    "gender ='"+ gender.trim() +" ', "+
                    "birth ='"+ birth.trim() +"', "+
                    "phone ='"+ phone.trim() +"', "+
                    "email ='"+ email.trim() +"', "+
                    "address ='"+ address.trim() +" ', "+
                    "addressDetail ='"+ addressDetail.trim() +"' WHERE id='"+ id +"'";


            if(dao.pwCheckIsOk(dbUtil, pw, pwCheck)&& dao.signNotNull(dbUtil, id, pw,
                    name,gender,birth,phone)){

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

                request.setAttribute("UserId", id);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("modify_success.jsp");
                requestDispatcher.forward(request, response);

                //update 하기
                dbUtil.update(strQuery);




            }
            else{
                response.sendRedirect("modify_fail.jsp");

            }


        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(dbUtil!=null){
                dbUtil.Close();
            }
        }

    }

    private void update(String strQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        DBUtil<MemberDTO> dbUtil = null;

        try{
            dbUtil = new DBUtil<>();

        }
        catch(Exception ex){
            response.sendRedirect("modify_fail.jsp");
        }
        finally {
            if(dbUtil !=null){
                dbUtil.Close();
            }
        }

    }
}
