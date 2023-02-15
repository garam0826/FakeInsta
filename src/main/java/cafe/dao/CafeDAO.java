package cafe.dao;

import cafe.model.Cafe;
import db.util.DBUtil;

import java.sql.SQLException;
import java.util.List;

public class CafeDAO
{
    //CRUD
    public int create(DBUtil dbUtil, String custumer, String drik, int watingNumber)
    {
        return 0;
    }

    public int updateNamewhereDrink(DBUtil dbUtil, String drink, String customer) throws SQLException {
        String updateQuery  = "UPDATE cafe SET customer  = '"+customer+"' WHERE drink = '"+drink+"' ";
        return dbUtil.update(updateQuery);
    }

    public List<Cafe> read(DBUtil dbUtil, String strWhere)
    {
        String strQuery =   "select * from cafe ";
        if(strWhere != null && !"".equals(strWhere))
        {
            strQuery += strWhere;
        }
        return dbUtil.select(strQuery, Cafe.class);
    }

    public  int delete(DBUtil dbUtil)
    {
        return 0;
    }
}
