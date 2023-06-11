package util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Person {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String country;
    private String city;
    private int money;
    private HashSet<String> itemsInCart = new HashSet<>();
    private HashSet<String> itemsOwns = new HashSet<>();

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public HashSet<String> getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(String itemsInCart) {
        this.itemsInCart = new HashSet<>(Arrays.asList(itemsInCart.split(",")));
    }

    public HashSet<String> getItemsOwns() {
        return itemsOwns;
    }

    public void setItemsOwns(String itemsOwns) {
        this.itemsOwns = new HashSet<>(Arrays.asList(itemsOwns.split(",")));
    }

    public void addItemCart(String item) {
        itemsInCart.add(item);
    }

    public void removeItemCart(String item) {
        itemsInCart.remove(item);
    }

    public void addItemOwns(String item) {
        itemsOwns.add(item);
    }

    public void removeItemOwns(String item) {
        itemsOwns.add(item);
    }

    public static void getPersonsList(HttpServletResponse response, String sql) throws SQLException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        DBClient dbClient = new DBClient();
        Gson gson = new Gson();
        Person person;
        ArrayList<Person> list = new ArrayList<>();
        Statement statement = dbClient.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setSurname(resultSet.getString("surname"));
            person.setEmail(resultSet.getString("email"));
            person.setPhone(resultSet.getString("phone"));
            person.setCountry(resultSet.getString("country"));
            person.setCity(resultSet.getString("city"));
            person.setItemsInCart(resultSet.getString("cart"));
            person.setItemsOwns(resultSet.getString("owns"));
            person.setMoney(resultSet.getInt("money"));

            list.add(person);
        }
        statement.close();
        pw.print(gson.toJson(list));
    }

    public static void sqlProcess(HttpServletResponse response, String sql) throws SQLException {
        DBClient dbClient = new DBClient();
        Statement statement = dbClient.getConnection().createStatement();
        statement.executeQuery(sql);
        statement.close();
    }

    private static void prepare(HttpServletResponse response, PrintWriter pw) {

    }
}
