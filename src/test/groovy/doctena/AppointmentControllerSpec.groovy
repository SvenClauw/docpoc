package doctena

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AppointmentController)
@Mock(Appointment)
class AppointmentControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        params["doctor"] = new Doctor(firstName: "Doc", lastName: "Doc")
        params["patient"] = new Patient(firstName: "Patient", lastName: "Patient")
        params["date"] = new Date(2016, 12, 12)
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.appointmentList
            model.appointmentCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.appointment!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def appointment = new Appointment()
            appointment.validate()
            controller.save(appointment)

        then:"The create view is rendered again with the correct model"
            model.appointment!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            appointment = new Appointment(params)

            controller.save(appointment)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/appointment/show/1'
            controller.flash.message != null
            Appointment.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def appointment = new Appointment(params)
            controller.show(appointment)

        then:"A model is populated containing the domain instance"
            model.appointment == appointment
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def appointment = new Appointment(params)
            controller.edit(appointment)

        then:"A model is populated containing the domain instance"
            model.appointment == appointment
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/appointment/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def appointment = new Appointment()
            appointment.validate()
            controller.update(appointment)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.appointment == appointment

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            appointment = new Appointment(params).save(flush: true)
            controller.update(appointment)

        then:"A redirect is issued to the show action"
            appointment != null
            response.redirectedUrl == "/appointment/show/$appointment.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/appointment/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def appointment = new Appointment(params).save(flush: true)

        then:"It exists"
            Appointment.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(appointment)

        then:"The instance is deleted"
            Appointment.count() == 0
            response.redirectedUrl == '/appointment/index'
            flash.message != null
    }
}
