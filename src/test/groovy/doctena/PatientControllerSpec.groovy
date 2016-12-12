package doctena

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PatientController)
@Mock(Patient)
class PatientControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        params["firstName"] = 'John'
        params["lastName"] = 'Patientson'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.patientList
            model.patientCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.patient!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def patient = new Patient()
            patient.validate()
            controller.save(patient)

        then:"The create view is rendered again with the correct model"
            model.patient!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            patient = new Patient(params)

            controller.save(patient)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/patient/show/1'
            controller.flash.message != null
            Patient.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def patient = new Patient(params)
            controller.show(patient)

        then:"A model is populated containing the domain instance"
            model.patient == patient
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def patient = new Patient(params)
            controller.edit(patient)

        then:"A model is populated containing the domain instance"
            model.patient == patient
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/patient/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def patient = new Patient()
            patient.validate()
            controller.update(patient)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.patient == patient

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            patient = new Patient(params).save(flush: true)
            controller.update(patient)

        then:"A redirect is issued to the show action"
            patient != null
            response.redirectedUrl == "/patient/show/$patient.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/patient/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def patient = new Patient(params).save(flush: true)

        then:"It exists"
            Patient.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(patient)

        then:"The instance is deleted"
            Patient.count() == 0
            response.redirectedUrl == '/patient/index'
            flash.message != null
    }
}
