package doctena

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@Integration
@Rollback
class MessagingSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }


    void "test that an audit log is created when an object is created"() {
        when:"an object is created"
        def patient = new Patient(firstName: "Test1", lastName: "Test2")
        patient.save flush:true
        then:"verify that a new audit log object was created"
        sleep(5000)
        def patientId = patient.getId()
        List auditLogs = AuditLog.executeQuery("select distinct log from AuditLog log where log.type='CREATE' and log.persistentId='" + patientId + "'")
        assert auditLogs != null
        assert auditLogs.size() == 1
        AuditLog log = auditLogs.get(0)
        assert log != null
    }

}
