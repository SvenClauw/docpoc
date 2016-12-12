package doctena

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import grails.plugin.awssdk.sqs.AmazonSQSService
import grails.transaction.Transactional

import javax.annotation.PostConstruct

@Transactional
class AuditQueueService extends AmazonSQSService {

    static final QUEUE_NAME = 'audit-queue'

    def send(DocMessage docMessage) {
        ObjectWriter writer = new ObjectMapper().writer()
        String json = writer.writeValueAsString(docMessage)
        sendMessage(QUEUE_NAME, json)
    }

    List receive() {
        receiveMessages(QUEUE_NAME)
    }

    @PostConstruct
    def init() {
        init(QUEUE_NAME)
    }
}
