package ru.gubern.listener;

import org.hibernate.event.spi.*;
import ru.gubern.entity.Audit;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent preDeleteEvent) {
        auditEntity(preDeleteEvent, Audit.Operation.DELETE);
        return false;
    }

    private static void auditEntity(AbstractPreDatabaseOperationEvent preDeleteEvent, Audit.Operation operation) {
        if (preDeleteEvent.getEntity().getClass() != Audit.class) {
            var audit = Audit.builder()
                    .entityId(preDeleteEvent.getId())
                    .entityName(preDeleteEvent.getEntityName())
                    .entityContent(preDeleteEvent.getEntity().toString())
                    .operation(operation)
                    .build();
            preDeleteEvent.getSession().save(audit);
        }
    }

    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {
        auditEntity(preInsertEvent, Audit.Operation.INSERT);
        return false;
    }
}
