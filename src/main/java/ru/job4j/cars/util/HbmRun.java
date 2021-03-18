package ru.job4j.cars.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.Ads;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Photo;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repo.AdRepository;

import java.util.List;

public class HbmRun {
    private static final Logger LOG = LoggerFactory.getLogger(HbmRun.class);

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try {
            Session session = factory.openSession();
            session.beginTransaction();
            User user = User.of("Sam", "Sam@mail", "qwerty");
            Car car = Car.of("Audi", "A6", "sedan", 710.00);
            Ads ads = Ads.of("sale cool modern car", car, null, user, true);
            session.save(ads);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOG.error("transaction exception", e);
            factory.getCurrentSession().getTransaction().rollback();
            throw e;
        }

        AdRepository repository = new AdRepository(factory);
        List<Ads> day = repository.allAdsForDay();
        List<Ads> photo = repository.allAdsWithPhoto();
        List<Ads> mark = repository.allAdsForMark("BMW");
        day.forEach(System.out::println);
        photo.forEach(System.out::println);
        mark.forEach(System.out::println);
    }
}
