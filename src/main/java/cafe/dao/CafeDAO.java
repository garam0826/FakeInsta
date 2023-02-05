package cafe.dao;

import cafe.model.CafeDTO;
import db.util.DBUtil;

import java.util.List;

public class CafeDAO
{
    //CRUD
    public int create(DBUtil dbUtil, String custumer, String drik, int watingNumber)
    {
        return 0;
    }

    public int update(DBUtil dbUtil)
    {
        return 0;
    }

    public List<CafeDTO> read(DBUtil dbUtil, String strWhere)
    {
        String strQuery =   "select * from cafe ";
        if(strWhere != null && !"".equals(strWhere))
        {
            strQuery += strWhere;
        }
        return dbUtil.select(strQuery, CafeDTO.class);
    }

    public  int delete(DBUtil dbUtil)
    {
        return 0;
    }
}
