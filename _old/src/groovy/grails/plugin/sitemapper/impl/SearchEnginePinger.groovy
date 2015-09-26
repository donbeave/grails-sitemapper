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
package grails.plugin.sitemapper.impl

import grails.plugin.sitemapper.SitemapServerUrlResolver
import org.apache.commons.logging.LogFactory
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.HttpResponseException
import org.apache.http.client.methods.HttpGet
import org.apache.http.params.HttpConnectionParams
import org.apache.http.params.HttpParams
import org.springframework.beans.factory.InitializingBean

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
class SearchEnginePinger implements InitializingBean {

    private static final log = LogFactory.getLog(this)

    SitemapServerUrlResolver sitemapServerUrlResolver
    int pingTimeoutInSeconds = 8

    HttpClient httpClient
    HttpParams httpParams

    // Search engine name => url format with %s for sitemap
    Map<String, String> searchEnginePingUrls = [:]

    boolean pingAll() {
        boolean allSuccess = true
        String sitemapUrl = sitemapServerUrlResolver.serverUrl + "/sitemap.xml"
        searchEnginePingUrls.each { name, urlFormat ->
            if (!pingSearchEngine(name, urlFormat, sitemapUrl)) {
                allSuccess = false
            }
        }

        return allSuccess
    }

    void setSearchEnginePingUrls(Map<String, String> urls) {
        urls.each { name, urlFormat ->
            addSearchEnginePingUrl(name, urlFormat)
        }
    }

    void addSearchEnginePingUrl(String engineName, String urlFormat) {
        log.debug "Adding ping url for $engineName -> $urlFormat"
        searchEnginePingUrls[engineName] = urlFormat
    }

    @Override
    void afterPropertiesSet() {
        httpParams = httpClient.getParams()
        HttpConnectionParams.setConnectionTimeout(httpParams, pingTimeoutInSeconds * 1000)
        HttpConnectionParams.setSoTimeout(httpParams, pingTimeoutInSeconds * 1000);
    }

    private boolean pingSearchEngine(String engineName, String urlFormat, String sitemapUrl) {
        try {
            String pingUrl = String.format(urlFormat, sitemapUrl)
            log.info "Pinging $engineName @ $pingUrl"

            HttpGet httpGet = new HttpGet(pingUrl)
            HttpResponse response = httpClient.execute(httpGet)
            log.info "Pinged $engineName"
            return true
        } catch (HttpResponseException e) {
            log.warn("Unable to ping $engineName. Http response exception, "
                    + "$e.statusCode, message: $e.message", e)
        } catch (IOException e) {
            log.warn "Unable to ping $engineName, e: " + e.message, e
        } catch (ClientProtocolException e) {
            log.warn "Unable to ping $engineName, client-protocol-ex: " + e.message, e
        } catch (Exception e) {
            log.error "Unable to ping $engineName, e: " + e.message, e
        }

        return false
    }

}
