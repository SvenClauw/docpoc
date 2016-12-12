package doctena

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
class Patient implements doctena.AbstractModel {

    String firstName

    String lastName

    static hasMany = [appointments: Appointment]

    def getName() {
        return firstName + " " + lastName
    }

    static constraints = {
    }

    static auditable = [handlersOnly: true]
}
