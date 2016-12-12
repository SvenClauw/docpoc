package doctena

import com.amazonaws.services.sqs.model.Message
import grails.transaction.Transactional

@Transactional
class AuditQueueListenerJob {

    AuditQueueService auditQueueService

    AuditMessageService auditMessageService

    static triggers = {
        simple name: 'Audit trail job', repeatInterval: 1000 // execute job once in 5 seconds
    }

    def group = 'MessageJob'

    def description = 'Quartz job which polls for messages coming from the audit trail queue'

    def execute() {
        List messages = auditQueueService.receive()
        messages.each {
            Message m = (Message) it
            println "Received message: " + m.getBody()
            auditMessageService.receive(m)

            auditQueueService.deleteMessage(m.getReceiptHandle())
        }
    }
}
