package api.v1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBClient;
import util.Person;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet("/user")
public class User extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String userID = request.getParameter("userID");

        try {
            String sql = "SELECT * FROM testTable WHERE id = " + userID;
            Person.getPersonsList(response, sql);
        } catch (SQLException e) {
            pw.write("Ошибка при обработке SQL запроса. Метод GET.");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        HashMap<String, String> parametersList = new HashMap<>(DBClient.getURLParametersList(request));
        String requestBody = request.getReader().readLine();

        // trying with json if URL parameters are not present
        if (parametersList.containsKey("empty") && !requestBody.isEmpty()) {
//            pw.println("Вставляем из JSON");

            try {
                JsonObject jsonString = (JsonObject) JsonParser.parseString(requestBody);
                String sqlString = DBClient.buildSQLString(jsonString, "INSERT", "testTable");
//                pw.println("sql: " + sqlString);

                try {
                    DBClient.sqlProcess(sqlString);
                } catch (SQLException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    pw.write("Ошибка при обработке SQL запроса, проверьте заголовки ключей. Метод POST и JSON.");
                    e.printStackTrace();
                }
            } catch (JsonSyntaxException e) { // ловим неправильную строку JSON
                pw.println("Неверная JSON строка. Проверьте JSON строку и попробуйте ещё раз.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {  // proceed with URL parameters
            String sql = DBClient.buildSQLString(parametersList, "INSERT", "testTable");
            try {
                DBClient.sqlProcess(sql);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                pw.println("!!Ошибка при обработке SQL запроса. Где-то на просторах POST и параметров URL.");
                pw.println("SQL: " + sql);
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();

        HashMap<String, String> list = new HashMap<>(DBClient.getURLParametersList(request));
        if (list.get("userID") == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.println("Неверно указаны параметры: отсутствует userID.");
            return;
        }
        String sql = DBClient.buildSQLString(list, "UPDATE", "testTable");
        try {
            DBClient.sqlProcess(sql);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.println("Sql: " + sql);
            pw.println("Ошибка при обработке SQL запроса. Где-то на просторах PUT.");
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String userID = request.getParameter("userID");

        if (request.getParameter("userID") == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.println("Неверно указаны параметры");
            return;
        }

        try {
            String sql = "DELETE FROM testTable WHERE id = " + userID;
            DBClient.sqlProcess(sql);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.println("Ошибка при обработке SQL запроса. Где-то на просторах DELETE.");
            e.printStackTrace();        }
    }
}
