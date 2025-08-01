package org.learn;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AccountService {

    // This will hold the loaded database URL
    private static String DB_URL;

    static {
        try {
            // Load properties from the config.properties file
            Properties props = new Properties();
            InputStream inputStream = AccountService.class.getClassLoader().getResourceAsStream("config.properties");
            if (inputStream == null) {
                throw new RuntimeException("config.properties not found in the classpath");
            }
            props.load(inputStream);
            DB_URL = props.getProperty("db.url");
        } catch (Exception e) {
            e.printStackTrace();
            // Exit if we can't load the database configuration
            System.exit(1);
        }
    }

    public static class NewAccountRequest {
        public String ownerName;
        public double initialBalance;
    }

    public static void main(String args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new io.javalin.json.JavalinJackson());
        }).start(8080);

        System.out.println("Account Service is running on port 8080");

        app.get("/", ctx -> ctx.result("Account Service is running!"));
        app.get("/accounts/{id}", AccountService::getAccountById);
        app.post("/accounts", AccountService::createAccount);
    }

    private static Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void createAccount(Context ctx) throws SQLException {
        NewAccountRequest request = ctx.bodyAsClass(NewAccountRequest.class);
        String sql = "INSERT INTO accounts (owner_name, balance) VALUES (?,?) RETURNING id;";

        try (Connection conn = getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, request.ownerName);
            pstmt.setBigDecimal(2, java.math.BigDecimal.valueOf(request.initialBalance));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int newId = rs.getInt("id");
                ctx.status(201).json(Map.of("id", newId, "message", "Account created successfully"));
            } else {
                ctx.status(500).result("Failed to create account, no ID returned.");
            }
        }
    }

    private static void getAccountById(Context ctx) throws SQLException {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        String sql = "SELECT id, owner_name, balance FROM accounts WHERE id =?;";

        try (Connection conn = getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> account = new HashMap<>();
                account.put("id", rs.getInt("id"));
                account.put("ownerName", rs.getString("owner_name"));
                account.put("balance", rs.getBigDecimal("balance"));
                ctx.json(account);
            } else {
                ctx.status(404).result("Account not found");
            }
        }
    }
}