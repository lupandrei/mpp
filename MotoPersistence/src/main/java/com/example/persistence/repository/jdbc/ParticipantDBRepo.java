package com.example.persistence.repository.jdbc;

import com.example.model.Participant;
import com.example.persistence.IParticipantRepo;

import com.example.persistence.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepo implements IParticipantRepo {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ParticipantDBRepo(Properties prop) {
        dbUtils = new JdbcUtils(prop);
    }

    @Override
    public List<Participant> findParticipantsByTeamName(String teamName) {
        List<Participant> participants = new ArrayList<>();
        logger.traceEntry("finding by team name" + teamName);
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("Select * from participant where teamname=?")){
            ps.setString(1,teamName);
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String firstname=resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");
                    Participant participant = new Participant(id,firstname,lastname,teamName);
                    participants.add(participant);
                }
            }
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println("Error DB" + se);
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public List<String> getAllTeamNames() {
        List<String> teams = new ArrayList<>();
        logger.traceEntry("get all team names");
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("Select distinct teamname from participant")){
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    String teamName = resultSet.getString("teamname");
                    teams.add(teamName);
                }
            }
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println("Error DB" + se);
        }
        logger.traceExit();
        return teams;
    }

    @Override
    public Participant getLastParticipant() {
        logger.traceEntry("getLastParticipant");
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("Select * from participant order by id desc limit 1")){

            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String firstname=resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");
                    String teamName = resultSet.getString("teamname");
                    Participant participant = new Participant(id,firstname,lastname,teamName);
                    return participant;
                }
            }
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println("Error DB" + se);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public void add(Participant participant) {
        logger.traceEntry("saving task {}", participant);
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("insert into participant(firstname,lastname,teamname) Values (?,?,?)")){
            ps.setString(1, participant.getFirstName());
            ps.setString(2,participant.getLastName());
            ps.setString(3, participant.getTeamName());
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
    public void delete(Participant participant) {

    }

    @Override
    public void update(Participant participant, Integer integer) {

    }

    @Override
    public Participant findByID(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Participant> getAll() {
        return null;
    }
}
