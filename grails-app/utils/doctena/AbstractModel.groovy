package doctena

import com.fasterxml.jackson.annotation.JsonIgnore

trait AbstractModel {

    @JsonIgnore
    def auditQueueService

    static constraints = {
    }

    abstract Long getId()

    @JsonIgnore
    def onSave = {
        newMap ->
            println "New object created"
            println newMap
            def event = createEvent(AuditEvent.AuditEventType.CREATE, null, newMap)
            auditQueueService.send(event)
    }

    @JsonIgnore
    def onChange = { oldMap, newMap ->
        println "Person was changed ..."
        println oldMap
        println newMap
        def event = createEvent(AuditEvent.AuditEventType.UPDATE, oldMap, newMap)
        auditQueueService.send(event)
    }

    @JsonIgnore
    def onDelete = { oldMap ->
        println "Object was deleted ..."
        println oldMap
        def event = createEvent(AuditEvent.AuditEventType.DELETE, oldMap, null)
        auditQueueService.send(event)
    }

    AuditEvent createEvent(type, Map oldMap, Map newMap) {
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
        println event
        event
    }

    String toString() {
        return getClass().getSimpleName() + ": " + getId()
    }
}
