package doctena

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
class AuditLogController {

    static allowedMethods = []

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        respond AuditLog.list(params), model: [auditLogCount: AuditLog.count()]
    }

    def show(AuditLog auditLog) {
        respond auditLog
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'auditLog.label', default: 'AuditLog'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
