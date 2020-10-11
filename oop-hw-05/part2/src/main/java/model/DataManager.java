package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public class DataManager {

    public static class Product {
        public String id;
        public String img;
        public String name;
        public BigDecimal price;
    }

    public Product getProductInfo(String productId) {
        Product product = null;
        MyDB db = new MyDB();
        Connection con = db.getConnection();

        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM products WHERE productId = ?;");
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.id = rs.getString(1);
                product.name = rs.getString(2);
                product.img = rs.getString(3);
                product.price = rs.getBigDecimal(4);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    public ArrayList<Product> getProductsList() {
        return getProductsList(null);
    }

    public ArrayList<Product> getProductsList(Set<String> idSet) {
        ArrayList<Product> res = new ArrayList<>();
        MyDB db = new MyDB();
        Connection con = db.getConnection();

        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM products");
            if (idSet != null) {
                query.append(" WHERE productId IN (");
                StringBuilder ids = new StringBuilder();
                for (String id : idSet) {
                    ids.append("'").append(id).append("',");
                }

                query.append(ids.substring(0, ids.length() - 1)).append(")");
            }

            query.append(";");
            PreparedStatement statement = con.prepareStatement(query.toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Product product = new Product();

                product.id = (String) rs.getObject(1);
                product.name = (String) rs.getObject(2);
                product.img = (String) rs.getObject(3);
                product.price = (BigDecimal) rs.getObject(4);

                res.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

}