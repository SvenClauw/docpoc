package doctena

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AppointmentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Appointment.list(params), model:[appointmentCount: Appointment.count()]
    }

    def show(Appointment appointment) {
        respond appointment
    }

    def create() {
        respond new Appointment(params)
    }

    @Transactional
    def save(Appointment appointment) {
        if (appointment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (appointment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond appointment.errors, view:'create'
            return
        }

        appointment.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'appointment.label', default: 'Appointment'), appointment.id])
                redirect appointment
            }
            '*' { respond appointment, [status: CREATED] }
        }
    }

    def edit(Appointment appointment) {
        respond appointment
    }

    @Transactional
    def update(Appointment appointment) {
        if (appointment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (appointment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond appointment.errors, view:'edit'
            return
        }

        appointment.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'appointment.label', default: 'Appointment'), appointment.id])
                redirect appointment
            }
            '*'{ respond appointment, [status: OK] }
        }
    }

    @Transactional
    def delete(Appointment appointment) {

        if (appointment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        appointment.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'appointment.label', default: 'Appointment'), appointment.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
