package db.util;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.google.gson.Gson;

public class DBUtil <T>
{
    private Connection conn = null;
    private Statement stmt	=	null;

    private String m_strStatementType =   "normal";

    public DBUtil ()
    {
        Properties properties = new Properties();

        try{
            InputStream is = this.getClass().getResourceAsStream("/conf.properties");
            properties.load(is);


            // 1. 드라이버 로딩
            // 드라이버 인터페이스를 구현한 클래스를 로딩
            // mysql, oracle 등 각 벤더사 마다 클래스 이름이 다르다.
            // mysql은 "com.mysql.jdbc.Driver"이며, 이는 외우는 것이 아니라 구글링하면 된다.
            // 참고로 이전에 연동했던 jar 파일을 보면 com.mysql.jdbc 패키지에 Driver 라는 클래스가 있다.
            Class.forName(properties.getProperty("driver"));

            // 2. 연결하기
            // 드라이버 매니저에게 Connection 객체를 달라고 요청한다.
            // Connection을 얻기 위해 필요한 url 역시, 벤더사마다 다르다.
            // mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
            String url = properties.getProperty("url");

            // @param  getConnection(url, userName, password);
            // @return Connection
            conn = DriverManager.getConnection(url, properties.getProperty("user_id"), properties.getProperty("password"));

            String strStatementType = properties.getProperty("statement_type");
            this.m_strStatementType = strStatementType == null ? this.m_strStatementType : strStatementType;

            if("normal".equalsIgnoreCase(this.m_strStatementType) )
            {
                stmt = conn.createStatement();
            }


            System.out.println("Conection success!!");

        }
        catch(ClassNotFoundException e){
            System.out.println("Driver Load fail");
        }
        catch(SQLException e){
            System.out.println("Error: " + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int update( String strQuery ) throws SQLException
    {
        int nResult	=	-1;

        conn.setAutoCommit(false);
        if(stmt != null)
        {
            //수정된 로우개수 리턴
            nResult = stmt.executeUpdate(strQuery);
        }

        return nResult;

    }

    private int update( String strQuery, List<Object> valueList) throws SQLException
    {
        int nResult	=	-1;
        PreparedStatement pstmt = null;
        conn.setAutoCommit(false);
        try
        {
            pstmt =   conn.prepareStatement(strQuery);
            int i = 1;
            for (Object value : valueList)
            {
                pstmt.setObject(i, value);
                i++;
            }

            nResult = pstmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(pstmt!= null) pstmt.close();
        }

        return nResult;

    }

    public int insert( String strQuery ) throws SQLException
    {
        return update(strQuery);
    }


    public int delete( String strQuery ) throws SQLException
    {
        return update(strQuery);
    }

    public List<Map<String, Object>> select(String strQuery)
    {
        ResultSet rs	=	null;
        List<Map<String, Object>> selectList	=	new ArrayList<Map<String, Object>>();

        try
        {
            if(stmt == null )
            {
                stmt = this.conn.createStatement();
            }

            rs = stmt.executeQuery(strQuery);
			/*학교버전
			while(rs.next())
			{
				String a	=	rs.getString("id");
			}
			*/

            ResultSetMetaData rsmd = rs.getMetaData();
            while( rs.next() )
            {
                Map<String, Object> rsMap	=	new HashMap<String, Object>();

                for(int i=1; i<=rsmd.getColumnCount(); i++)
                {
                    rsMap.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                selectList.add(rsMap);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e);
                }
            }
        }

        return selectList;
    }


    public List<T> select(String strQuery, Class<T> clazz)
    {
        List<T> selectList	=	new ArrayList<>();

        try
        {
            Constructor<T> constructor = clazz.getConstructor();
            Method[] method =	clazz.getDeclaredMethods();
            selectList =   getResultList(strQuery, constructor, method, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return selectList;
    }


    public List<T> select(T tClass)
    {
        StringBuffer strbufQuery   =   new StringBuffer();
        List<T> selectList	=	new ArrayList<>();
        List<Object> valueList  =   new ArrayList<>();

        StringBuffer strbufWhere = new StringBuffer();

        try
        {
            Class clazz           =     tClass.getClass();
            String strClassName   =     clazz.getName();
            String strTableName   =     strClassName.substring( strClassName.lastIndexOf(".")+1 );

            strbufQuery.append("SELECT * FROM ");
            strbufQuery.append(strTableName);
            strbufQuery.append(" WHERE ");

            Method[] method =	clazz.getDeclaredMethods();

            for (Method value : method) {
                String strMethodName = value.getName().toLowerCase();
                if (strMethodName.startsWith("get")) {
                    String strColNm = strMethodName.substring(3);

                    if (value.getReturnType().getName().toLowerCase().contains("string")) {
                        String strValue = (String) value.invoke(tClass);
                        if (strValue != null)
                        {
                            if("normal".equalsIgnoreCase(this.m_strStatementType))
                            {
                                if(!strbufWhere.isEmpty())
                                {
                                    strbufWhere.append(" AND ");
                                }
                                strbufWhere.append(String.format(" %s='%s'", strColNm, strValue));
                            }
                            else
                            {
                                strbufWhere.append(String.format(" %s= ?", strColNm));
                                valueList.add(strValue);
                            }

                        }

                    } else {
                        Object ObjValue = value.invoke(tClass);
                        if (ObjValue != null) {
                            // BigDecimal bnValue  =   new BigDecimal( ObjValue.toString() );
                            // if( bnValue.compareTo( new BigDecimal("-999999999") ) != 0)
                            {
                                if(!strbufWhere.isEmpty())
                                {
                                    strbufWhere.append(" AND ");
                                }

                                if("normal".equalsIgnoreCase(this.m_strStatementType))
                                {
                                    strbufWhere.append(String.format(" %s=", strColNm));
                                    strbufWhere.append(ObjValue);
                                }
                                else
                                {
                                    strbufWhere.append(String.format(" %s= ?", strColNm));
                                    valueList.add(ObjValue);
                                }

                            }
                        }
                    }
                }
            }

            Constructor<T> constructor = clazz.getConstructor();
            strbufQuery.append(strbufWhere);
            Method[] setMethod =	clazz.getDeclaredMethods();
            selectList =   getResultList(strbufQuery.toString(), constructor, setMethod, valueList);



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return selectList;
    }

    private List<T> getResultList(String strQuery, Constructor<T> constructor, Method[] method, List<Object> valueList)
    {
        ResultSet rs	=	null;

        List<T> selectList	=	new ArrayList<>();
        PreparedStatement pstmt =   null;
        try
        {
            if ("normal".equalsIgnoreCase(this.m_strStatementType))
            {
                if (stmt == null)
                {
                    stmt = this.conn.createStatement();
                }
                rs = stmt.executeQuery(strQuery);
            }
            else {
                pstmt = conn.prepareStatement(strQuery);
                if(valueList != null)
                {
                    int i = 1;
                    for (Object value : valueList)
                    {
                        pstmt.setObject(i, value);
                        i++;
                    }
                }

                rs = pstmt.executeQuery();
            }


            ResultSetMetaData rsmd = rs.getMetaData();

            while( rs.next() )
            {
                T node = constructor.newInstance();

                for(int j=1; j<=rsmd.getColumnCount(); j++)
                {
                    String strColName	=	rsmd.getColumnName(j);

                    for (Method value : method) {
                        String strMethodName = value.getName().toLowerCase();

                        if (strMethodName.startsWith("set")) {
                            String strMethodColName = strMethodName.substring(3);

                            if (strMethodColName.equalsIgnoreCase(strColName)) {
                                Object objValue = rs.getObject(j);
                                System.out.println(strMethodName);
                                value.invoke(node, objValue);
                                break;
                            }
                        }

                        //.put(rsmd.getColumnName(i), rs.getObject(i));
                    }


                }

                selectList.add(node);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                System.out.println(e);
            }
        }

        return  selectList;
    }


    public int insert(T tClass)
    {
        StringBuffer strbufQuery   =   new StringBuffer();

        int nResulRow   =   -1;

        try
        {
            Class clazz           =     tClass.getClass();
            String strClassName   =     clazz.getName();
            String strTableName   =     strClassName.substring( strClassName.lastIndexOf(".")+1 );

            strbufQuery.append("INSERT INTO ");
            strbufQuery.append(strTableName);
            strbufQuery.append("(%s) VALUES (%s)");

            StringBuffer strbufColumn   =   new StringBuffer();
            StringBuffer strbufValue   =   new StringBuffer();

            List<Object> valueList      =   new ArrayList<>();

            Method[] method =	clazz.getDeclaredMethods();

            for (Method value : method) {
                String strMethodName = value.getName().toLowerCase();
                if (strMethodName.startsWith("get")) {
                    String strColNm = strMethodName.substring(3);


                    if ("normal".equalsIgnoreCase(this.m_strStatementType))
                    {
                        if (value.getReturnType().getName().toLowerCase().contains("string")) {
                            String strValue = (String) value.invoke(tClass);
                            if (strValue != null) {
                                if (strbufColumn.isEmpty())
                                {
                                    strbufColumn.append(strColNm);
                                }
                                else
                                {
                                    strbufColumn.append(", ");
                                    strbufColumn.append(strColNm);
                                }

                                if (strbufValue.isEmpty())
                                {
                                    strbufValue.append("'");
                                    strbufValue.append(strValue);
                                    strbufValue.append("'");
                                }
                                else
                                {
                                    strbufValue.append(", ");
                                    strbufValue.append("'");
                                    strbufValue.append(strValue);
                                    strbufValue.append("'");
                                }
                            }

                        } else {
                            Object ObjValue = value.invoke(tClass);
                            if (ObjValue != null) {
                                if (strbufColumn.isEmpty())
                                {
                                    strbufColumn.append(strColNm);
                                }
                                else
                                {
                                    strbufColumn.append(", ");
                                    strbufColumn.append(strColNm);
                                }
                                if (strbufValue.isEmpty())
                                {
                                    strbufValue.append(ObjValue);
                                }
                                else
                                {
                                    strbufValue.append(", ");
                                    strbufValue.append(ObjValue);
                                }

                            }
                        }
                    }
                    else
                    {

                        String objValue = (String) value.invoke(tClass);

                        if(objValue != null)
                        {
                            if (strbufColumn.isEmpty())
                            {
                                strbufColumn.append(strColNm);
                            }
                            else
                            {
                                strbufColumn.append(", ");
                                strbufColumn.append(strColNm);
                            }

                            if (strbufValue.isEmpty())
                            {
                                strbufValue.append("?");
                            }
                            else
                            {
                                strbufValue.append(", ?");
                            }
                            valueList.add(objValue);
                        }
                    }

                }
            }

            String strInsert    =  String.format(strbufQuery.toString(), strbufColumn, strbufValue);

            if ("normal".equalsIgnoreCase(this.m_strStatementType))
            {
                nResulRow =   insert(strInsert);
            }
            else {
                nResulRow =   update( strInsert, valueList );
            }




        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return nResulRow;
    }

    public int update(T updateClass, T whereClass)
    {
        StringBuffer strbufQuery    =   new StringBuffer();
        StringBuffer strbufUpdate   =   new StringBuffer();
        StringBuffer strbufWhere    =   new StringBuffer();

        List<Object> valueList      =   new ArrayList<>();

        int nResulRow   =   -1;

        try
        {
            Class clazz           =     whereClass.getClass();
            String strClassName   =     clazz.getName();
            String strTableName   =     strClassName.substring( strClassName.lastIndexOf(".")+1 );

            strbufQuery.append("UPDATE ");
            strbufQuery.append(strTableName);
            strbufQuery.append(" SET %s WHERE %s ");

            Class updateClazz           =     updateClass.getClass();
            Method[] updateMethods =	updateClazz.getDeclaredMethods();

            for (Method updateMethod : updateMethods) {
                String strMethodName = updateMethod.getName().toLowerCase();
                if (strMethodName.startsWith("get")) {
                    String strColNm = strMethodName.substring(3);

                    if (updateMethod.getReturnType().getName().toLowerCase().contains("string")) {
                        String strValue = (String) updateMethod.invoke(updateClass);
                        if (strValue != null) {

                            if ("normal".equalsIgnoreCase(this.m_strStatementType))
                            {
                                if (!strbufUpdate.isEmpty() )
                                {
                                    strbufUpdate.append(", ");
                                }
                                strbufUpdate.append(String.format(" %s='%s'", strColNm, strValue));
                            }
                            else {
                                if (!strbufUpdate.isEmpty() )
                                {
                                    strbufUpdate.append(", ");
                                }
                                strbufUpdate.append(String.format(" %s=?", strColNm));

                                valueList.add(strValue);
                            }

                        }

                    } else {
                        Object ObjValue = updateMethod.invoke(updateClass);
                        if (ObjValue != null) {
                            if ("normal".equalsIgnoreCase(this.m_strStatementType))
                            {
                                if (!strbufUpdate.isEmpty() )
                                {
                                    strbufUpdate.append(", ");
                                }

                                strbufUpdate.append(String.format(" %s=", strColNm));
                                strbufUpdate.append(ObjValue);
                            }
                            else {
                                if (!strbufUpdate.isEmpty() )
                                {
                                    strbufUpdate.append(", ");
                                }
                                strbufUpdate.append(String.format(" %s=?", strColNm));

                                valueList.add(ObjValue);
                            }


                        }
                    }
                }
            }

            Method[] whereMethods =	clazz.getDeclaredMethods();

            for (Method whereMethod : whereMethods) {
                String strMethodName = whereMethod.getName().toLowerCase();
                if (strMethodName.startsWith("get")) {
                    String strColNm = strMethodName.substring(3);

                    if (whereMethod.getReturnType().getName().toLowerCase().contains("string")) {
                        String strValue = (String) whereMethod.invoke(whereClass);
                        if (strValue != null) {

                            if ("normal".equalsIgnoreCase(this.m_strStatementType))
                            {
                                if (!strbufWhere.isEmpty())
                                {
                                    strbufWhere.append(" AND ");
                                }
                                strbufWhere.append(String.format(" %s='%s'", strColNm, strValue));
                            }
                            else
                            {
                                if (!strbufWhere.isEmpty())
                                {
                                    strbufWhere.append(" AND ");
                                }
                                strbufWhere.append(String.format(" %s=?", strColNm));
                                valueList.add(strValue);
                            }

                        }

                    } else {
                        Object ObjValue = whereMethod.invoke(whereClass);
                        if (ObjValue != null) {
                            {
                                if ("normal".equalsIgnoreCase(this.m_strStatementType))
                                {
                                    if (!strbufWhere.isEmpty())
                                    {
                                        strbufWhere.append(" AND ");
                                    }
                                    strbufWhere.append(String.format(" %s=", strColNm));
                                    strbufWhere.append(ObjValue);
                                }
                                else
                                {
                                    if (!strbufWhere.isEmpty())
                                    {
                                        strbufWhere.append(" AND ");
                                    }
                                    strbufWhere.append(String.format(" %s=?", strColNm));
                                    valueList.add(ObjValue);
                                }

                            }
                        }
                    }
                }
            }

            String strUpdate = String.format(strbufQuery.toString(), strbufUpdate, strbufWhere);
            if ("normal".equalsIgnoreCase(this.m_strStatementType))
            {
                nResulRow =   update( strUpdate );
            }
            else
            {
                nResulRow =   update( strUpdate, valueList );
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return nResulRow;
    }

    public int delete(T tClass)
    {
        StringBuffer strbufQuery   =   new StringBuffer();
        StringBuffer strbufWhere   =   new StringBuffer();

        List<Object> valueList      =   new ArrayList<>();

        int nResulRow   =   -1;

        try
        {
            Class clazz           =     tClass.getClass();
            String strClassName   =     clazz.getName();
            String strTableName   =     strClassName.substring( strClassName.lastIndexOf(".")+1 );
            strbufQuery.append("DELETE FROM ");
            strbufQuery.append(strTableName);
            strbufQuery.append(" WHERE  ");

            Method[] method =	clazz.getDeclaredMethods();

            for (Method value : method) {
                String strMethodName = value.getName().toLowerCase();
                if (strMethodName.startsWith("get")) {
                    String strColNm = strMethodName.substring(3);

                    if ("normal".equalsIgnoreCase(this.m_strStatementType))
                    {
                        if (value.getReturnType().getName().toLowerCase().contains("string")) {
                            String strValue = (String) value.invoke(tClass);
                            if (strValue != null) {

                                if(!strbufWhere.isEmpty())
                                {
                                    strbufWhere.append( " AND " );
                                }
                                strbufWhere.append(String.format(" %s='%s'", strColNm, strValue));
                            }
                        } else {
                            Object ObjValue = value.invoke(tClass);
                            if (ObjValue != null) {
                                {
                                    if(!strbufWhere.isEmpty())
                                    {
                                        strbufWhere.append( " AND " );
                                    }

                                    strbufWhere.append(String.format(" %s=", strColNm));
                                    strbufWhere.append(ObjValue);
                                }
                            }
                        }
                    }
                    else
                    {
                        Object ObjValue = value.invoke(tClass);
                        if (ObjValue != null) {
                            {
                                if(!strbufWhere.isEmpty())
                                {
                                    strbufWhere.append( " AND " );
                                }

                                strbufWhere.append(String.format(" %s=?", strColNm));
                                valueList.add(ObjValue);
                            }
                        }


                    }

                }
            }

            strbufQuery.append(strbufWhere);
            if ("normal".equalsIgnoreCase(this.m_strStatementType))
            {
                nResulRow =   delete(strbufQuery.toString());
            }
            else
            {
                nResulRow =   update(strbufQuery.toString(), valueList);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return nResulRow;
    }






    public Connection getConnection()
    {
        return this.conn;
    }

    public void Commit() throws SQLException
    {
        conn.commit();
    }

    public void Rollback() throws SQLException
    {
        conn.rollback();
    }

    public void Close()
    {
        try{
            if(!conn.getAutoCommit())
            {
                conn.setAutoCommit(true);
            }
            if(stmt!=null)
            {
                stmt.close();
            }

            if( conn != null && !conn.isClosed()){
                conn.close();
                System.out.println("Driver Closed");
            }
        }
        catch( SQLException e){
            System.out.println("Error: " + e);
        }
    }

}

