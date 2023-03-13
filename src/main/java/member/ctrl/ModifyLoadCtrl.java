package member.ctrl;

import db.util.DBUtil;
import member.dao.MemberDAO;
import member.model.Member;
import member.model.MemberDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.nimbus.State;
import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@WebServlet(name="modifyLoad", value="/modifyLoad")
public class ModifyLoadCtrl extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        DBUtil<Member> dbUtil= null;

        PrintWriter out= response.getWriter();
        try {
            dbUtil = new DBUtil<Member>();

            HttpSession session = request.getSession();
            //로그인한 상태에서만 회원정보수정이 가능
            String strUserId = (String) session.getAttribute("UserId");

            /* 학교
            String strQuery =   "select * from member where id = ? and name = ?";
           // String strQuery =   "update member set pw=? where id=?";

            Connection con = dbUtil.getConn();
            PreparedStatement pstmt =con.prepareStatement(strQuery);
            pstmt.setString(1, strUserId);
            pstmt.setString(2, "garam");

            ResultSet rs = pstmt.executeQuery();
            //String strQuery =   "select * from member where id = '"+strUserId+"'";

            //List<Map<String, Object>> listUser  = dbUtil.select(strQuery);

            while (rs.next())
            {
                String strID = rs.getString("id");
            }
            */
            Member member = new Member();
            member.setId(strUserId);
            List<Member> rs = dbUtil.select(member);


            request.setAttribute("userInfo", rs);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("getInfo.jsp");
            requestDispatcher.forward(request, response);

        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(dbUtil!=null){
                dbUtil.Close();
            }
        }
        }



}
