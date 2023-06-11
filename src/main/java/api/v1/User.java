package api.v1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBClient;
import util.Person;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/user")
public class User extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userID = request.getParameter("userID");

        try {
            String sql = "SELECT * FROM testTable WHERE id = " + userID;
            Person.getPersonsList(response, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        // JSON INSERT statement (no 'cart' and 'owns' fields):
        // {"name": "Roman", "surname": "Lavrov", "email": "email@email.com", "phone": "12345678", "country": "Country", "city": "City", "money": 10902}
        String requestBody = request.getReader().readLine();
        if (requestBody != null && requestBody.charAt(0) == '{') { // json body insert
                String sql = "INSERT INTO testTable (name, surname, email, phone, country, city, money)\n" +
                             "VALUES (\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.name'),\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.surname'),\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.email'),\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.phone'),\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.country'),\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.city'),\n" +
                             "           JSON_VALUE('" + requestBody + "', '$.money')\n" +
                             "       )";
            try {
                DBClient.sqlProcess(sql);
            } catch (SQLException e) {
                response.setStatus(400);
                pw.write("Неверно сформирована строка JSON");
                e.printStackTrace();
            }
        } else {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String country = request.getParameter("country");
            String city = request.getParameter("city");
            int money = Integer.parseInt(request.getParameter("money"));

            String valuesString = "'" + name + "','" + surname + "','" + email + "','" + phone + "','" + country + "','" + city + "'," + money;
            String sql = "INSERT INTO testTable (name, surname, email, phone, country, city, money) " +
                         "VALUES (" + valuesString + ")";
            try {
                DBClient.sqlProcess(sql);
            } catch (SQLException e) {
                response.setStatus(400);
                pw.println("Неверно указаны параметры");
                pw.println("ValuesString: " + valuesString);
                pw.println("sql: " + sql);
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}
