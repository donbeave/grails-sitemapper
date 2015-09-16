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
package grails.plugin.sitemapper.extension;

import grails.plugin.sitemapper.Extension;

import java.util.Date;
import java.util.List;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class Video implements Extension {

    private String thumbnailLocation;
    private String title;
    private String description;
    private String contentLocation;
    private VideoPlayerLocation playerLocation;
    private int duration;
    private Date expirationDate;
    private double rating;
    private long viewCount = -1;
    private Date publicationDate;
    private boolean familyFriendly = true;
    private String category;
    private List<String> tags;
    private List<String> restriction;
    private Relationship relationship = Relationship.ALLOW;
    private String galleryLocation;
    private String galleryLocationTitle;
    private double price = -1;
    private PriceType priceType = PriceType.OWN;
    private Resolution priceResolution;
    private Currency currency = Currency.USD;
    private boolean requiresSubscription;
    private String uploader;
    private String uploaderInfo;
    private List<Platform> platforms;
    private Relationship platformsRelationship = Relationship.ALLOW;
    private boolean live;

    public String getThumbnailLocation() {
        return thumbnailLocation;
    }

    public void setThumbnailLocation(String thumbnailLocation) {
        this.thumbnailLocation = thumbnailLocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public VideoPlayerLocation getPlayerLocation() {
        return playerLocation;
    }

    public void setPlayerLocation(VideoPlayerLocation playerLocation) {
        this.playerLocation = playerLocation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isFamilyFriendly() {
        return familyFriendly;
    }

    public void setFamilyFriendly(boolean familyFriendly) {
        this.familyFriendly = familyFriendly;
    }

    public List<String> getRestriction() {
        return restriction;
    }

    public void setRestriction(List<String> restriction) {
        this.restriction = restriction;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public String getGalleryLocation() {
        return galleryLocation;
    }

    public void setGalleryLocation(String galleryLocation) {
        this.galleryLocation = galleryLocation;
    }

    public String getGalleryLocationTitle() {
        return galleryLocationTitle;
    }

    public void setGalleryLocationTitle(String galleryLocationTitle) {
        this.galleryLocationTitle = galleryLocationTitle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isRequiresSubscription() {
        return requiresSubscription;
    }

    public void setRequiresSubscription(boolean requiresSubscription) {
        this.requiresSubscription = requiresSubscription;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUploaderInfo() {
        return uploaderInfo;
    }

    public void setUploaderInfo(String uploaderInfo) {
        this.uploaderInfo = uploaderInfo;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public Resolution getPriceResolution() {
        return priceResolution;
    }

    public void setPriceResolution(Resolution priceResolution) {
        this.priceResolution = priceResolution;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public Relationship getPlatformsRelationship() {
        return platformsRelationship;
    }

    public void setPlatformsRelationship(Relationship platformsRelationship) {
        this.platformsRelationship = platformsRelationship;
    }
}
