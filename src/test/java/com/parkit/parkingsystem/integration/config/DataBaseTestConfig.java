package com.parkit.parkingsystem.integration.config;

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

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {

        InputStream inputStream = null;
        Connection result = null;

        try {
            Properties properties = new Properties();
            String fileconfigDb = "testConfig.properties";
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream("testConfig.properties");
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

    @Override
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

    @Override
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

    @Override
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
     * @param inputStream : method to close resources
     */
    @Override
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