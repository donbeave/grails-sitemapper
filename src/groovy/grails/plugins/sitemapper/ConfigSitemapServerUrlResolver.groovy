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
package grails.plugins.sitemapper

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware

/**
 * Default sitemapServerUrl bean.
 * Looks up server URL in application configuration (grails.serverURL).
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
class ConfigSitemapServerUrlResolver implements SitemapServerUrlResolver, GrailsApplicationAware {

    GrailsApplication grailsApplication

    public String getServerUrl() {
        String serverUrl = getServerUrlFromConfiguration()
        if (serverUrl == null) {
            throw new SitemapperException("Unable to find server url, please set grails.serverURL "
                    + "in Config.groovy, or provided your own implementation of SitemapServerUrlResolver.")
        }

        return removeTrailingSlash(serverUrl)
    }

    protected String getServerUrlFromConfiguration() {
        grailsApplication.config?.grails?.serverURL?.toString()
    }

    protected String removeTrailingSlash(String serverUrl) {
        serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.size() - 1) : serverUrl
    }

}