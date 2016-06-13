package com.arcquim.server.service;

import com.arcquim.gwt.shared.Record;
import com.arcquim.server.transport.MessageParser;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public abstract class AbstractFacade<T> {
    
    private final MessageParser messageParser = new MessageParser();
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public Record parseMessage(String json) {
        return messageParser.getMessageBodyAsRecord(json);
    }
    
    public String createMessage(Record record) {
        return messageParser.getMessageString(record);
    }
    
}
