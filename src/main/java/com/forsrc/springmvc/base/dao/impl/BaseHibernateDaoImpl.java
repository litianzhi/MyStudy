package com.forsrc.springmvc.base.dao.impl;


import com.forsrc.exception.DaoException;
import com.forsrc.springmvc.base.dao.BaseHibernateDao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseHibernateDaoImpl<E> extends DaoSupport implements BaseHibernateDao<E> {

    protected Class<E> entityClass;
    @Autowired
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
    @Autowired
    @Resource(name = "hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    public BaseHibernateDaoImpl() {
        /*this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];*/

    }

    public int count() throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass())
                .setProjection(Projections.rowCount());
        Object obj = getHibernateTemplate().findByCriteria(criteria).get(0);
        if (obj instanceof Integer) {
            return (Integer) ((Integer) obj).intValue();
        }
        return 0;
    }

    public List<E> list(int begin, int size) throws DaoException {

        return list(this.entityClass, begin, size, null);
    }

    public <T> List<T> list(Class<T> clazz, int begin, int size)
            throws DaoException {

        return list(clazz, begin, size, null);
    }

    public <T> List<T> list(Class<T> clazz, int begin, int size, String orderColumnName)
            throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        if (!StringUtils.isBlank(orderColumnName)) {
            criteria.addOrder(Order.desc(orderColumnName.trim()));
        }
        if (begin < 0 || size <= 0) {
            return (List<T>) getHibernateTemplate().findByCriteria(criteria);
        }
        return (List<T>) getHibernateTemplate().findByCriteria(criteria, begin, size);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.hibernateTemplate = createHibernateTemplate(sessionFactory);
    }

    public Session getSession() {
        if (this.sessionFactory == null) {
            throw new HibernateException("Session Create Fail, SessionFactory is null!");
        }
        Session session = this.sessionFactory.getCurrentSession();
        if (session == null) {
            session = this.sessionFactory.openSession();
        }
        return session;
    }

    protected HibernateTemplate createHibernateTemplate(
            SessionFactory sessionFactory) {
        return new HibernateTemplate(sessionFactory);
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        if (this.hibernateTemplate == null) {
            throw new IllegalArgumentException("'sessionFactory' or 'hibernateTemplate' is required");
        }
    }

    protected final Session getSession(boolean allowCreate) {
        Session session = this.sessionFactory.getCurrentSession();
        if (session == null && allowCreate) {
            session = this.sessionFactory.openSession();
        }
        return session;
    }

    protected final Session openSession(boolean allowCreate) {
        return getSessionFactory().openSession();
    }

    protected final Session getCurrentSession(boolean allowCreate) {
        return getSessionFactory().getCurrentSession();
    }

    protected final void releaseSession(Session session) {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    public final HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    public final void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public synchronized Class<E> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
}