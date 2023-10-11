package com.example.persistence.repository.jdbc;

import com.example.model.Entry;
import com.example.model.ParticipantDTO;
import com.example.model.RaceDTO;
import com.example.persistence.IEntryRepo;
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
import java.util.Set;

public class EntryDBRepo implements IEntryRepo {
    JdbcUtils dbutils;
    private static final Logger logger= LogManager.getLogger();

    public EntryDBRepo(Properties props) {
        this.dbutils = new JdbcUtils(props);
    }

    @Override
    public List<RaceDTO> getRaceEntriesByEngineCapacity() {
        List<RaceDTO> raceDTOS = new ArrayList<>();
        logger.traceEntry("Race entries by engine capacity");
        Connection conn = dbutils.getConnection();
        String sql = """
        Select R.name as name, R.enginecapacity as enginecapacity, count(E.idrace) as count from entry E
        INNER JOIN race R ON r.id=E.idrace
        GROUP BY E.idrace, R.name, R.enginecapacity
                """;
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    String name = resultSet.getString("name");
                    int enginecapacity= resultSet.getInt("enginecapacity");
                    int count = resultSet.getInt("count");
                    RaceDTO raceDTO = new RaceDTO(name,enginecapacity,count);
                    logger.trace("racedto: " +raceDTO);
                    raceDTOS.add(raceDTO);
                }
            }
        }
        catch(SQLException se){
            logger.error(se);
            System.err.println(se);
        }
        logger.traceExit();
        return raceDTOS;
    }

    @Override
    public List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName) {
        List<ParticipantDTO> participantDTOS = new ArrayList<>();
        logger.traceEntry("participants with name and engine capacity");
        Connection conn = dbutils.getConnection();
        if(!teamName.equals("all"))
        {
            String sql= """
                Select P.firstname as firstname, P.lastname as lastname, R.enginecapacity
                from Entry E INNER JOIN participant P on p.id=E.idparticipant
                INNER JOIN race R on r.id=E.idrace
                WHERE P.teamname=?
                GROUP BY P.firstname, P.lastname, R.enginecapacity
                """;
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1,teamName);
                try(ResultSet resultSet = ps.executeQuery()){
                    while(resultSet.next()){
                        String firstname = resultSet.getString("firstname");
                        String lastname= resultSet.getString("lastname");
                        int enginecapacity = resultSet.getInt("enginecapacity");
                        ParticipantDTO participantDTO = new ParticipantDTO(firstname,lastname,enginecapacity);
                        logger.trace("participantdto: " +participantDTO);
                        participantDTOS.add(participantDTO);
                    }
                }
            }
            catch(SQLException se){
                logger.error(se);
                System.err.println(se);
            }
        }
        else{
            String sql= """
                Select P.firstname as firstname, P.lastname as lastname, R.enginecapacity
                from Entry E INNER JOIN participant P on p.id=E.idparticipant
                INNER JOIN race R on r.id=E.idrace
                GROUP BY P.firstname, P.lastname, R.enginecapacity
                """;
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                try(ResultSet resultSet = ps.executeQuery()){
                    while(resultSet.next()){
                        String firstname = resultSet.getString("firstname");
                        String lastname= resultSet.getString("lastname");
                        int enginecapacity = resultSet.getInt("enginecapacity");
                        ParticipantDTO participantDTO = new ParticipantDTO(firstname,lastname,enginecapacity);
                        logger.trace("participantdto: " +participantDTO);
                        participantDTOS.add(participantDTO);
                    }
                }
            }
            catch(SQLException se){
                logger.error(se);
                System.err.println(se);
            }
        }

        logger.traceExit();
        return participantDTOS;
    }

    @Override
    public void add(Entry entry) {
        logger.traceEntry("saving entry {}", entry);
        Connection conn = dbutils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("insert into entry(idrace,idparticipant) Values (?,?)")){
            ps.setInt(1, entry.getIdRace());
            ps.setInt(2,entry.getIdParticipant());

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
    public void delete(Entry entry) {

    }

    @Override
    public Iterable<Entry> getAll() {
        return null;
    }

    @Override
    public Entry findByID(Set<Integer> integerSet) {
        return null;
    }

    @Override
    public void update(Entry entry, Set<Integer> integerSet) {

    }
}
