package db.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class DBUtil <T>
{
    private Connection conn = null;
    private Statement stmt	=	null;
    private final PreparedStatement pstmt	=	null;

    public DBUtil ()
    {
        try{
            // 1. 드라이버 로딩
            // 드라이버 인터페이스를 구현한 클래스를 로딩
            // mysql, oracle 등 각 벤더사 마다 클래스 이름이 다르다.
            // mysql은 "com.mysql.jdbc.Driver"이며, 이는 외우는 것이 아니라 구글링하면 된다.
            // 참고로 이전에 연동했던 jar 파일을 보면 com.mysql.jdbc 패키지에 Driver 라는 클래스가 있다.
            Class.forName("org.mariadb.jdbc.Driver");

            // 2. 연결하기
            // 드라이버 매니저에게 Connection 객체를 달라고 요청한다.
            // Connection을 얻기 위해 필요한 url 역시, 벤더사마다 다르다.
            // mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
            String url = "jdbc:mariadb://dev.appdign.com:3306/FAKE_INSTA";

            // @param  getConnection(url, userName, password);
            // @return Connection
            conn = DriverManager.getConnection(url, "fake_usr", "FakeInsta1234");
            stmt = conn.createStatement();
            System.out.println("Conection success!!");

        }
        catch(ClassNotFoundException e){
            System.out.println("Driver Load fail");
        }
        catch(SQLException e){
            System.out.println("Error: " + e);
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
            selectList =   getResultList(strQuery, constructor, method);
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

        try
        {
            Class clazz           =     tClass.getClass();
            String strClassName   =     clazz.getName();
            String strTableName   =     strClassName.substring( strClassName.lastIndexOf(".")+1 );
            strTableName = strTableName.substring(0, strTableName.length() -3);
            strbufQuery.append("SELECT * FROM ");
            strbufQuery.append(strTableName);
            strbufQuery.append(" WHERE 1=1 ");

            Method[] method =	clazz.getDeclaredMethods();

            for (Method value : method) {
                String strMethodName = value.getName().toLowerCase();
                if (strMethodName.startsWith("get")) {
                    String strColNm = strMethodName.substring(3);

                    if (value.getReturnType().getName().toLowerCase().contains("string")) {
                        String strValue = (String) value.invoke(tClass);
                        if (strValue != null) {
                            strbufQuery.append(String.format(" AND %s='%s'", strColNm, strValue));
                        }

                    } else {
                        Object ObjValue = value.invoke(tClass);
                        if (ObjValue != null) {
                            // BigDecimal bnValue  =   new BigDecimal( ObjValue.toString() );
                            // if( bnValue.compareTo( new BigDecimal("-999999999") ) != 0)
                            {
                                strbufQuery.append(String.format(" AND %s=", strColNm));
                                strbufQuery.append(ObjValue);
                            }
                        }
                    }
                }
            }

            Constructor<T> constructor = clazz.getConstructor();

            Method[] setMethod =	clazz.getDeclaredMethods();
            selectList =   getResultList(strbufQuery.toString(), constructor, setMethod);



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return selectList;
    }

    private List<T> getResultList(String strQuery, Constructor<T> constructor, Method[] method)
    {
        ResultSet rs	=	null;

        List<T> selectList	=	new ArrayList<>();

        try
        {
            rs = stmt.executeQuery(strQuery);

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

        return  selectList;
    }







    public String selectToJson(String strQuery, Class<T> clazz)
    {
        List<T> selectList = select(strQuery, clazz);
        return new Gson().toJson(selectList);
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

