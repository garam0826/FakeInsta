package member.dao;

import db.util.DBUtil;
import member.model.MemberDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDAO {
    public int create(DBUtil dbUtil, String id,String pw, String pwCheck
                      ,String name,String gender, String birth,
                      String phone, String email, String address,
                      String addressDetail){
        return 0;
    }

    public int update(DBUtil dbUtil){
        return 0;
    }

    public boolean selectIsMember(DBUtil dbUtil, String id, String pw){
        //List<MemberDTO> list = dbUtil.select("SELECT * FROM member WHERE id= '"+id+"'", MemberDTO.class);
        List<Map<String, Object>> list    =   dbUtil.select("SELECT * FROM member WHERE id= '"+id+"'");
        if(list != null){
            if(list.size()==1){
                String dbPw = (String) list.get(0).get("pw");
                if(pw.equals(dbPw)){
                    return true;
                }

            }
        }
        return false;
    }


    public int delete(DBUtil dbUtil){return 0;}

    public boolean pwCheckIsOk(DBUtil dbUtil, String pw, String pwCheck){
        if(pw.equals(pwCheck)){
            return true;
        }
        return false;
    }

    public boolean signNotNull(DBUtil dbUtil, String id, String pw, String name, String gender,
                               String birth, String phone){
        if(id!=null && pw!=null && name!=null && gender!=null && birth!=null && phone!=null){
            return true;
        }
        return false;
    }
    public boolean firstSign(DBUtil dbUtil, String id){
        return true;
    }


}
