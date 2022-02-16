package org.ga4gh.testbed.api.utils.hibernate;

import javax.annotation.PostConstruct;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityMismatchException;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.hibernate.exception.EntityDoesntExistException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestbedApiHibernateUtil extends HibernateUtil {

    // private boolean configured;
    // private SessionFactory sessionFactory;

    /*
    @PostConstruct
    public void buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.setProperties(getDatabaseProps().getAllProperties());
            for (Class<? extends HibernateEntity<? extends Serializable>> annotatedClass : getAnnotatedClasses()) {
                configuration.addAnnotatedClass(annotatedClass);
            }
            setSessionFactory(configuration.buildSessionFactory());
            setConfigured(true);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public boolean getConfigured() {
        return configured;
    }

    private void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    */
}
