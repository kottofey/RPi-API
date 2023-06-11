package api.v1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.Person;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/users")
public class Users extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String sql = "SELECT * FROM testTable";
            Person.getPersonsList(response, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
