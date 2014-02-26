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

import grails.util.Holders

/**
 * Default sitemapServerUrl bean.
 * Looks up server URL in application configuration (grails.serverURL).
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class ConfigSitemapServerUrlResolver implements SitemapServerUrlResolver {

    public String getServerUrl() {
        String serverUrl = getServerUrlFromConfiguration()
        if (serverUrl == null) {
            throw new SitemapperException("Unable to find server url, please set grails.serverURL "
                    + "in Config.groovy, or provided your own implementation of SitemapServerUrlResolver.")
        }

        return removeTrailingSlash(serverUrl)
    }

    protected String getServerUrlFromConfiguration() {
        def serverUrl = Holders.config?.grails?.serverURL

        if (serverUrl) {
            return Holders.config?.grails?.serverURL?.toString()
        } else {
            Integer port = System.getProperty('server.port', '8080').toInteger()
            String host = System.getProperty('server.host', 'localhost')
            String portPostfix = port != 80 ? ":${port}" : ''
            return "http://${host}${portPostfix}"
        }
    }

    protected String removeTrailingSlash(String serverUrl) {
        serverUrl.endsWith('/') ? serverUrl.substring(0, serverUrl.size() - 1) : serverUrl
    }

}