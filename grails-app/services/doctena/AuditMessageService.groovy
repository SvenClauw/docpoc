package doctena

import com.amazonaws.services.sqs.model.Message
import com.fasterxml.jackson.databind.ObjectMapper
import grails.transaction.Transactional

@Transactional
class AuditMessageService extends MessageService {

    @Transactional
    protected def processMessage(Message message) {
        ObjectMapper mapper = new ObjectMapper()
        AuditEvent event = mapper.readValue(message.getBody(), AuditEvent.class)
        if (!event.messageType == MessageType.AUDIT_EVENT) {
            log.error "Message not of type AUDIT_EVENT: " + event.messageType
            transactionStatus.setRollbackOnly()
            return
        }

        AuditLog auditLog = new AuditLog()
        auditLog.with {
            persistentId = event.id
            className = event.className
            actor = event.actor
            newValues = mapper.writeValueAsString(event.newValues)
            oldValues = mapper.writeValueAsString(event.oldValues)
            timestamp = event.timestamp
            type = event.type
        }
        auditLog.validate()
        if (auditLog.hasErrors()) {
            log.error "Errors found"
            log.error auditLog.getErrors()
            transactionStatus.setRollbackOnly()
        }
        auditLog.save flush: true
    }
}
