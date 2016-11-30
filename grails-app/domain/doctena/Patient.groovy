package doctena

class Patient extends doctena.model.AbstractModel {

    def String firstName

    def String lastName

    def Appointment[] appointments = []

    static constraints = {
    }
}
