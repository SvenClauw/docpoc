package doctena
/**
 * Created by sclauw on 11/12/2016.
 */
class AuditEvent implements Serializable, DocMessage {

    enum AuditEventType {
        CREATE, UPDATE, DELETE
    }

    AuditEventType type

    String actor

    String className

    Long id

    Map oldValues

    Map newValues

    Date timestamp

    MessageType messageType = MessageType.AUDIT_EVENT

}
