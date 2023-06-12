package util;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBClient {
    private final String USER = "kottofey";
    private final String PASSWORD = "98408";
    private final String DB_ADDRESS = "10.0.1.12";
    private final String DB_PORT = "23109";
    private final String DB = "dbTest";
    Connection connection;

    public DBClient() {
        try {
            String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String DB_URL = "jdbc:mariadb://" + DB_ADDRESS + ":" + DB_PORT + "/" + DB;
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static ResultSet sqlProcess(String sql) throws SQLException {
        DBClient dbClient = new DBClient();
        Statement statement = dbClient.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);
        statement.close();
        return rs;
    }

    public static HashMap<String, String> getURLParametersList(HttpServletRequest request) {
        HashMap<String, String> list = new HashMap<>();

        list.put("userID", request.getParameter("userID"));
        list.put("name", request.getParameter("name"));
        list.put("surname", request.getParameter("surname"));
        list.put("email", request.getParameter("email"));
        list.put("phone", request.getParameter("phone"));
        list.put("country", request.getParameter("country"));
        list.put("city", request.getParameter("city"));
        list.put("money", request.getParameter("money"));

        boolean isNull = true;
        for (Map.Entry<String, String> entry : list.entrySet()) {
            if (entry.getValue() != null) {
                return list;
            }
        }
        list.put("empty", "true");
        return list;
    }

    // DONE: URL parameters UPDATE; INSERT with url params
    public static String buildSQLString(HashMap<String, String> paramList, String action, String table) {
        StringBuilder sqlString = new StringBuilder();

        if (action.equals("UPDATE")) {
            sqlString.append("UPDATE ").append(table).append(" SET ");
            for (Map.Entry<String, String> list : paramList.entrySet()) {
                if (list.getValue() != null && !list.getKey().equals("userID")) {
                    sqlString.append(list.getKey()).append(" = '")
                            .append(list.getValue()).append("', ");
                }
            }
            sqlString.delete(sqlString.length() - 2, sqlString.length());
            sqlString.append(" WHERE id = ").append(paramList.get("userID"));
        } else if (action.equals("INSERT")) {
            sqlString.append("INSERT INTO ").append(table).append(" (");

            for (Map.Entry<String, String> list : paramList.entrySet()) {
                if (list.getValue() != null && !list.getKey().equals("userID")) {
                    sqlString.append(list.getKey()).append(", ");
                }
            }
            sqlString.delete(sqlString.length() - 2, sqlString.length()).append(") VALUES ('");
            for (Map.Entry<String, String> list : paramList.entrySet()) {
                if (list.getValue() != null && !list.getKey().equals("userID")) {
                    sqlString.append(list.getValue()).append("', '");
                }
            }
            sqlString.delete(sqlString.length() - 3, sqlString.length()).append(")");
        }

        return sqlString.toString();
    }

    // DONE: JSON string INSERT
    public static String buildSQLString(JsonObject jsonString, String action, String table) {

        // JSON has been validated in parent method
        StringBuilder sqlString = new StringBuilder();

        sqlString.append("INSERT INTO ").append(table).append(" (");

        // Add rows
        for (String key : jsonString.keySet()) {
            if (!key.isEmpty()) {
                sqlString.append(key).append(", ");
            }
        }
        sqlString.delete(sqlString.length() - 2, sqlString.length()).append(") VALUES (");

        // Add values
        for (String key : jsonString.keySet()) {
            if (!key.isEmpty()) {
                sqlString.append(jsonString.get(key)).append(", ");
            }
        }
        sqlString.delete(sqlString.length() - 2, sqlString.length()).append(")");

        return sqlString.toString();
    }
}