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
package grails.plugins.sitemapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.Assert

import javax.servlet.http.HttpServletResponse
import java.util.zip.GZIPOutputStream

/**
 * Generates sitemaps on the fly. The XML output is piped directly to
 * response.output to avoid unnecessary object generation and memory usage.
 * The drawback is that exceptions can stop the sitemap generation
 * "mid stream" so test your sitemappers.
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class SitemapperController {

    static PrintWriter getGzipWriter(HttpServletResponse response) throws IOException {
        return new PrintWriter(new GZIPOutputStream(response.outputStream))
    }

    def sitemapWriter

    private Set<Sitemapper> sitemappers

    /**
     *  The index sitemap
     *  ------------------
     *  Contains uri's to each sitemapper sitemap
     *  + a time stamp indicating the last update.
     */
    def index() {
        PrintWriter writer

        if (grailsApplication.config.sitemap.gzip) {
            response.contentType = 'application/gzip'

            writer = getGzipWriter(response)
        } else {
            response.contentType = 'application/xml'

            writer = new PrintWriter(response.outputStream)
        }

        sitemapWriter.sitemappers = sitemappers
        sitemapWriter.writeIndexEntries(writer)

        writer.flush()
        writer.close()
    }

    /**
     * Sitemapper sitemap
     * ------------------
     * Each sitemapper implementation will have its own sitemap file.
     * The entries in this file is populated by calling the mapper.
     */
    def source(String name) {
        PrintWriter writer

        if (grailsApplication.config.sitemap.gzip) {
            response.contentType = 'application/gzip'

            writer = getGzipWriter(response)
        } else {
            response.contentType = 'application/xml'

            writer = new PrintWriter(response.outputStream)
        }

        sitemapWriter.sitemappers = sitemappers
        sitemapWriter.writeSitemapEntries(writer, parseName(name), parseNumber(name))

        writer.flush()
        writer.close()
    }

    @Autowired(required = false)
    private void setSitemappers(Set<Sitemapper> newMappers) {
        this.sitemappers = newMappers
    }

    private String parseName(String mapperName) {
        Assert.hasLength(mapperName)

        if (mapperName.contains('-')) {
            int dashIndex = mapperName.indexOf('-')
            return mapperName.substring(0, dashIndex)
        }

        int dotIndex = mapperName.indexOf('.')
        return dotIndex > 0 ? mapperName.substring(0, dotIndex) : mapperName
    }

    private int parseNumber(String mapperName) {
        Assert.hasLength(mapperName)

        if (mapperName.contains('-')) {
            int dashIndex = mapperName.indexOf('-')
            int dotIndex = mapperName.indexOf('.')
            String num = mapperName.substring(dashIndex + 1, dotIndex > -1 ? dotIndex : mapperName.size())
            return Integer.parseInt(num)
        }

        return 0;
    }

}
