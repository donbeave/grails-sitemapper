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

import grails.plugin.sitemapper.*;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import static grails.plugin.sitemapper.ValidationUtils.*;

/**
 * Responsible for printing sitemap entries as XML to output stream.
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public final class XmlEntryWriter implements EntryWriter {

    public static final Double DEFAULT_PRIORITY = 0.5;

    public static final String LOCATION_TAG = "loc";
    public static final String LAST_MOD_TAG = "lastmod";
    public static final String EXPIRES_TAG = "expires";
    public static final String CHANGE_FREQ_TAG = "changefreq";
    public static final String PRIORITY_TAG = "priority";
    public static final String ALT_LINK = "xhtml:link";
    public static final String DATA_OBJECT_TAG = "DataObject";
    public static final String DATA_OBJECT_ATTR_TAG = "Attribute";

    private static final String URL_OPEN = "<url>";
    private static final String URL_CLOSE = "</url>\n";
    private static final String PAGE_MAP_OPEN = "<PageMap xmlns=\"http://www.google.com/schemas/sitemap-pagemap/1.0\">";
    private static final String PAGE_MAP_CLOSE = "</PageMap>\n";

    private final DateUtils dateUtils = new DateUtils();
    private final Appendable output;
    private final String serverUrl;

    public XmlEntryWriter(Appendable output, String serverUrl) {
        this.serverUrl = serverUrl;
        this.output = output;
    }

    @Override
    public void addEntry(String location, Date modifiedAt) throws IOException {
        addEntry(location, modifiedAt, null, DEFAULT_PRIORITY);
    }

    @Override
    public void addEntry(String location, Date modifiedAt, ContentChangeFrequency changeFrequency, Double priority) throws IOException {
        location = fixLocation(location);

        assertLocation(location);
        assertPriority(priority);

        output.append(URL_OPEN);

        printLocation(location);
        printLastModification(modifiedAt);

        if (changeFrequency != null)
            printChangeFrequency(changeFrequency);

        if (priority != 0.5)
            printPriority(priority);

        output.append(URL_CLOSE);
    }

    @Override
    public void add(Entry entry) throws IOException {
        String location = fixLocation(entry.getLocation());

        assertLocation(location);

        output.append(URL_OPEN);

        printLocation(location);
        printLastModification(entry.getModifiedAt());

        if (entry.getChangeFrequency() != null)
            printChangeFrequency(entry.getChangeFrequency());

        if (entry.getPriority() != 0.5)
            printPriority(entry.getPriority());

        if (entry.getExpires() != null)
            printExpires(entry.getExpires());

        if (entry.getExtensions() != null && !entry.getExtensions().isEmpty()) {
            for (Extension extension : entry.getExtensions()) {
                if (extension instanceof MobileExtension) {
                    printMobile();
                } else if (extension instanceof AlternateLinkExtension) {
                    AlternateLinkExtension item = (AlternateLinkExtension) extension;

                    String itemLocation = fixLocation(item.getLocation());

                    assertLocation(itemLocation);

                    if (item.getLanguage() != null)
                        printAlternateLink(itemLocation, item.getLanguage());
                    else
                        printAlternateLink(itemLocation);
                } else if (extension instanceof PageMapExtension) {
                    PageMapExtension item = (PageMapExtension) extension;

                    output.append(PAGE_MAP_OPEN);

                    assertDataObjects(item.getDataObjects());

                    for (PageMapDataObject dataObject : item.getDataObjects()) {
                        assertDataObjectType(dataObject.getType());

                        String idAttr = "id=\"" + dataObject.getId() + "\"";

                        String value = "";

                        assertDataObjectsAttr(dataObject.getAttributes());

                        for (PageMapDataObjectAttr attr : dataObject.getAttributes()) {
                            value += getDataObjectAttr(attr.getName(), attr.getValue());
                        }

                        printDataObject(dataObject.getType(), idAttr, value);
                    }

                    output.append(PAGE_MAP_CLOSE);
                } else if (extension instanceof ImageExtension) {
                    // TODO https://support.google.com/webmasters/answer/178636
                } else if (extension instanceof VideoExtension) {
                    // TODO https://support.google.com/webmasters/answer/80471?vid=1-635776480131450456-2975046344
                    // https://support.google.com/webmasters/answer/183668?hl=en
                } else if (extension instanceof NewsExtension) {
                    // TODO https://support.google.com/news/publisher/answer/74288
                }
            }
        }

        output.append(URL_CLOSE);
    }

    protected String fixLocation(String url) {
        if (url.startsWith("http://") || url.startsWith("https://"))
            return url;
        else
            return serverUrl + url;
    }

    protected void printLocation(String locationUrl) throws IOException {
        printTag(LOCATION_TAG, locationUrl);
    }

    protected void printLastModification(Date modifiedAt) throws IOException {
        printTag(LAST_MOD_TAG, dateUtils.format(modifiedAt));
    }

    protected void printChangeFrequency(ContentChangeFrequency changeFrequency) throws IOException {
        printTag(CHANGE_FREQ_TAG, changeFrequency.name().toLowerCase());
    }

    protected void printPriority(double priority) throws IOException {
        printTag(PRIORITY_TAG, String.valueOf(priority));
    }

    protected void printExpires(Date expires) throws IOException {
        printTag(EXPIRES_TAG, dateUtils.format(expires));
    }

    protected void printMobile() throws IOException {
        String xml = String.format("<%s/>", "mobile:mobile");
        output.append(xml);
    }

    protected void printAlternateLink(String locationUrl) throws IOException {
        String xml = String.format("<%s rel=\"alternate\" href=\"%s\"/>", ALT_LINK, locationUrl);
        output.append(xml);
    }

    protected void printAlternateLink(String locationUrl, String language) throws IOException {
        String xml = String.format("<%s rel=\"alternate\" hreflang=\"%s\" href=\"%s\"/>", ALT_LINK,
                language.toLowerCase(Locale.ROOT), locationUrl);
        output.append(xml);
    }

    protected void printDataObject(String type, String attrs, String value) throws IOException {
        printTag(DATA_OBJECT_TAG, "type=\"" + type + "\"" + (attrs != null ? " " + attrs : ""), value);
    }

    protected String getDataObjectAttr(String name, String value) throws IOException {
        return String.format("<%s %s>%s</%1$s>", DATA_OBJECT_ATTR_TAG, "type=\"" + name + "\"", escape(value));
    }

    protected void printTag(String tagName, String value) throws IOException {
        String xml = String.format("<%s>%s</%1$s>", tagName, escape(value));
        output.append(xml);
    }

    protected void printTag(String tagName, String attrs, String value) throws IOException {
        String xml = String.format("<%s %s>%s</%1$s>", tagName, attrs, value);
        output.append(xml);
    }

    String escape(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

}