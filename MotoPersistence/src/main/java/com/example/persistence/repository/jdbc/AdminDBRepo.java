package com.example.persistence.repository.jdbc;

import com.example.model.Admin;
import com.example.persistence.IAdminRepo;
import com.example.persistence.JdbcUtils;
import com.example.persistence.RepositoryException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AdminDBRepo implements IAdminRepo {

    JdbcUtils dbutils;
    private static final Logger logger= LogManager.getLogger();

    public AdminDBRepo(Properties props) {
        this.dbutils = new JdbcUtils(props);
    }

    @Override
    public void add(Admin admin) {

    }

    @Override
    public void delete(Admin admin) {

    }

    @Override
    public void update(Admin admin, String s) {

    }

    @Override
    public Admin findByID(String s){
        logger.traceEntry("find admin with username +" +s);
        Connection connection = dbutils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("Select * from adminmoto where username=?")){
            ps.setString(1,s);
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String password = resultSet.getString("password");
                    Admin admin = new Admin(s, password);
                    logger.trace("found admin " + admin);
                    return admin;
                }
            }
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println(se);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Admin> getAll() {
        return null;
    }
}
