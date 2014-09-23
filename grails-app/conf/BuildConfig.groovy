grails.project.work.dir = "target"

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compile 'org.apache.httpcomponents:httpclient:4.2.5', {
            excludes 'httpcore', 'commons-logging', 'commons-codec', 'junit', 'mockito-core'
            export = false
        }
        compile 'org.apache.httpcomponents:httpcore:4.2.4', {
            excludes 'junit', 'mockito-core'
            export = false
        }
        test 'org.objenesis:objenesis:1.2', 'cglib:cglib-nodep:2.2', {
            export = false
        }
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
