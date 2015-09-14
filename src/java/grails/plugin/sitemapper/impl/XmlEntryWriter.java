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
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public static final String IMAGE_LOCATION_TAG = "image:loc";
    public static final String IMAGE_CAPTION_TAG = "image:caption";
    public static final String IMAGE_GEO_LOCATION_TAG = "image:geo_location";
    public static final String IMAGE_TITLE = "image:title";
    public static final String IMAGE_LICENSE = "image:license";
    public static final String VIDEO_THUMBNAIL_TAG = "video:thumbnail_loc";
    public static final String VIDEO_TITLE_TAG = "video:title";
    public static final String VIDEO_DESCRIPTION_TAG = "video:description";
    public static final String VIDEO_CONTENT_LOCATION_TAG = "video:content_loc";
    public static final String VIDEO_PLAYER_LOCATION_TAG = "video:player_loc";
    public static final String VIDEO_DURATION_TAG = "video:duration";
    public static final String VIDEO_EXPIRATION_DATE_TAG = "video:expiration_date";
    public static final String VIDEO_RATING_TAG = "video:rating";
    public static final String VIDEO_VIEW_COUNT_TAG = "video:view_count";
    public static final String VIDEO_PUBLICATION_DATE_TAG = "video:publication_date";
    public static final String VIDEO_FAMILY_FRIENDLY_TAG = "video:family_friendly";
    public static final String VIDEO_TAG_TAG = "video:tag";
    public static final String VIDEO_CATEGORY_TAG = "video:category";
    public static final String VIDEO_RESTRICTION_TAG = "video:restriction";
    public static final String VIDEO_GALLERY_LOCATION_TAG = "video:gallery_loc";
    public static final String VIDEO_PRICE_TAG = "video:price";
    public static final String VIDEO_REQUIRES_SUBSCRIPTION_TAG = "video:requires_subscription";
    public static final String VIDEO_UPLOADER_TAG = "video:uploader";
    public static final String VIDEO_PLATFORM_TAG = "video:platform";
    public static final String VIDEO_LIVE_TAG = "video:live";

    private static final String URL_OPEN = "<url>";
    private static final String URL_CLOSE = "</url>\n";
    private static final String PAGE_MAP_OPEN = "<PageMap xmlns=\"http://www.google.com/schemas/sitemap-pagemap/1.0\">";
    private static final String PAGE_MAP_CLOSE = "</PageMap>\n";
    private static final String IMAGE_OPEN = "<image:image>";
    private static final String IMAGE_CLOSE = "</image:image>\n";
    private static final String VIDEO_OPEN = "<video:video>";
    private static final String VIDEO_CLOSE = "</video:video>\n";

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
                            assertDataObjectAttrName(attr.getName());

                            value += getDataObjectAttr(attr.getName(), attr.getValue());
                        }

                        printDataObject(dataObject.getType(), idAttr, value);
                    }

                    output.append(PAGE_MAP_CLOSE);
                } else if (extension instanceof ImageExtension) {
                    ImageExtension item = (ImageExtension) extension;

                    String itemLocation = fixLocation(item.getLocation());

                    assertLocation(itemLocation);

                    if (item.getLicense() != null)
                        assertLocation(item.getLicense());

                    printImage(itemLocation, item.getCaption(), item.getGeoLocation(), item.getTitle(), item.getLicense());
                } else if (extension instanceof VideoExtension) {
                    VideoExtension item = (VideoExtension) extension;

                    String thumbnailLocation = fixLocation(item.getThumbnailLocation());
                    String contentLocation = item.getContentLocation() != null ? fixLocation(item.getContentLocation()) : null;
                    String playerLocation = item.getPlayerLocation() != null ? fixLocation(item.getPlayerLocation().getLocation()) : null;
                    String galleryLocation = item.getGalleryLocation() != null ? fixLocation(item.getGalleryLocation()) : null;
                    String uploaderInfo = item.getUploaderInfo() != null ? fixLocation(item.getUploaderInfo()) : null;

                    assertLocation(thumbnailLocation);
                    assertVideoTitle(item.getTitle());
                    assertVideoDescription(item.getDescription());

                    if (contentLocation != null)
                        assertLocation(contentLocation);

                    if (playerLocation != null)
                        assertLocation(playerLocation);

                    if (galleryLocation != null)
                        assertLocation(galleryLocation);

                    assertDuration(item.getDuration());
                    assertRating(item.getRating());

                    List<String> platforms = new ArrayList<>();

                    if (item.getPlatforms() != null) {
                        for (Platform platform : item.getPlatforms())
                            platforms.add(platform.name().toLowerCase());
                    }

                    printVideo(thumbnailLocation, item.getTitle(), item.getDescription(), item.getContentLocation(),
                            item.getPlayerLocation() != null ? item.getPlayerLocation().getLocation() : null,
                            item.getPlayerLocation() != null && item.getPlayerLocation().isAllowEmbed(),
                            item.getPlayerLocation() != null ? item.getPlayerLocation().getAutoPlay() : null,
                            item.getDuration(), item.getExpirationDate(), item.getRating(), item.getViewCount(),
                            item.getPublicationDate(), item.isFamilyFriendly(), item.getCategory(), item.getTags(),
                            item.getRestriction(), item.getRelationship().name(), galleryLocation,
                            item.getGalleryLocationTitle(), item.getPrice(), item.getCurrency().name(),
                            item.getPriceType().name(),
                            item.getPriceResolution() != null ? item.getPriceResolution().name() : null,
                            item.isRequiresSubscription(), item.getUploader(), uploaderInfo, platforms,
                            item.getPlatformsRelationship().name(), item.isLive());
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

    protected void printImage(String itemLocation, String caption, String geoLocation, String title, String license) throws IOException {
        output.append(IMAGE_OPEN);

        printTag(IMAGE_LOCATION_TAG, itemLocation);

        if (StringUtils.isNotEmpty(caption))
            printTag(IMAGE_CAPTION_TAG, caption);

        if (StringUtils.isNotEmpty(geoLocation))
            printTag(IMAGE_GEO_LOCATION_TAG, geoLocation);

        if (StringUtils.isNotEmpty(title))
            printTag(IMAGE_TITLE, title);

        if (StringUtils.isNotEmpty(license))
            printTag(IMAGE_LICENSE, license);

        output.append(IMAGE_CLOSE);
    }

    protected void printVideo(String thumbnailLocation, String title, String description, String contentLocation,
                              String playerLocation, boolean allowEmbed, String autoPlay, int duration,
                              Date expirationDate, double rating, long viewCount, Date publicationDate,
                              boolean familyFriendly, String category, List<String> tags, List<String> restriction,
                              String relationship, String galleryLocation, String galleryLocationTitle, double price,
                              String currency, String priceType, String resolution, boolean requiresSubscription,
                              String uploader, String uploaderInfo, List<String> platforms, String platformsRelationship,
                              boolean live)
            throws IOException {
        output.append(VIDEO_OPEN);

        printTag(VIDEO_THUMBNAIL_TAG, thumbnailLocation);
        printTag(VIDEO_TITLE_TAG, title);
        printTag(VIDEO_DESCRIPTION_TAG, description);

        if (contentLocation != null)
            printTag(VIDEO_CONTENT_LOCATION_TAG, contentLocation);

        if (playerLocation != null) {
            String attrs = "allow_embed=\"" + booleanToString(allowEmbed) + "\"";

            if (autoPlay != null)
                attrs += " autoplay=\"" + escape(autoPlay) + "\"";

            printTag(VIDEO_PLAYER_LOCATION_TAG, attrs, escape(playerLocation));
        }

        printTag(VIDEO_DURATION_TAG, String.valueOf(duration));

        if (expirationDate != null)
            printTag(VIDEO_EXPIRATION_DATE_TAG, dateUtils.format(expirationDate));

        printTag(VIDEO_RATING_TAG, String.valueOf(rating));
        printTag(VIDEO_VIEW_COUNT_TAG, String.valueOf(viewCount));

        if (expirationDate != null)
            printTag(VIDEO_PUBLICATION_DATE_TAG, dateUtils.format(publicationDate));

        printTag(VIDEO_FAMILY_FRIENDLY_TAG, booleanToString(familyFriendly));

        if (category != null)
            printTag(VIDEO_CATEGORY_TAG, category);

        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                printTag(VIDEO_TAG_TAG, tag);
            }
        }

        if (restriction != null && !restriction.isEmpty()) {
            String attrs = "relationship=\"" + relationship.toLowerCase() + "\"";

            printTag(VIDEO_RESTRICTION_TAG, attrs, escape(StringUtils.join(restriction, " ")));
        }

        if (galleryLocation != null) {
            String attrs = galleryLocationTitle != null ? "title=\"" + galleryLocationTitle + "\"" : "";

            printTag(VIDEO_GALLERY_LOCATION_TAG, attrs, escape(galleryLocation));
        }

        if (price > -1) {
            String attrs = "currency=\"" + currency.toUpperCase() + "\"";

            if (priceType != null)
                attrs += " type=\"" + priceType.toLowerCase() + "\"";

            if (resolution != null)
                attrs += " resolution=\"" + resolution.toUpperCase() + "\"";

            printTag(VIDEO_PRICE_TAG, attrs, escape(galleryLocation));
        }

        printTag(VIDEO_REQUIRES_SUBSCRIPTION_TAG, booleanToString(requiresSubscription));

        if (uploader != null) {
            String attrs = uploaderInfo != null ? "info=\"" + uploaderInfo + "\"" : "";

            printTag(VIDEO_UPLOADER_TAG, attrs, escape(uploader));
        }

        if (platforms != null && !platforms.isEmpty()) {
            String attrs = "relationship=\"" + platformsRelationship.toLowerCase() + "\"";

            printTag(VIDEO_PLATFORM_TAG, attrs, escape(StringUtils.join(platforms, ",")));
        }

        printTag(VIDEO_LIVE_TAG, booleanToString(live));

        output.append(VIDEO_CLOSE);
    }

    protected void printTag(String tagName, String value) throws IOException {
        String xml = String.format("<%s>%s</%1$s>", tagName, escape(value));
        output.append(xml);
    }

    protected void printTag(String tagName, String attrs, String value) throws IOException {
        String xml = String.format("<%s %s>%s</%1$s>", tagName, attrs, value);
        output.append(xml);
    }

    String booleanToString(boolean value) {
        return value ? "yes" : "no";
    }

    String escape(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

}