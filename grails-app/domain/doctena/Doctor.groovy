package doctena

class Doctor extends doctena.model.AbstractModel {

    def String firstName

    def String lastName

    def Appointment[] appointments = []

    static constraints = {
    }
}
