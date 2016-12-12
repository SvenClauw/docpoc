package doctena

/**
 * Created by sclauw on 11/12/2016.
 * Interface representing a message that can be sent through the queues
 */
interface DocMessage {

    MessageType getMessageType()
}