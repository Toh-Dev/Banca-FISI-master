package com.bancamovil.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_HOST = "gateway01.sa-east-1.prod.aws.tidbcloud.com";
    private static final String DB_PORT = "4000";
    private static final String DB_USER = "4Af1AZQEyYwsSiG.root";
    private static final String DB_PASSWORD = "4aYMRuFtGlRDnBby";
    private static final String DB_NAME = "sistema_bancario";

    // ✅ Se cambió useSSL=false por sslMode=VERIFY_IDENTITY para cumplir con la seguridad de TiDB Cloud
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?sslMode=VERIFY_IDENTITY&serverTimezone=UTC";

    // ✅ Devuelve una NUEVA conexión siempre
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado");
            return null;

        } catch (SQLException e) {
            System.err.println("❌ Error conectando a MySQL: " + e.getMessage());
            return null;
        }
    }
}