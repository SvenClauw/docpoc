package doctena

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
class Appointment implements doctena.AbstractModel {

    static hasOne = [doctor: Doctor, patient: Patient]

    @JsonManagedReference
    Doctor doctor

    @JsonManagedReference
    Patient patient

    def Date date

    static constraints = {
    }

    static auditable = [handlersOnly: true]

    def getName() {
        return date.toString()
    }
}
