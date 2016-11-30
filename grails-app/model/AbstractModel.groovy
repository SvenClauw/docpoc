package doctena.model

class AbstractModel {

    static constraints = {
    }

    def afterInsert() {
        println("Object inserted:" + this.toString())
    }

    def afterDelete() {
        println("Object deleted:" + this.toString())
    }

    def afterUpdate() {
        println("Object updated:" + this.toString())
    }
}
