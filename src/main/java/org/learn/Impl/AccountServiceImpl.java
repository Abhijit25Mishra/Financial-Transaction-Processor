package org.learn.Impl;

import io.javalin.http.Context;
import org.learn.Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.learn.VOs.NewAccountRequest;

public class AccountServiceImpl {

    public void createAccount(Context ctx) throws SQLException {
        NewAccountRequest request = ctx.bodyAsClass(NewAccountRequest.class);
        String sql = "INSERT INTO accounts (owner_name, balance) VALUES (?,?) RETURNING id;";

        try (Connection conn = DbUtils.getDbConnection();
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

    public void getAccountById(Context ctx) throws SQLException {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        String sql = "SELECT id, owner_name, balance FROM accounts WHERE id =?;";

        try (Connection conn = DbUtils.getDbConnection();
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