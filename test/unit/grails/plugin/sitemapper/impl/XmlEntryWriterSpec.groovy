package grails.plugin.sitemapper.impl

import grails.plugin.sitemapper.Entry
import grails.plugin.sitemapper.extension.AlternateLink
import grails.plugin.sitemapper.extension.Mobile
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
@TestMixin(GrailsUnitTestMixin)
class XmlEntryWriterSpec extends Specification {

    @Unroll("Writing tag #tagName with value #value")
    def "Should be able to format xml tags correctly"() {
        given:
        Appendable output = Mock()
        XmlEntryWriter entryWriter = new XmlEntryWriter(output, "http://developer-b.com");

        when:
        entryWriter.printTag(tagName, value)

        then:
        1 * output.append(expected)

        where:
        tagName   | value                                                         | expected
        "loc"     | 'http://www.example.com/catalog?item=12&desc=vacation_hawaii' | "<loc>http://www.example.com/catalog?item=12&amp;desc=vacation_hawaii</loc>"
        "escaped" | '''&'"><'''                                                   | "<escaped>&amp;&apos;&quot;&gt;&lt;</escaped>"
    }

    def "Check entry object"() {
        given:
        Appendable output = new StringWriter()
        XmlEntryWriter entryWriter = new XmlEntryWriter(output, "http://t.com")

        when:
        entryWriter.add(new Entry(data))

        then:
        output.toString().equals(expected)

        where:
        data                                            | expected
        [location: 'http://test.com/about']             | '<url><loc>http://test.com/about</loc></url>'

        [location  : '/uri',
         modifiedAt: new Date(1442378298922)]           | '<url><loc>http://t.com/uri</loc><lastmod>2015-09-16T04:38:18+00:00</lastmod></url>'

        [location       : '/uri',
         changeFrequency: 'ALWAYS']                     | '<url><loc>http://t.com/uri</loc><changefreq>always</changefreq></url>'

        [location: '/uri',
         priority: 0]                                   | '<url><loc>http://t.com/uri</loc><priority>0.0</priority></url>'

        [location: '/uri',
         priority: 0.7]                                 | '<url><loc>http://t.com/uri</loc><priority>0.7</priority></url>'

        [location: '/uri',
         expires : new Date(1442378756773)]             | '<url><loc>http://t.com/uri</loc><expires>2015-09-16T04:45:56+00:00</expires></url>'

        [location  : '/uri',
         extensions: [new Mobile()]]                    | '<url><loc>http://t.com/uri</loc><mobile:mobile/></url>'

        [location  : '/uri',
         extensions: [
                 new AlternateLink(location: '/test')]] | '<url><loc>http://t.com/uri</loc><xhtml:link rel="alternate" href="http://t.com/test"/></url>'

        [location  : '/uri',
         extensions: [new AlternateLink(
                 location: '/test',
                 language: 'ru-ru'
         )]]                                            | '<url><loc>http://t.com/uri</loc><xhtml:link rel="alternate" hreflang="ru-ru" href="http://t.com/test"/></url>'
    }

    /*
    def "Priority should be between zero and one"() {
        given:
        Appendable output = Mock()
        XmlEntryWriter entryWriter = new XmlEntryWriter(output, null);

        when: entryWriter.printPriority(-0.1)
        then:
        SitemapperException ex = thrown()

        when: entryWriter.printPriority(1.1)
        then: ex = thrown()

        when: entryWriter.printPriority(0.1)
        then: 1 * output.append("<priority>0.1</priority>")
    }
    */

}