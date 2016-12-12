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
            println "Message not of type AUDIT_EVENT: " + event.messageType
            transactionStatus.setRollbackOnly()
            return
        }


        AuditLog log = new AuditLog()
        log.with {
            persistentId = event.id
            className = event.className
            actor = event.actor
            newValues = mapper.writeValueAsString(event.newValues)
            oldValues = mapper.writeValueAsString(event.oldValues)
            timestamp = event.timestamp
            type = event.type
        }
        log.validate()
        if (log.hasErrors()) {
            println "Errors found"
            println log.getErrors()
            transactionStatus.setRollbackOnly()
        }
        log.save flush: true
    }
}
