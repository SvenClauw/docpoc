package doctena

class BootStrap {

    def auditQueueService

    def init = { servletContext ->
        auditQueueService.init()
    }

    def destroy = {
    }
}
