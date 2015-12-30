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
package grails.plugins.sitemapper.impl;

import grails.core.GrailsApplication;
import grails.core.GrailsClass;
import grails.plugins.sitemapper.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
public class XmlSitemapWriter extends AbstractSitemapWriter implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(XmlSitemapWriter.class);

    private static final String SITEMAP_OPEN = "<sitemap>";
    private static final String SITEMAP_CLOSE = "</sitemap>\n";

    @Autowired
    GrailsApplication grailsApplication;

    public String path;
    public String extension;

    @Override
    public void afterPropertiesSet() {
        Properties properties = grailsApplication.getConfig().toProperties();

        path = properties.getProperty("sitemap.prefix");
        extension = properties.getProperty("sitemap.extension");

        List<DefaultGrailsSitemapClass> sitemaps = new ArrayList<DefaultGrailsSitemapClass>();

        for (GrailsClass sitemapClass : grailsApplication.getArtefacts(SitemapArtefactHandler.getTYPE())) {
            String fullName = sitemapClass.getFullName();

            DefaultGrailsSitemapClass artefact = (DefaultGrailsSitemapClass)
                    grailsApplication.getMainContext().getBean(fullName + "Class");

            sitemaps.add(artefact);
        }

        setSitemaps(sitemaps);
    }

    @Override
    public void writeIndexEntries(PrintWriter writer) throws IOException {
        writeIndexHead(writer);

        DateUtils dateUtils = new DateUtils();

        for (Map.Entry<String, DefaultGrailsSitemapClass> entry : sitemaps.entrySet()) {
            GrailsSitemapClass mapper = entry.getValue();
            String mapperName = entry.getKey();

            String serverUrl = getServerUrl(mapper);

            Date previousUpdate = mapper.getPreviousUpdate();

            if (previousUpdate == null) {
                log.debug("No entries found for {}", mapperName);
                continue;
            }

            String lastMod = dateUtils.format(previousUpdate);

            long pagesCount = mapper.getPagesCount();

            for (int pageIndex = 0; pageIndex < pagesCount; pageIndex++) {
                writeIndexExtry(writer, serverUrl, mapperName + "-" + pageIndex, lastMod);
            }
        }

        writeIndexTail(writer);
    }

    @Override
    public void writeSitemapEntries(PrintWriter writer, GrailsSitemapClass mapper, Integer pageIndex) throws IOException {
        writeSitemapHead(writer);

        String serverUrl = getServerUrl(mapper);

        EntryWriter entryWriter = new XmlEntryWriter(writer, serverUrl);
        mapper.withEntryWriter(entryWriter, pageIndex, mapper.getOffset(pageIndex));

        writeSitemapTail(writer);
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
        writer.print("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n" +
                "        xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"\n" +
                "        xmlns:content=\"http://www.google.com/schemas/sitemap-content/1.0\"\n" +
                "        xmlns:mobile=\"http://www.google.com/schemas/sitemap-mobile/1.0\"\n" +
                "        xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\"\n" +
                "        xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\"\n" +
                "        xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">\n");
    }

    private void writeSitemapTail(PrintWriter writer) {
        writer.print("</urlset>");
    }

    private String getServerUrl(GrailsSitemapClass sitemapper) {
        // override server url for some sitemaps

        String url = sitemapper.getServerUrl();

        if (!StringUtils.isEmpty(url))
            return url;

        return serverUrlResolver.getServerUrl();
    }

}
