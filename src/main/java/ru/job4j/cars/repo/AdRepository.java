package ru.job4j.cars.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.Ads;
import ru.job4j.cars.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class AdRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AdRepository.class);
    private final SessionFactory factory;

    public AdRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public List<Ads> allAdsForDay() {
        Date date = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
       try {
           Session session = factory.openSession();
           session.beginTransaction();
           List result = session.createQuery(
                   "select a from Ads a "
                           + " where a.created > :date")
                   .setParameter("date", date)
                   .list();
           session.getTransaction().commit();
           session.close();
           return result;
       } catch (Exception e) {
           LOG.error("transaction error", e);
           factory.getCurrentSession().getTransaction().rollback();
           throw e;
       }
    }

    public List<Ads> allAdsWithPhoto() {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            List list = session.createQuery("select a from Ads a "
                    + "join fetch a.car c join fetch a.user u "
                    + " join fetch a.photo p").list();
            session.getTransaction().commit();
            session.close();
            return list;
        } catch (Exception e) {
            LOG.error("transaction exception", e);
            factory.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    public List<Ads> allAdsForMark(String mark) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            List list = session.createQuery("select a from Ads a join fetch a.car c"
                    + " where c.mark = :mark")
                    .setParameter("mark", mark)
                    .list();
            session.getTransaction().commit();
            session.close();
            return list;
        } catch (Exception e) {
            LOG.error("transaction exception", e);
            factory.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }
}
