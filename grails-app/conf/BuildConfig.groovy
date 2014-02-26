grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
    }
    dependencies {
//        compile 'org.apache.httpcomponents:httpclient:4.1.1'
        test 'org.objenesis:objenesis:1.2', 'cglib:cglib-nodep:2.2'
        test 'org.spockframework:spock-grails-support:0.7-groovy-2.0'
    }
    plugins {
        build ':tomcat:7.0.50.1', ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }

        test(':spock:0.7') {
            exclude 'spock-grails-support'
        }
    }
}
