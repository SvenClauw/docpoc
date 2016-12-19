package doctena

import com.amazonaws.services.sqs.model.Message
import grails.transaction.Transactional

@Transactional
abstract class MessageService {

    @Transactional
    final def receive(Message message) {
        assert message != null
        String body = message.getBody()
        assert body != null

        log.debug body

        processMessage(message)
    }

    protected abstract processMessage(Message message)
}
