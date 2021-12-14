package com.parkit.parkingsystem.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection()
            throws ClassNotFoundException, SQLException {

        /*
         * logger.info("Create DB connection");
         * Class.forName("com.mysql.cj.jdbc.Driver"); Ajout après le nom de la BDD du
         * serveur de temps. return DriverManager.getConnection(
         * "jdbc:mysql://localhost:3306/prod?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
         * "root", "rootroot");
         */
        InputStream inputStream = null;
        Connection result = null;

        try {
            Properties properties = new Properties();
            String fileconfigDb = "config.properties";
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream("config.properties");
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + fileconfigDb
                        + "' not found in the classpath");
            }

            logger.info("Create DB connection");
            Class.forName("com.mysql.cj.jdbc.Driver");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            result = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(inputStream);
        }
        return result;

    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }

    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }

    /**
     * @param inputStream : méthode pour fermer les ressources
     */
    public void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
                logger.info("Closing Input Stream");
            } catch (IOException e) {
                logger.error("Error while closing result set", e);
            }

        }
    }

}
