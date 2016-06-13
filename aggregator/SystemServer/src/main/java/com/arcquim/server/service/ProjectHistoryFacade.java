package com.arcquim.server.service;

import com.arcquim.server.entity.ProjectHistory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Stateless
@Path("/history")
public class ProjectHistoryFacade extends AbstractFacade<ProjectHistory> {
    
    @PersistenceContext(unitName = "com.arcquim_SystemServer_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;
    
    public ProjectHistoryFacade() {
        this(ProjectHistory.class);
    }

    public ProjectHistoryFacade(Class<ProjectHistory> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
}
