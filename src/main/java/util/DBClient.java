package util;

import java.sql.*;

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
}