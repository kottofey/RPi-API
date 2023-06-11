package util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private final HashSet<Integer> itemsInCart = new HashSet<>();
    private final HashSet<Integer> itemsOwns = new HashSet<>();

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

    public void setItemsInCart(String itemsToAdd) {
        String[] str = itemsToAdd.replaceAll(" ", "").split(",");
        for (String s : str) {
            this.itemsInCart.add(Integer.parseInt(s));
        }
    }

    public HashSet<Integer> getItemsInCart() {
        return this.itemsInCart;
    }

    public void removeItemCart(String itemToRemove) {
        String[] str = itemToRemove.replaceAll(" ", "").split(",");
        for (String s : str) {
            this.itemsInCart.remove(Integer.parseInt(s));
        }
    }

    public void setItemsOwns(String itemsToAdd) {
        String[] str = itemsToAdd.replaceAll(" ", "").split(",");
        for (String s : str) {
            this.itemsOwns.add(Integer.parseInt(s));
        }
    }

    public HashSet<Integer> getItemsOwns() {
        return this.itemsOwns;
    }

    public void removeItemOwns(String itemToRemove) {
        String[] str = itemToRemove.replaceAll(" ", "").split(",");
        for (String s : str) {
            this.itemsOwns.remove(Integer.parseInt(s));
        }
    }

    public static void getPersonsList(HttpServletResponse response, String sql) throws SQLException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        Gson gson = new Gson();
        Person person;
        ArrayList<Person> list = new ArrayList<>();

        ResultSet resultSet = DBClient.sqlProcess(sql);

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
        pw.print(gson.toJson(list));
    }



    private static void prepare(HttpServletResponse response, PrintWriter pw) {

    }
}
