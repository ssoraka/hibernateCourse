package com.dmdev.listener;

import com.dmdev.entity.Audit;
import com.dmdev.entity.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.hibernate.event.spi.*;

import java.io.Serializable;
import java.time.Instant;

public class AuditTableListener implements PreInsertEventListener, PreDeleteEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }

    private void auditEntity(AbstractPreDatabaseOperationEvent event, Audit.Operation operation) {
        if (event.getEntity().getClass() == Audit.class) {
            return;
        }

        Audit audit = Audit.builder()
                .entityId((Serializable) event.getId())
                .entityName(event.getPersister().getEntityName())
                .entityContent(event.getEntity().toString())
                .operation(operation)
                .build();
        event.getSession().persist(audit);
    }
}
