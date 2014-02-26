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

import grails.plugins.sitemapper.impl.XmlSitemapWriter
import org.springframework.util.Assert

import javax.servlet.ServletOutputStream

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

    XmlSitemapWriter sitemapWriter

    /**
     *  The index sitemap
     *  ------------------
     *  Contains uri's to each sitemapper sitemap
     *  + a time stamp indicating the last update.
     */
    def index = {
        response.contentType = "application/xml"

        ServletOutputStream out = response.outputStream
        PrintWriter writer = new PrintWriter(out)

        sitemapWriter.writeIndexEntries(writer)

        writer.flush()
    }

    /**
     * Sitemapper sitemap
     * ------------------
     * Each sitemapper implementation will have its own sitemap file.
     * The entries in this file is populated by calling the mapper.
     */
    def source = {
        response.contentType = "application/xml"

        ServletOutputStream out = response.outputStream
        PrintWriter writer = new PrintWriter(out)

        sitemapWriter.writeSitemapEntries(writer, parseName(params.name), parseNumber(params.name))

        writer.flush()
    }

    private String parseName(String mapperName) {
        Assert.hasLength(mapperName)

        if (mapperName.contains('-')) {
            int dashIndex = mapperName.indexOf('-')
            return mapperName.substring(0, dashIndex)
        } else {
            int dotIndex = mapperName.indexOf('.')
            return dotIndex > 0 ? mapperName.substring(0, dotIndex) : mapperName
        }
    }

    private Integer parseNumber(String mapperName) {
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
