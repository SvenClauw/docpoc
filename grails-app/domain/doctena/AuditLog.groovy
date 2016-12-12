package doctena

class AuditLog {

    static constraints = {
        className(nullable: false, blank: false)
        type(nullable: false, blank: false)
        oldValues(nullable: true, blank: false)
        newValues(nullable: true, blank: false)
        timestamp(nullable: false)
        actor(nullabe: false, blank: false)
        persistentId(nullable: false)
    }

    String className

    String actor

    Date timestamp

    // Stored as JSON
    String oldValues

    // Stored as JSON
    String newValues

    Long persistentId

    AuditEvent.AuditEventType type

}
