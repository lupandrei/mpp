package com.example.persistence.repository.jdbc;

import com.example.model.Race;
import com.example.persistence.IRaceRepo;

import com.example.persistence.JdbcUtils;
import com.example.persistence.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class RaceDBRepo implements IRaceRepo {

    JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RaceDBRepo(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Race findRaceByEngineCapacity(int engineCapacity) {
        logger.traceEntry("find race with capacity " + engineCapacity);
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("Select * from race where enginecapacity=?")){
            ps.setInt(1,engineCapacity);
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String racename = resultSet.getString("name");
                    Race race = new Race(id,engineCapacity,racename);
                    logger.trace("found race " + race);
                    return race;
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
    public List<Integer> getAllEngineCapacities() {
        logger.traceEntry("getAllEngineCapacities");
        Connection conn = dbUtils.getConnection();
        List<Integer> engineCapacities = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement("Select enginecapacity from race")){
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    int engineCapacity = resultSet.getInt("enginecapacity");
                    engineCapacities.add(engineCapacity);
                }
            }
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println(se);
        }
        logger.traceExit();
        return engineCapacities;
    }

    @Override
    public Race getLastRaceAdded() {
        return null;
    }

    @Override
    public void deleteRace(Race race) throws RepositoryException {

    }

    @Override
    public void add(Race race) {
        logger.traceEntry("saving race {}", race);
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("insert into race(name,enginecapacity) Values (?,?)")){
            ps.setString(1, race.getRaceName());
            ps.setInt(2,race.getEngineCapacity());
            int resultset= ps.executeUpdate();
            logger.trace("Saved  {} instances",resultset);
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println("Error DB "+se);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Race race) {

    }

    @Override
    public void update(Race race, Integer integer) {

    }

    @Override
    public Race findByID(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Race> getAll() {
        return null;
    }
}
