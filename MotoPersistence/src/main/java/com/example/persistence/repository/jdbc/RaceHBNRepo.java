package com.example.persistence.repository.jdbc;

import com.example.model.Race;
import com.example.persistence.IRaceRepo;
import com.example.persistence.RepositoryException;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.archive.spi.ArchiveException;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Component
public class RaceHBNRepo implements IRaceRepo {
    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public RaceHBNRepo() {
        initialize();
    }

    @Override
    public Race findRaceByEngineCapacity(int engineCapacity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            String sql = "From Race where engineCapacity=:capacity";
            Race race = (Race)session.createQuery(sql)
                    .setParameter("capacity",engineCapacity)
                    .uniqueResult();
            return race;
        }catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getAllEngineCapacities() {
        List<Integer> engineCapacities = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
            Root<Race> root = query.from(Race.class);
            query.select(root.get("engineCapacity")).distinct(true);
            engineCapacities = session.createQuery(query).getResultList();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return engineCapacities;
    }

    @Override
    public Race getLastRaceAdded() {
        return null;
    }

    @Override
    public void deleteRace(Race race)  {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(race);
            session.getTransaction().commit();
        }catch (HibernateException e) {
            e.printStackTrace();;
        }
    }

    @Override
    public void add(Race race) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(race);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Race race) {

    }

    @Override
    public void update(Race race, Integer integer) {
        System.out.println("Updating race");
            int newEngineCapacity = race.getEngineCapacity();
            String newRaceName = race.getRaceName();
            try(Session session =sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                String hqlUpdate = "update Race set engineCapacity = :newEngineCapacity, raceName = :newRaceName where id = :integer";
                int updatedEntities = session.createQuery(hqlUpdate)
                        .setParameter("newEngineCapacity", newEngineCapacity) // or any other new capacity value you want to set
                        .setParameter("newRaceName", newRaceName) // the name value to filter by
                        .setParameter("integer",integer)
                        .executeUpdate();
                System.out.println(updatedEntities);
                tx.commit();
        }

    }

    @Override
    public Race findByID(Integer integer) {
        System.out.println("find by id"+integer);
        try(Session session = sessionFactory.openSession()) {
            Race race = (Race) session.get(Race.class,integer);
            return race;
        }
    }

    @Override
    public Iterable<Race> getAll() {
        try (Session session = sessionFactory.openSession()) {
            System.out.println("get all races");
            List<Race> races = session.createQuery("from Race r").list();
            for(Race race:races){
                System.out.println(race.getID());
            }
            return races;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
