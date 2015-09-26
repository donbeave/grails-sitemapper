/*
 * Copyright 2015 the original author or authors
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
package grails.plugins.sitemapper

import grails.plugins.Plugin
import grails.plugins.sitemapper.impl.XmlSitemapWriter
import groovy.util.logging.Commons
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

/**
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
@Commons
class SitemapperGrailsPlugin extends Plugin {

    def grailsVersion = '3.0.0 > *'

    def title = 'Sitemapper'
    def author = 'Alexey Zhokhov'
    def authorEmail = 'alexey@zhokhov.com'
    def description = 'Autogeneration of sitemaps, see sitemaps.org for more information about sitemaps.'

    def documentation = 'http://grails.org/plugin/sitemapper'

    def license = 'APACHE'

    def organization = [name: 'AZ', url: 'http://www.zhokhov.com']

    def developers = [
            [name: 'Alexey Zhokhov', email: 'alexey@zhokhov.com']
    ]

    def issueManagement = [system: 'github', url: 'https://github.com/donbeave/grails-sitemapper/issues']
    def scm = [system: 'Github Issues', url: 'https://github.com/donbeave/grails-sitemapper']

    Closure doWithSpring() {
        { ->
            // Configure sitemap beans
            grailsApplication.sitemapClasses.each { GrailsSitemapClass sitemapClass ->
                configureSitemapBeans.delegate = delegate
                configureSitemapBeans(sitemapClass)
            }

            sitemapServerUrlResolver(ConfigSitemapServerUrlResolver) { bean ->
                bean.autowire = true
            }

            sitemapWriter(XmlSitemapWriter) { bean ->
                bean.autowire = true
            }

            searchEnginePinger(SearchEnginePinger) { bean ->
                bean.autowire = true
            }
        }
    }

    /**
     * Configure sitemap beans.
     */
    def configureSitemapBeans = { GrailsSitemapClass sitemapClass ->
        def fullName = sitemapClass.fullName

        try {
            "${fullName}Class"(MethodInvokingFactoryBean) {
                targetObject = ref("grailsApplication", false)
                targetMethod = 'getArtefact'
                arguments = [SitemapArtefactHandler.TYPE, fullName]
            }

            "${fullName}"(ref("${fullName}Class")) { bean ->
                bean.factoryMethod = 'newInstance'
                bean.autowire = 'byName'
                bean.scope = 'prototype'
            }
        } catch (Exception e) {
            log.error("Error declaring ${fullName} bean in context", e)
        }
    }

}
