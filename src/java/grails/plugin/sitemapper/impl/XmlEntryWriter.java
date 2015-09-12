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

import grails.plugin.sitemapper.ContentChangeFrequency;
import grails.plugin.sitemapper.EntryWriter;
import grails.plugin.sitemapper.SitemapperException;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Responsible for printing sitemap entries as XML to output stream.
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public final class XmlEntryWriter implements EntryWriter {

    public static final String LOCATION_TAG = "loc";
    public static final String LAST_MOD_TAG = "lastmod";
    public static final String CHANGE_FREQ_TAG = "changefreq";
    public static final String PRIORITY_TAG = "priority";
    private static final String URL_OPEN = "<url>";
    private static final String URL_CLOSE = "</url>\n";
    private final SitemapDateUtils dateUtils = new SitemapDateUtils();
    private final Appendable output;
    private final String serverUrl;

    public XmlEntryWriter(Appendable output, String serverUrl) {
        this.serverUrl = serverUrl;
        this.output = output;
    }

    @Override
    public void addEntry(String location, Date modifiedAt) throws IOException {
        output.append(URL_OPEN);
        printLocation(location);
        printLastModification(modifiedAt);
        output.append(URL_CLOSE);
    }

    @Override
    public void addEntry(String location, Date modifiedAt, ContentChangeFrequency changeFrequency, double priority) throws IOException {
        output.append(URL_OPEN);
        printLocation(location);
        printLastModification(modifiedAt);
        printChangeFrequency(changeFrequency.toString().toLowerCase());
        printPriority(priority);
        output.append(URL_CLOSE);
    }

    protected void printLocation(String locationUrl) throws IOException {
        if (locationUrl.startsWith("http://") || locationUrl.startsWith("https://")) {
            printTag(LOCATION_TAG, locationUrl);
        } else {
            printTag(LOCATION_TAG, serverUrl + locationUrl);
        }
    }

    protected void printLastModification(Date modifiedAt) throws IOException {
        printTag(LAST_MOD_TAG, dateUtils.formatForSitemap(modifiedAt));
    }

    protected void printChangeFrequency(String changeFrequency) throws IOException {
        printTag(CHANGE_FREQ_TAG, changeFrequency);
    }

    protected void printPriority(double priority) throws IOException {
        if (priority < 0 || priority > 1) {
            throw new SitemapperException("Priority has to be between 0 and 1, not " + priority);
        }

        printTag(PRIORITY_TAG, String.valueOf(priority));
    }

    protected void printTag(final String tagName, final String value) throws IOException {
        String escapedValue = StringEscapeUtils.escapeXml(value);
        String xml = String.format("<%s>%s</%1$s>", tagName, escapedValue);
        output.append(xml);
    }

}