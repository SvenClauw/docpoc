package doctena

import com.amazonaws.services.sqs.model.Message
import com.fasterxml.jackson.databind.ObjectMapper
import grails.transaction.Transactional

@Transactional
class AuditMessageService extends MessageService {

    /**
     * This method will process a message of type AUDIT_EVENT.
     * If the message is not of the correct type the transaction will be rolled back.
     * If the message is of the correct type, the information will be stored in an
     * AuditLog object and stored in the database.
     *
     * @param message: SQS Message containing a JSON representation of an AuditEvent object
     * @return nothing
     * @throws IllegalArgumentException if the message is not of type 'AUDIT_EVENT'
     */
    @Transactional
    protected def processMessage(Message message) {
        ObjectMapper mapper = new ObjectMapper()
        AuditEvent event = mapper.readValue(message.getBody(), AuditEvent.class)
        if (!event.messageType == MessageType.AUDIT_EVENT) {
            log.error "Message not of type AUDIT_EVENT: " + event.messageType
            transactionStatus.setRollbackOnly()
            throw new IllegalArgumentException("Message not of type AUDIT_EVENT: " + event.messageType)
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
            throw new IllegalArgumentException("Message contains errors: " + auditLog.getErrors())
        }
        auditLog.save flush: true
    }
}
