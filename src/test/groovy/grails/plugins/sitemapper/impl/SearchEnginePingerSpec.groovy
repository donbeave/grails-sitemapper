/*
 * Copyright 2015 Kim A. Betti, Alexey Zhokhov
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
package grails.plugins.sitemapper.impl

import grails.plugins.sitemapper.SearchEnginePinger
import grails.plugins.sitemapper.SitemapServerUrlResolver
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.apache.http.client.HttpClient
import spock.lang.Specification

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
@TestMixin(GrailsUnitTestMixin)
class SearchEnginePingerSpec extends Specification {

    def "Ping all works as expected"() {

        given:
        String sitemapURI = "http://example.com/sitemap.xml"
        String bingPingURI = "http://www.bing.com/webmaster/ping.aspx?siteMap=%s"
        String expectedURI = "http://www.bing.com/webmaster/ping.aspx?siteMap=" + sitemapURI

        and:
        def pinger = new SearchEnginePinger()
        pinger.addSearchEnginePingUrl "bing", bingPingURI
        pinger.httpClient = Mock(HttpClient)
        pinger.sitemapServerUrlResolver = Mock(SitemapServerUrlResolver)

        when:
        boolean success = pinger.pingAll()

        then:
        success == true
        1 * pinger.sitemapServerUrlResolver.getServerUrl() >> "http://example.com"
        1 * pinger.httpClient.execute({ expectedURI == it.URI.toString() })

    }

}
