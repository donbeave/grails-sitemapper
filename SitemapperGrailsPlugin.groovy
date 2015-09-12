/*
 * Copyright 2010 Kim A. Betti, Alexey Zhokhov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import grails.plugin.sitemapper.ConfigSitemapServerUrlResolver
import grails.plugin.sitemapper.artefact.SitemapperArtefactHandler
import grails.plugin.sitemapper.impl.SearchEnginePinger
import grails.plugin.sitemapper.impl.XmlSitemapWriter
import grails.util.Environment
import org.apache.http.impl.client.DefaultHttpClient

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class SitemapperGrailsPlugin {

    def version = '0.9-SNAPSHOT'
    def grailsVersion = '2.0 > *'
    def pluginExcludes = [
            'lib/**',
            '**/demo/**'
    ]
    def title = 'Sitemapper'
    def description = 'Autogeneration of sitemaps, see sitemaps.org for more information about sitemaps.'
    def documentation = 'http://grails.org/plugin/sitemapper'
    def license = 'APACHE'
    def developers = [
            [name: 'Kim A. Betti', email: 'kim@developer-b.com'],
            [name: 'Alexey Zhokhov', email: 'donbeave@gmail.com']]
    def organization = [name: 'Polusharie', url: 'http://www.polusharie.com']
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/donbeave/grails-sitemapper/issues']
    def scm = [url: 'https://github.com/donbeave/grails-sitemapper/']

    def artefacts = [SitemapperArtefactHandler]

    def watchedResources = [
            'file:./grails-app/sitemaps/**/*Sitemapper.groovy',
            'file:./plugins/*/grails-app/sitemaps/**/*Sitemapper.groovy'
    ]

    def doWithSpring = {
        loadSitemapConfig(application.config)

        application.sitemapperClasses.each { mapperClass ->
            log.debug "Registering sitemapper class ${mapperClass.name} as sitemapper / bean"
            "${mapperClass.name}Sitemapper"(mapperClass.clazz) { bean ->
                bean.autowire = true
            }
        }

        sitemapServerUrlResolver(ConfigSitemapServerUrlResolver) {
            grailsApplication = ref('grailsApplication')
        }

        sitemapWriter(XmlSitemapWriter) { bean ->
            bean.autowire = true
        }

        searchEnginePinger(SearchEnginePinger) {
            searchEnginePingUrls = application.config.searchEnginePingUrls ?: [:]
            sitemapServerUrlResolver = ref('sitemapServerUrlResolver')
            httpClient = new DefaultHttpClient()
        }
    }

    def onChange = { event ->
        if (!application.isArtefactOfType(SitemapperArtefactHandler.TYPE, event.source)) {
            return
        }

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

    private ConfigObject loadSitemapConfig(ConfigObject config) {
        def classLoader = new GroovyClassLoader(getClass().classLoader)
        String environment = Environment.current.name

        // Note here the order of objects when calling merge - merge OVERWRITES values in the target object
        // Load default config as a basis
        def newConfig = new ConfigSlurper(environment).parse(
                classLoader.loadClass('DefaultSitemapConfig')
        )

        // Overwrite defaults with what Config.groovy has supplied, perhaps from external files
        newConfig.merge(config)

        // Overwrite with contents of SitemapConfig
        try {
            newConfig.merge(new ConfigSlurper(environment).parse(
                    classLoader.loadClass('SitemapConfig'))
            )
        } catch (Exception ignored) {
            // ignore, just use the defaults
        }

        // Now merge our correctly merged DefaultSitemapConfig and SitemapConfig into the main config
        config.merge(newConfig)

        config.sitemap
    }

}
