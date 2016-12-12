package doctena

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class DoctorController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Doctor.list(params), model: [doctorCount: Doctor.count()]
    }

    def show(Doctor doctor) {
        respond doctor
    }

    def create() {
        respond new Doctor(params)
    }

    @Transactional
    def save(Doctor doctor) {
        if (doctor == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (doctor.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond doctor.errors, view: 'create'
            return
        }

        doctor.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'doctor.label', default: 'Doctor'), doctor.id])
                redirect doctor
            }
            '*' { respond doctor, [status: CREATED] }
        }
    }

    def edit(Doctor doctor) {
        respond doctor
    }

    @Transactional
    def update(Doctor doctor) {
        if (doctor == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (doctor.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond doctor.errors, view: 'edit'
            return
        }

        doctor.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'doctor.label', default: 'Doctor'), doctor.id])
                redirect doctor
            }
            '*' { respond doctor, [status: OK] }
        }
    }

    @Transactional
    def delete(Doctor doctor) {

        if (doctor == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        doctor.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'doctor.label', default: 'Doctor'), doctor.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'doctor.label', default: 'Doctor'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
