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
package grails.plugins.sitemapper;

import org.grails.core.AbstractGrailsClass;

import java.util.Date;

import static grails.plugins.sitemapper.GrailsSitemapClassConstants.*;

/**
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
public class DefaultGrailsSitemapClass extends AbstractGrailsClass implements GrailsSitemapClass {

    public static final String SITEMAP = "Sitemap";

    public DefaultGrailsSitemapClass(Class clazz) {
        super(clazz, SITEMAP);
    }

    @Override
    public Date getPreviousUpdate() {
        Date date = getPropertyOrStaticPropertyOrFieldValue(PREVIOUS_UPDATE, Date.class);
        if (date == null) return new Date();
        return date;
    }

    @Override
    public void withEntryWriter(EntryWriter entryWriter, Integer pageIndex, Integer offset) {
        getMetaClass().invokeMethod(getReferenceInstance(), WITH_ENTRY_WRITER, new Object[]{entryWriter, pageIndex, offset});
    }

    @Override
    public Integer getPerPageCount() {
        return (Integer) getMetaClass().invokeMethod(getReferenceInstance(), GET_PER_PAGE_COUNT, new Object[]{});
    }

    @Override
    public Long getTotalCount() {
        return (Long) getMetaClass().invokeMethod(getReferenceInstance(), GET_TOTAL_COUNT, new Object[]{});
    }

    @Override
    public Integer getPagesCount() {
        return (int) Math.ceil((double) getTotalCount() / getPerPageCount());
    }

    @Override
    public Integer getOffset(Integer pageIndex) {
        return pageIndex * getPerPageCount();
    }

    @Override
    public String getServerUrl() {
        if (getReferenceInstance() instanceof SitemapServerUrlResolver) {
            return (String) getMetaClass().invokeMethod(getReferenceInstance(), GET_SERVER_URL, new Object[]{});
        } else {
            return null;
        }
    }

}
