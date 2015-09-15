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
package grails.plugin.sitemapper;

import java.util.Date;
import java.util.List;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class NewsExtension implements Extension {

    private String publicationName;
    private String publicationLanguage;
    private NewsAccess access;
    private List<NewsGenre> genres;
    private Date publicationDate;
    private String title;
    private List<String> keywords;
    private List<String> stockTickers;

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getStockTickers() {
        return stockTickers;
    }

    public void setStockTickers(List<String> stockTickers) {
        this.stockTickers = stockTickers;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    public String getPublicationLanguage() {
        return publicationLanguage;
    }

    public void setPublicationLanguage(String publicationLanguage) {
        this.publicationLanguage = publicationLanguage;
    }

    public NewsAccess getAccess() {
        return access;
    }

    public void setAccess(NewsAccess access) {
        this.access = access;
    }

    public List<NewsGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<NewsGenre> genres) {
        this.genres = genres;
    }

}
