package doctena

import com.amazonaws.services.sqs.model.Message
import grails.transaction.Transactional

@Transactional
class AuditQueueListenerJob {

    AuditQueueService auditQueueService

    AuditMessageService auditMessageService

    // Poll once every second
    static triggers = {
        simple name: 'Audit trail job', repeatInterval: 1000 // execute job once per second
    }

    def group = 'MessageJob'

    def description = 'Quartz job which polls for messages coming from the audit trail queue'

    /**
     * Receive all messages that are queued on the AuditQueue
     * and process each one by one. When the message was correctly processed
     * it is deleted from the queue.
     */

    def execute() {
        List messages = auditQueueService.receive()
        messages.each {
            Message m = (Message) it
            log.debug "Received message: " + m.getBody()

            auditMessageService.receive(m)

            auditQueueService.deleteMessage(m.getReceiptHandle())
        }
    }
}
