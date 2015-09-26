package grails.plugin.sitemapper.impl

import grails.plugin.sitemapper.SitemapServerUrlResolver
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
