package org.ga4gh.testbed.api.utils.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.criteria.*;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.ga4gh.testbed.api.controller.Testbeds;
import org.ga4gh.testbed.api.model.*;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;

public class TestbedApiHibernateUtil extends HibernateUtil {

    public <I extends Serializable, T extends HibernateEntity<I>> List<T> listEntityObject(Class<T> entityClass) throws HibernateException {
        Session session = newTransaction();
        List<T> objects = new ArrayList<>();

        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(entityClass);
            Root<T> root = cr.from(entityClass);
            cr.select(root);

            Query<T> query = session.createQuery(cr);
            objects = query.getResultList();
        } catch (Exception ex) {
            endTransaction(session);
            throw ex;
        } finally {
            endTransaction(session);
        }

        return objects;
    }

    public Report readFullReport(String id) throws HibernateException {
        Session session = newTransaction();
        Report report = null;

        try {
            report = session.get(Report.class, id);
            if (report != null) {
                report.loadRelations();
                for (Phase phase : report.getPhases()) {
                    phase.loadRelations();
                    for (TestbedTest testbedTest : phase.getTests()) {
                        testbedTest.loadRelations();
                        for (TestbedCase testbedCase : testbedTest.getCases()) {
                            testbedCase.loadRelations();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // any errors need to be caught so the transaction can be closed
            endTransaction(session);
            throw ex;
        } finally {
            endTransaction(session);
        }

        return report;
    }

    public Report readPartialReport(String id) throws HibernateException {
        Session session = newTransaction();
        Report report = null;

        try {
            report = session.get(Report.class, id);
            if (report != null) {
                report.loadRelations();
                for (Phase phase : report.getPhases()) {
                    phase.loadRelations();
                }
            }
        } catch (Exception ex) {
            // any errors need to be caught so the transaction can be closed
            endTransaction(session);
            throw ex;
        } finally {
            endTransaction(session);
        }

        return report;
    }
}
