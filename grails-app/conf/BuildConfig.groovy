grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
    inherits('global') {

    }
    log 'warn'
    repositories {
        mavenLocal()
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        compile 'org.apache.httpcomponents:httpclient:4.2.6', {
            excludes 'httpcore', 'commons-logging', 'commons-codec', 'junit', 'mockito-core'
            export = false
        }
        compile 'org.apache.httpcomponents:httpcore:4.2.5', {
            excludes 'junit', 'mockito-core'
            export = false
        }
        test 'org.objenesis:objenesis:2.1', 'cglib:cglib-nodep:3.1', {
            export = false
        }
    }

    plugins {
        build ':release:3.1.1', ':rest-client-builder:2.1.1', {
            export = false
        }

        compile ':tomcat:8.0.22', {
            export = false
        }
    }
}
