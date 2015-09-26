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

import grails.plugins.sitemapper.Entry
import grails.plugins.sitemapper.extension.*
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
@TestMixin(GrailsUnitTestMixin)
class XmlEntryWriterSpec extends Specification {

    def entryWriter
    def output

    def setup() {
        output = new StringWriter()
        entryWriter = new XmlEntryWriter(output, 'http://t.com');
    }

    @Unroll("Writing tag #tagName with value #value")
    def "Should be able to format xml tags correctly"() {
        given:
        Appendable output = Mock()
        XmlEntryWriter entryWriter = new XmlEntryWriter(output, 'http://developer-b.com');

        when:
        entryWriter.printTag(tagName, value)

        then:
        1 * output.append(expected)

        where:
        tagName   | value                                                         | expected
        "loc"     | 'http://www.example.com/catalog?item=12&desc=vacation_hawaii' | "<loc>http://www.example.com/catalog?item=12&amp;desc=vacation_hawaii</loc>"
        "escaped" | '''&'"><'''                                                   | "<escaped>&amp;&apos;&quot;&gt;&lt;</escaped>"
    }

    def "Creating simple item with Entry class"() {
        when:
        entryWriter.add new Entry(location: 'http://test.com/about')

        then:
        output.toString() == '<url><loc>http://test.com/about</loc></url>'
    }

    def "Checking last modified date"() {
        when:
        entryWriter.add new Entry(location: '/uri', modifiedAt: new Date(1442378298922))

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><lastmod>2015-09-16T04:38:18+00:00</lastmod></url>'
    }

    def "Checking content change frequency"() {
        when:
        entryWriter.add new Entry(location: '/uri', changeFrequency: 'ALWAYS')

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><changefreq>always</changefreq></url>'
    }

    def "Checking priority - 0"() {
        when:
        entryWriter.add new Entry(location: '/uri', priority: 0)

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><priority>0.0</priority></url>'
    }

    def "Checking priority - 0.7"() {
        when:
        entryWriter.add new Entry(location: '/uri', priority: 0.7)

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><priority>0.7</priority></url>'
    }

    def "Checking expires"() {
        when:
        entryWriter.add new Entry(location: '/uri', expires: new Date(1442378756773))

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><expires>2015-09-16T04:45:56+00:00</expires></url>'
    }

    def "Checking mobile extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Mobile()])

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><mobile:mobile/></url>'
    }

    def "Creating simple entry with alternate link extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new AlternateLink(location: '/test')])

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><xhtml:link rel="alternate" href="http://t.com/test"/></url>'
    }

    def "Checking language in alternate link extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new AlternateLink(
                        location: '/test',
                        language: 'ru-ru'
                )])

        then:
        output.toString() == '<url><loc>http://t.com/uri</loc><xhtml:link rel="alternate" hreflang="ru-ru" href="http://t.com/test"/></url>'
    }

    def "Creating simple entry with PageMap extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [
                        new PageMap([
                                new PageMapDataObject(type: 'action', attributes: [new PageMapDataObjectAttr(name: 'label')])
                        ])
                ])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<PageMap xmlns="http://www.google.com/schemas/sitemap-pagemap/1.0">' +
                '<DataObject type="action">' +
                '<Attribute type="label">' +
                '</Attribute>' +
                '</DataObject>' +
                '</PageMap>' +
                '</url>'
    }

    def "Checking value in PageMap extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [
                        new PageMap([
                                new PageMapDataObject(type: 'action', attributes: [new PageMapDataObjectAttr(name: 'label', value: 'Download')])
                        ])
                ])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<PageMap xmlns="http://www.google.com/schemas/sitemap-pagemap/1.0">' +
                '<DataObject type="action">' +
                '<Attribute type="label">Download</Attribute>' +
                '</DataObject>' +
                '</PageMap>' +
                '</url>'
    }

    def "Checking id in PageMap extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [
                        new PageMap([
                                new PageMapDataObject(id: 'test', type: 'action', attributes: [new PageMapDataObjectAttr(name: 'label', value: 'Download')])
                        ])
                ])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<PageMap xmlns="http://www.google.com/schemas/sitemap-pagemap/1.0">' +
                '<DataObject type="action" id="test">' +
                '<Attribute type="label">Download</Attribute>' +
                '</DataObject>' +
                '</PageMap>' +
                '</url>'
    }

    def "Creating simple entry with image extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Image(
                        location: 'http://example.com/photo.jpg'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<image:image>' +
                '<image:loc>http://example.com/photo.jpg</image:loc>' +
                '</image:image>' +
                '</url>'
    }

    def "Checking caption in image extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Image(
                        location: '/photo.jpg',
                        caption: 'Some caption'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<image:image>' +
                '<image:loc>http://t.com/photo.jpg</image:loc>' +
                '<image:caption>Some caption</image:caption>' +
                '</image:image>' +
                '</url>'
    }

    def "Checking geo location in image extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Image(
                        location: '/photo.jpg',
                        geoLocation: 'Limerick, Ireland'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<image:image>' +
                '<image:loc>http://t.com/photo.jpg</image:loc>' +
                '<image:geo_location>Limerick, Ireland</image:geo_location>' +
                '</image:image></url>'
    }

    def "Checking title in image extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Image(
                        location: '/photo.jpg',
                        title: 'Some title'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<image:image>' +
                '<image:loc>http://t.com/photo.jpg</image:loc>' +
                '<image:title>Some title</image:title>' +
                '</image:image>' +
                '</url>'
    }

    def "Checking license in image extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Image(
                        location: '/photo.jpg',
                        license: 'http://license.com'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<image:image>' +
                '<image:loc>http://t.com/photo.jpg</image:loc>' +
                '<image:license>http://license.com</image:license>' +
                '</image:image>' +
                '</url>'
    }

    def "Creating simple entry with video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 3.00
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>3.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking content location in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 3.00,
                        contentLocation: '/content'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:content_loc>http://t.com/content</video:content_loc>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>3.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking simple player location in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 3.00,
                        playerLocation: new VideoPlayerLocation(
                                location: '/player'
                        )
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:player_loc allow_embed="no">http://t.com/player</video:player_loc>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>3.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking allow embed property in video player location"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 3.00,
                        playerLocation: new VideoPlayerLocation(
                                location: '/player',
                                allowEmbed: true
                        )
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:player_loc allow_embed="yes">http://t.com/player</video:player_loc>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>3.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking auto play property in video player location"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 3.00,
                        playerLocation: new VideoPlayerLocation(
                                location: '/player',
                                autoPlay: 'ap=1'
                        )
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc><video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:player_loc allow_embed="no" autoplay="ap=1">http://t.com/player</video:player_loc>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>3.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking family friendly in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        familyFriendly: false
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>no</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking category in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        category: 'cooking'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:category>cooking</video:category>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking restriction in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        restriction: ['IE', 'GB', 'US', 'CA']
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:restriction relationship="allow">IE GB US CA</video:restriction>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live></video:video>' +
                '</url>'
    }

    def "Checking restriction relationship in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        restriction: ['IE', 'GB', 'US', 'CA'],
                        restrictionRelationship: Relationship.DENY
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:restriction relationship="deny">IE GB US CA</video:restriction>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking gallery location in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        galleryLocation: '/gallery'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:gallery_loc></video:gallery_loc>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking gallery location title in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        galleryLocationTitle: 'Some gallery'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking price in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        price: 30.05
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:price currency="USD" type="own">30.05</video:price>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking price type in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        price: 30.05,
                        priceType: PriceType.RENT
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:price currency="USD" type="rent">30.05</video:price>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking price resolution in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        price: 30.05,
                        priceResolution: Resolution.HD
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:price currency="USD" type="own" resolution="HD">30.05</video:price>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking uploader in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        uploader: 'Uploader name'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:uploader></video:uploader>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking uploader info in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        uploaderInfo: '/uploader'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking platforms in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        platforms: [Platform.WEB, Platform.MOBILE]
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:platform relationship="allow">web mobile</video:platform>' +
                '<video:live>no</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Checking live in video extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new Video(
                        thumbnailLocation: '/photo.jpg',
                        title: 'Some title',
                        description: 'Description of video',
                        duration: 40,
                        rating: 0,
                        live: true
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<video:video>' +
                '<video:thumbnail_loc>http://t.com/photo.jpg</video:thumbnail_loc>' +
                '<video:title>Some title</video:title>' +
                '<video:description>Description of video</video:description>' +
                '<video:duration>40</video:duration>' +
                '<video:rating>0.0</video:rating>' +
                '<video:family_friendly>yes</video:family_friendly>' +
                '<video:requires_subscription>no</video:requires_subscription>' +
                '<video:live>yes</video:live>' +
                '</video:video>' +
                '</url>'
    }

    def "Creating simple entry with news extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new News(
                        publicationName: 'The Example Times',
                        publicationLanguage: 'en',
                        publicationDate: new Date(1442462269668),
                        title: 'Companies A, B in Merger Talks'
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<news:news>' +
                '<news:publication>' +
                '<news:name>The Example Times</news:name>' +
                '<news:language>en</news:language>' +
                '</news:publication>' +
                '<news:publication_date>2015-09-17T03:57:49+00:00</news:publication_date>' +
                '<news:title>Companies A, B in Merger Talks</news:title>' +
                '</news:news>' +
                '</url>'
    }

    def "Checking genres in news extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new News(
                        publicationName: 'The Example Times',
                        publicationLanguage: 'en',
                        publicationDate: new Date(1442462269668),
                        title: 'Companies A, B in Merger Talks',
                        genres: [NewsGenre.PRESS_RELEASE, NewsGenre.BLOG]
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<news:news>' +
                '<news:publication>' +
                '<news:name>The Example Times</news:name>' +
                '<news:language>en</news:language>' +
                '</news:publication>' +
                '<news:genres>PressRelease, Blog</news:genres>' +
                '<news:publication_date>2015-09-17T03:57:49+00:00</news:publication_date>' +
                '<news:title>Companies A, B in Merger Talks</news:title>' +
                '</news:news>' +
                '</url>'
    }

    def "Checking keywords in news extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new News(
                        publicationName: 'The Example Times',
                        publicationLanguage: 'en',
                        publicationDate: new Date(1442462269668),
                        title: 'Companies A, B in Merger Talks',
                        keywords: ['business', 'merger', 'acquisition', 'A', 'B']
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<news:news>' +
                '<news:publication>' +
                '<news:name>The Example Times</news:name>' +
                '<news:language>en</news:language>' +
                '</news:publication>' +
                '<news:publication_date>2015-09-17T03:57:49+00:00</news:publication_date>' +
                '<news:title>Companies A, B in Merger Talks</news:title>' +
                '<news:keywords>business, merger, acquisition, A, B</news:keywords>' +
                '</news:news>' +
                '</url>'
    }

    def "Checking stock stickers in news extension"() {
        when:
        entryWriter.add new Entry(location: '/uri',
                extensions: [new News(
                        publicationName: 'The Example Times',
                        publicationLanguage: 'en',
                        publicationDate: new Date(1442462269668),
                        title: 'Companies A, B in Merger Talks',
                        stockTickers: ['NASDAQ:A', 'NASDAQ:B']
                )])

        then:
        output.toString() == '<url>' +
                '<loc>http://t.com/uri</loc>' +
                '<news:news>' +
                '<news:publication>' +
                '<news:name>The Example Times</news:name>' +
                '<news:language>en</news:language>' +
                '</news:publication>' +
                '<news:publication_date>2015-09-17T03:57:49+00:00</news:publication_date>' +
                '<news:title>Companies A, B in Merger Talks</news:title>' +
                '<news:stock_tickers>NASDAQ:A, NASDAQ:B</news:stock_tickers>' +
                '</news:news>' +
                '</url>'
    }

}