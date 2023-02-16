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


public class DBUtil <T>
{
    private Connection conn = null;
    private Statement stmt	=	null;

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

            stmt = conn.createStatement();

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

        conn.setAutoCommit(false);
        if(stmt == null)
        {
            stmt = conn.createStatement();
        }
        //수정된 로우개수 리턴
        int nResult = stmt.executeUpdate(strQuery);

        return nResult;

    }

    private int update( String strQuery, List<Object> valueList) throws SQLException
    {
        int nResult	=	-1;
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(strQuery)) {
            int i = 1;
            for (Object value : valueList) {
                pstmt.setObject(i, value);
                i++;
            }

            nResult = pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
        ArrayList<Map<String, Object>> selectList	=	new ArrayList<Map<String, Object>>();

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
                HashMap<String, Object> rsMap	=	new HashMap<String, Object>();

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
                    e.printStackTrace();
                }
            }
        }

        return selectList;
    }


    public List<T> select(String strQuery, Class<T> clazz)
    {
        List<T> selectList	=	new ArrayList<>();
        ResultSet rs	            =	null;

        try
        {
            if (stmt == null)
            {
                stmt = this.conn.createStatement();
            }
            rs = stmt.executeQuery(strQuery);

            selectList =  getResultSet(rs, clazz);
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
                if(stmt != null) stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return selectList;
    }


    public List<T> select(T tClass)
    {
        StringBuffer strbufQuery    =   new StringBuffer();
        List<T> selectList	        =	new ArrayList<>();
        List<Object> valueList      =   new ArrayList<>();
        StringBuffer strbufWhere    =   new StringBuffer();
        ResultSet rs	            =	null;
        PreparedStatement pstmt     =   null;

        try
        {
            Class clazz           =     tClass.getClass();
            String strClassName   =     clazz.getName();
            String strTableName   =     strClassName.substring( strClassName.lastIndexOf(".")+1 );

            strbufQuery.append("SELECT * FROM ");
            strbufQuery.append(strTableName);
            strbufQuery.append(" WHERE ");

            Method[] whereMethods =	clazz.getDeclaredMethods();
            //메소드 리스트를 불러온다
            for (Method whereMethod : whereMethods)
            {
                String strMethodName = whereMethod.getName().toLowerCase();
                //값이 담긴게 있는지 getter만 체크한다
                if (strMethodName.startsWith("get"))
                {
                    //컬럼명을 추출한다.
                    String strColNm = strMethodName.substring(3);
                    //함수를 실행한다.
                    Object ObjValue = whereMethod.invoke(tClass);
                    //null체크
                    if (ObjValue != null)
                    {
                        //null이 아니면 쿼리를 만든다
                        if(!strbufWhere.isEmpty())
                        {
                            strbufWhere.append(" AND ");
                        }
                        strbufWhere.append(String.format(" %s = ?", strColNm));
                        valueList.add(ObjValue);
                    }
                }
            }

            //쿼리완성
            strbufQuery.append(strbufWhere);

            //preparedstatement에 쿼리 삽입
            pstmt = conn.prepareStatement(strbufQuery.toString());
            //where절 데이터 삽입
            if(valueList != null)
            {
                int i = 1;
                for (Object value : valueList)
                {
                    pstmt.setObject(i, value);
                    i++;
                }
            }

            //쿼리 실행
            rs = pstmt.executeQuery();
            //결과값 가져오기
            selectList = getResultSet(rs, clazz);
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
                e.printStackTrace();
            }
        }

        return selectList;
    }

    private List<T> getResultSet(ResultSet rs, Class<T> clazz)
    {
        List<T> selectList = new ArrayList<>();

        if(rs == null)
        {
            return selectList;
        }

        try
        {
            Constructor<T> constructor = clazz.getConstructor();
            Method[] setMethods =	clazz.getDeclaredMethods();

            ResultSetMetaData rsmd = rs.getMetaData();

            while( rs.next() )
            {
                T node = constructor.newInstance();

                for(int j=1; j<=rsmd.getColumnCount(); j++)
                {
                    String strColName	=	rsmd.getColumnName(j);

                    for (Method setMethod : setMethods) {
                        String strMethodName = setMethod.getName().toLowerCase();

                        if (strMethodName.startsWith("set")) {
                            String strMethodColName = strMethodName.substring(3);

                            if (strMethodColName.equalsIgnoreCase(strColName)) {
                                Object objValue = rs.getObject(j);
                                setMethod.invoke(node, objValue);
                                break;
                            }
                        }
                    }
                }

                selectList.add(node);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return selectList;
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

            Method[] insertMethods =	clazz.getDeclaredMethods();

            for (Method insertMethod : insertMethods)
            {
                String strMethodName = insertMethod.getName().toLowerCase();
                if (strMethodName.startsWith("get"))
                {
                    String strColNm = strMethodName.substring(3);
                    String objValue = (String) insertMethod.invoke(tClass);

                    if(objValue != null)
                    {
                        //컬럼명 쿼리 생성
                        if (strbufColumn.isEmpty())
                        {
                            strbufColumn.append(strColNm);
                        }
                        else
                        {
                            strbufColumn.append(", ");
                            strbufColumn.append(strColNm);
                        }
                        //value 쿼리 생성
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

            String strInsert    =  String.format(strbufQuery.toString(), strbufColumn, strbufValue);
            nResulRow =   update( strInsert, valueList );
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

            for (Method updateMethod : updateMethods)
            {
                String strMethodName = updateMethod.getName().toLowerCase();
                if (strMethodName.startsWith("get"))
                {
                    String strColNm = strMethodName.substring(3);

                    Object ObjValue = updateMethod.invoke(updateClass);
                    if (ObjValue != null)
                    {
                        if (!strbufUpdate.isEmpty() )
                        {
                            strbufUpdate.append(", ");
                        }
                        strbufUpdate.append(String.format(" %s=?", strColNm));

                        valueList.add(ObjValue);
                    }
                }
            }

            Method[] whereMethods =	clazz.getDeclaredMethods();

            for (Method whereMethod : whereMethods)
            {
                String strMethodName = whereMethod.getName().toLowerCase();
                if (strMethodName.startsWith("get"))
                {
                    String strColNm = strMethodName.substring(3);

                    Object ObjValue = whereMethod.invoke(whereClass);
                    if (ObjValue != null)
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

            String strUpdate = String.format(strbufQuery.toString(), strbufUpdate, strbufWhere);
            nResulRow =   update( strUpdate, valueList );

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

            for (Method value : method)
            {
                String strMethodName = value.getName().toLowerCase();
                if (strMethodName.startsWith("get"))
                {
                    String strColNm = strMethodName.substring(3);

                    Object ObjValue = value.invoke(tClass);
                    if (ObjValue != null)
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

            strbufQuery.append(strbufWhere);

            nResulRow =   update(strbufQuery.toString(), valueList);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return nResulRow;
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