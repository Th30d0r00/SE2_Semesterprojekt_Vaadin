package org.hbrs.se2.project.hellocar.services.db;

import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton-Klasse zur Verwaltung der Datenbankverbindung.
 * Erstellt und verwaltet Prepared Statements für die Datenbankoperationen.
 *
 * Autor: sascha
 */
public class JDBCConnectionPrepared {

    private static JDBCConnectionPrepared instance = null;

    private String url = "jdbc:postgresql://dumbo.inf.h-brs.de/ewagen2s";

    private Connection conn;

    private String login = "ewagen2s";

    private String password = "ewagen2s";

    // Singleton-Instanz erhalten
    public static JDBCConnectionPrepared getInstance() throws DatabaseLayerException {
        if (instance == null) {
            instance = new JDBCConnectionPrepared();
        }
        return instance;
    }

    // Privater Konstruktor, um die Singleton-Eigenschaft zu gewährleisten
    private JDBCConnectionPrepared() throws DatabaseLayerException {
        this.initConnection();
    }

    // Initialisiert die Datenbankverbindung
    public void initConnection() throws DatabaseLayerException {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnectionPrepared.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseLayerException("Fehler bei der Registrierung des Datenbanktreibers.");
        }
        this.openConnection();
    }

    // Öffnet die Datenbankverbindung
    public void openConnection() throws DatabaseLayerException {
        try {
            Properties props = new Properties();
            props.setProperty("user", login);
            props.setProperty("password", password);
            this.conn = DriverManager.getConnection(this.url, props);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnectionPrepared.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseLayerException("Fehler beim Öffnen der Datenbankverbindung. Sichere Verbindung vorhanden?");
        }
    }

    // Gibt ein Prepared Statement zurück
    public PreparedStatement getPreparedStatement(String sql) throws DatabaseLayerException {
        try {
            if (this.conn.isClosed()) {
                this.openConnection();
            }
            return this.conn.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnectionPrepared.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseLayerException("Fehler beim Erstellen des Prepared Statements.");
        }
    }

    // Gibt ein Prepared Statement mit Rückgabe generierter Schlüssel zurück
    public PreparedStatement getPreparedStatement(String sql, int autoGeneratedKeys) throws DatabaseLayerException {
        try {
            if (this.conn.isClosed()) {
                this.openConnection();
            }
            return this.conn.prepareStatement(sql, autoGeneratedKeys);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnectionPrepared.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseLayerException("Fehler beim Erstellen des Prepared Statements mit generierten Schlüsseln.");
        }
    }

    // Schließt die Datenbankverbindung
    public void closeConnection() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnectionPrepared.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
