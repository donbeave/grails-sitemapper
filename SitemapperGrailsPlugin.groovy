import grails.plugins.sitemapper.ConfigSitemapServerUrlResolver
import grails.plugins.sitemapper.artefact.SitemapperArtefactHandler
import grails.plugins.sitemapper.impl.SearchEnginePinger
import grails.plugins.sitemapper.impl.XmlSitemapWriter
import grails.util.Holders
import org.apache.http.impl.client.DefaultHttpClient

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class SitemapperGrailsPlugin {

    def version = '0.7.1.2'
    def grailsVersion = '1.3.0 > *'
    def dependsOn = [:]
    def pluginExcludes = [
            'web-app/css',
            'web-app/images',
            'web-app/js/application.js',
            'grails-app/views/error.gsp',
            '**/demo/**'
    ]

    def title = 'Sitemapper'
    def author = 'Kim A. Betti, Alexey Zhokhov'
    def authorEmail = 'kim@developer-b.com'
    def description = 'Autogeneration of sitemaps, see sitemaps.org for more information about sitemaps.'

    def documentation = 'https://www.github.com/kimble/grails-sitemapper'

    def license = 'APACHE'
    def developers = [
            [name: 'Kim A. Betti', email: 'kim@developer-b.com'],
            [name: 'Alexey Zhokhov', email: 'donbeave@gmail.com']]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/donbeave/grails-sitemapper/issues']
    def scm = [url: 'https://github.com/donbeave/grails-sitemapper/']

    def artefacts = [SitemapperArtefactHandler]

    def watchedResources = [
            'file:./grails-app/sitemaps/**/*Sitemapper.groovy',
            'file:./plugins/*/grails-app/sitemaps/**/*Sitemapper.groovy'
    ]

    def doWithSpring = {
        application.sitemapperClasses.each { mapperClass ->
            log.debug "Registering sitemapper class ${mapperClass.name} as sitemapper / bean"
            "${mapperClass.name}Sitemapper"(mapperClass.clazz) { bean ->
                bean.autowire = true
            }
        }

        sitemapServerUrlResolver(ConfigSitemapServerUrlResolver)
        sitemapWriter(XmlSitemapWriter) { bean ->
            bean.autowire = true
        }

        searchEnginePinger(SearchEnginePinger) {
            searchEnginePingUrls = Holders.config?.searchEnginePingUrls ?: [:]
            sitemapServerUrlResolver = ref('sitemapServerUrlResolver')
            httpClient = new DefaultHttpClient()
        }
    }

    def onChange = { event ->
        if (application.isArtefactOfType(SitemapperArtefactHandler.TYPE, event.source)) {
            def mapperClass = application.addArtefact(SitemapperArtefactHandler.TYPE, event.source)
            def beanDefinitions = beans {

                // Redefine the sitemapper bean
                "${mapperClass.name}Sitemapper"(mapperClass.clazz) { bean ->
                    bean.autowire = true
                }

                // Contains references to the sitemappers so
                // it has to be re-defined as well.
                sitemapWriter(XmlSitemapWriter) { bean ->
                    bean.autowire = true
                }

            }

            beanDefinitions.registerBeans(event.ctx)
        }
    }

}
