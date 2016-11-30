package doctena

class Appointment extends doctena.model.AbstractModel {

    def Doctor doctor

    def Patient patient

    def Date date

    static constraints = {
    }
}
