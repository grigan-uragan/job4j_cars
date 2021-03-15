package ru.job4j.cars.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.Ads;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Photo;
import ru.job4j.cars.model.User;

public class HbmRun {
    private static final Logger LOG = LoggerFactory.getLogger(HbmRun.class);

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try {
            Session session = factory.openSession();
            session.beginTransaction();
            User user = User.of("user", "user@mail", "1234");
            Car car = Car.of("BMW", "M3", "sedan", 100.00);
            Photo photo = Photo.of("images/photo.png");
            Ads ads = Ads.of("sale car", car, photo, user, true);
            session.save(ads);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOG.error("transaction exception", e);
            factory.getCurrentSession().getTransaction().rollback();
            throw e;
        }

    }
}
