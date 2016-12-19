package doctena

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.util.logging.Log4j

@Log4j
trait AbstractModel {

    @JsonIgnore
    def auditQueueService

    static constraints = {
    }

    abstract Long getId()

    @JsonIgnore
    def onSave = {
        newMap ->
            log.debug "New object created"
            log.debug newMap.toString()
            def event = createAuditEvent(AuditEvent.AuditEventType.CREATE, null, newMap)
            auditQueueService.send(event)
    }

    @JsonIgnore
    def onChange = { oldMap, newMap ->
        log.debug "Object was changed ..."
        log.debug oldMap.toString()
        log.debug newMap.toString()
        def event = createAuditEvent(AuditEvent.AuditEventType.UPDATE, oldMap, newMap)
        auditQueueService.send(event)
    }

    @JsonIgnore
    def onDelete = { oldMap ->
        log.debug "Object was deleted ..."
        log.debug oldMap.toString()
        def event = createAuditEvent(AuditEvent.AuditEventType.DELETE, oldMap, null)
        auditQueueService.send(event)
    }

    AuditEvent createAuditEvent(type, Map oldMap, Map newMap) {
        def event = new AuditEvent()
        event.type = type
        event.actor = "admin"
        event.id = this.getId()
        if (oldMap) {
            event.oldValues = new HashMap()
            oldMap.each { String key, val ->
                if (val instanceof AbstractModel) {
                    event.oldValues[key] = val.toString()
                } else {
                    event.oldValues[key] = val
                }
            }
        }
        if (newMap) {
            event.newValues = new HashMap()
            newMap.each { String key, val ->
                if (val instanceof AbstractModel) {
                    event.newValues[key] = val.toString()
                } else {
                    event.newValues[key] = val
                }
            }
        }
        event.timestamp = new Date()
        event.className = getClass().getSimpleName()
        event
    }

    String toString() {
        return getClass().getSimpleName() + ": " + getId()
    }
}
