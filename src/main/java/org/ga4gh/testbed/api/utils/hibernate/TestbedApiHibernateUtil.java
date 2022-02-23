package org.ga4gh.testbed.api.utils.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.ga4gh.testbed.api.model.Phase;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.TestbedCase;
import org.ga4gh.testbed.api.model.TestbedTest;
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

    public <I extends Serializable, T extends HibernateEntity<I>> void createEntityObject(Class<T> entityClass, T newObject) throws EntityExistsException {
        Session session = newTransaction();
        try {
            T existingObject = session.get(entityClass, newObject.getId());
            if (existingObject != null) {
                endTransaction(session);
                throw new EntityExistsException("A(n) " + entityClass.getSimpleName() + " already exists at id " + newObject.getId());
            }
            session.save(newObject);
        } catch (Exception ex) {
            // any errors need to be caught so the transaction can be closed
            endTransaction(session);
            throw ex;
        } finally {
            endTransaction(session);
        }
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

}
