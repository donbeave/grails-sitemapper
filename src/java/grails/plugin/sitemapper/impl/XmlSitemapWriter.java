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
package grails.plugin.sitemapper.impl;

import grails.plugin.sitemapper.EntryWriter;
import grails.plugin.sitemapper.SitemapServerUrlResolver;
import grails.plugin.sitemapper.Sitemapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public class XmlSitemapWriter extends AbstractSitemapWriter implements InitializingBean {

    private final static Logger log = LoggerFactory.getLogger(XmlSitemapWriter.class);

    private final static String SITEMAP_OPEN = "<sitemap>";
    private final static String SITEMAP_CLOSE = "</sitemap>\n";

    public String path;
    public String extension;

    private GrailsApplication grailsApplication;

    public void setGrailsApplication(GrailsApplication app) {
        grailsApplication = app;
    }

    public void afterPropertiesSet() {
        Properties properties = grailsApplication.getConfig().toProperties();

        path = properties.getProperty("sitemap.prefix");
        extension = properties.getProperty("sitemap.gzip").equals("true") ? "xml.gz" : "xml";
    }

    @Override
    public void writeIndexEntries(PrintWriter writer) throws IOException {
        writeIndexHead(writer);

        SitemapDateUtils dateUtils = new SitemapDateUtils();

        for (Map.Entry<String, Sitemapper> entry : sitemappers.entrySet()) {
            Sitemapper mapper = entry.getValue();
            String mapperName = entry.getKey();

            String serverUrl = getServerUrl(mapper);

            Date previousUpdate = mapper.getPreviousUpdate();

            if (previousUpdate == null) {
                log.debug("No entries found for {}", mapperName);
                continue;
            }

            String lastMod = dateUtils.formatForSitemap(previousUpdate);

            if (mapper instanceof PaginationSitemapper) {
                PaginationSitemapper paginationMapper = (PaginationSitemapper) mapper;
                int count = paginationMapper.getPagesCount();
                for (int i = 0; i < count; i++) {
                    writeIndexExtry(writer, serverUrl, mapperName + "-" + i, lastMod);
                }
            } else {
                writeIndexExtry(writer, serverUrl, mapperName, lastMod);
            }
        }

        writeIndexTail(writer);
    }

    @Override
    public void writeSitemapEntries(PrintWriter writer, Sitemapper sitemapper, int pageNumber) throws IOException {
        writeSitemapHead(writer);
        super.writeSitemapEntries(writer, sitemapper, pageNumber);
        writeSitemapTail(writer);
    }

    @Override
    public void writeSitemapEntries(PrintWriter writer, Sitemapper mapper) throws IOException {
        String serverUrl = getServerUrl(mapper);

        EntryWriter entryWriter = new XmlEntryWriter(writer, serverUrl);
        mapper.withEntryWriter(entryWriter);
    }

    private void writeIndexHead(PrintWriter writer) {
        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.print("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
    }

    private void writeIndexTail(PrintWriter writer) {
        writer.print("</sitemapindex>");
    }

    private void writeIndexExtry(PrintWriter writer, String serverUrl, String mapperName, String lastMod) {
        writer.print(SITEMAP_OPEN);
        writer.print(String.format("<loc>%s/%s.%s.%s</loc>", serverUrl, path, mapperName, extension));
        writer.print(String.format("<lastmod>%s</lastmod>", lastMod));
        writer.print(SITEMAP_CLOSE);
    }

    private void writeSitemapHead(PrintWriter writer) {
        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.print("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
    }

    private void writeSitemapTail(PrintWriter writer) {
        writer.print("</urlset>");
    }

    private String getServerUrl(Sitemapper sitemapper) {
        // override server url for some sitemaps

        if (sitemapper instanceof SitemapServerUrlResolver &&
                ((SitemapServerUrlResolver) sitemapper).getServerUrl() != null) {
            return ((SitemapServerUrlResolver) sitemapper).getServerUrl();
        }

        return serverUrlResolver.getServerUrl();
    }
}
