package com.flipkart.hibernate.multisession;

import org.hibernate.*;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rishabh.sood on 11/08/16.
 */
public class MultiSessionFactoryImpl implements SessionFactory {

    public MultiSessionFactoryImpl() {

    }

    @Override public SessionFactoryOptions getSessionFactoryOptions() {
        return getSessionFactory().getSessionFactoryOptions();
    }

    @Override public SessionBuilder withOptions() {
        return getSessionFactory().withOptions();
    }

    @Override public Session openSession() throws HibernateException {
        return getSessionFactory().openSession();
    }

    @Override public Session getCurrentSession() throws HibernateException {
        return getSessionFactory().getCurrentSession();
    }

    @Override public StatelessSessionBuilder withStatelessOptions() {
        return getSessionFactory().withStatelessOptions();
    }

    @Override public StatelessSession openStatelessSession() {
        return getSessionFactory().openStatelessSession();
    }

    @Override public StatelessSession openStatelessSession(Connection connection) {
        return getSessionFactory().openStatelessSession(connection);
    }

    @Override public ClassMetadata getClassMetadata(Class aClass) {
        return getSessionFactory().getClassMetadata(aClass);
    }

    @Override public ClassMetadata getClassMetadata(String s) {
        return getSessionFactory().getClassMetadata(s);
    }

    @Override public CollectionMetadata getCollectionMetadata(String s) {
        return getSessionFactory().getCollectionMetadata(s);
    }

    @Override public Map<String, ClassMetadata> getAllClassMetadata() {
        return getSessionFactory().getAllClassMetadata();
    }

    @Override public Map getAllCollectionMetadata() {
        return getSessionFactory().getAllCollectionMetadata();
    }

    @Override public Statistics getStatistics() {
        return getSessionFactory().getStatistics();
    }

    @Override public void close() throws HibernateException {
        getSessionFactory().close();
    }

    @Override public boolean isClosed() {
        return getSessionFactory().isClosed();
    }

    @Override public Cache getCache() {
        return getSessionFactory().getCache();
    }

    @Override public void evict(Class aClass) throws HibernateException {
        getSessionFactory().evict(aClass);
    }

    @Override public void evict(Class aClass, Serializable serializable) throws HibernateException {
        getSessionFactory().evict(aClass, serializable);
    }

    @Override public void evictEntity(String s) throws HibernateException {
        getSessionFactory().evictEntity(s);
    }

    @Override public void evictEntity(String s, Serializable serializable)
        throws HibernateException {
        getSessionFactory().evictEntity(s, serializable);
    }

    @Override public void evictCollection(String s) throws HibernateException {
        getSessionFactory().evictCollection(s);
    }

    @Override public void evictCollection(String s, Serializable serializable)
        throws HibernateException {
        getSessionFactory().evictCollection(s, serializable);
    }

    @Override public void evictQueries(String s) throws HibernateException {
        getSessionFactory().evictQueries(s);
    }

    @Override public void evictQueries() throws HibernateException {
        getSessionFactory().evictQueries();
    }

    @Override public Set getDefinedFilterNames() {
        return getSessionFactory().getDefinedFilterNames();
    }

    @Override public FilterDefinition getFilterDefinition(String s) throws HibernateException {
        return getSessionFactory().getFilterDefinition(s);
    }

    @Override public boolean containsFetchProfileDefinition(String s) {
        return getSessionFactory().containsFetchProfileDefinition(s);
    }

    @Override public TypeHelper getTypeHelper() {
        return getSessionFactory().getTypeHelper();
    }

    @Override public Reference getReference() throws NamingException {
        return getSessionFactory().getReference();
    }

    private SessionFactory getSessionFactory() {
        List<SessionFactory> sessionFactories = MultiManagedSessionContext.getBoundSessionFactories();
        if(sessionFactories.size() == 1) {
            return sessionFactories.get(0);
        }
        throw new RuntimeException("You have " + sessionFactories.size() + " SessionFactories bound to ManagedSessionContext. MultiManagedSessionContext requires exactly 1 SessionFactory to be bound");
    }
}

