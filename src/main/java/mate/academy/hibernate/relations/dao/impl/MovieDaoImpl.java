package mate.academy.hibernate.relations.dao.impl;

import exception.DataProcessingException;
import java.util.Optional;
import mate.academy.hibernate.relations.dao.MovieDao;
import mate.academy.hibernate.relations.model.Movie;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class MovieDaoImpl extends AbstractDao implements MovieDao {
    public MovieDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Movie add(Movie movie) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(movie);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                throw new DataProcessingException(
                        "Can't commit transaction. Transaction is rolled back", e);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return movie;
    }

    @Override
    public Optional<Movie> get(Long id) {
        Optional<Movie> movie;
        try (Session session = factory.openSession()) {
            movie = Optional.ofNullable(session.get(Movie.class, id));
        } catch (HibernateException e) {
            throw new DataProcessingException(
                    "Can't get movie with id = " + id + "from DB", e);
        }
        return movie;
    }
}
