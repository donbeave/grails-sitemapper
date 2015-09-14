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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public final class ValidationUtils {

    public static void assertLocation(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new SitemapperException(e.getMessage(), e);
        }
    }

    public static void assertPriority(double priority) {
        if (priority < 0 || priority > 1)
            throw new SitemapperException("Priority has to be between 0 and 1, not " + priority);
    }

    public static void assertDataObjects(List<PageMapDataObject> dataObjectList) {
        if (dataObjectList == null || dataObjectList.isEmpty())
            throw new SitemapperException("Data objects can not be null or empty");
    }

    public static void assertDataObjectType(String type) {
        if (type == null || type.isEmpty())
            throw new SitemapperException("Data object's type not specified");
    }

    public static void assertDataObjectsAttr(List<PageMapDataObjectAttr> attrs) {
        if (attrs == null || attrs.isEmpty())
            throw new SitemapperException("Data object attributes can not be null or empty");
    }

    public static void assertDataObjectAttrName(String name) {
        if (name == null || name.isEmpty())
            throw new SitemapperException("Data object attr's name not specified");
    }

    public static void assertVideoTitle(String title) {
        if (title == null || title.isEmpty())
            throw new SitemapperException("Data object's type not specified");
    }

    public static void assertVideoDescription(String description) {
        if (description == null || description.isEmpty())
            throw new SitemapperException("Data object's type not specified");
    }

    public static void assertDuration(int duration) {
        if (duration < 0 || duration > 1)
            throw new SitemapperException("Duration has to be between 0 and 28800, not " + duration);
    }

    public static void assertRating(double rating) {
        if (rating < 0 || rating > 1)
            throw new SitemapperException("Rating has to be between 0.0 and 5.0, not " + rating);
    }

}
