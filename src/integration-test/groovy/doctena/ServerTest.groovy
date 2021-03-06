package doctena

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class ServerTest extends GebSpec  {

    def setup() {
    }

    def cleanup() {
    }

    void "test the main page is correctly loaded"() {
        when:"The home page is visited"
            go '/'

        then:"The title is correct"
            title == "Welcome to Grails"
    }

}
