package doctena

import com.amazonaws.services.sqs.model.Message
import grails.transaction.Transactional

@Transactional
abstract class MessageService {

    /**
     * Receive method that accepts an SQS Message.
     * It asserts the message and its body are not null.
     *
     * @param message
     * @return
     * @throws IllegalArgumentException if the message or its body is null
     */
    @Transactional
    final def receive(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is null")
        }
        String body = message.getBody()
        if (body == null) {
            throw new IllegalArgumentException("Message body is null")
        }

        log.debug body

        processMessage(message)
    }

    protected abstract processMessage(Message message)
}
