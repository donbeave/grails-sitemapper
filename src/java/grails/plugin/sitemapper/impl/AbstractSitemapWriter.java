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

import static grails.plugin.sitemapper.artefact.SitemapperArtefactHandler.SUFFIX;
import grails.plugin.sitemapper.SitemapServerUrlResolver;
import grails.plugin.sitemapper.Sitemapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public abstract class AbstractSitemapWriter {

    protected SitemapServerUrlResolver serverUrlResolver;
    protected Map<String, Sitemapper> sitemappers = new HashMap<String, Sitemapper>();

    public abstract void writeIndexEntries(PrintWriter writer) throws IOException;

    public void writeSitemapEntries(PrintWriter writer, String sourceName, int pageNumber)
            throws IOException {
        Sitemapper mapper = getMapperByName(sourceName);

        writeSitemapEntries(writer, mapper, pageNumber);
    }

    public void writeSitemapEntries(PrintWriter writer, Sitemapper sitemapper, int pageNumber)
            throws IOException {
        if (sitemapper instanceof PaginationSitemapper) {
            ((PaginationSitemapper) sitemapper).setPageIndex(pageNumber);
        }

        writeSitemapEntries(writer, sitemapper);
    }

    protected Sitemapper getMapperByName(String name) {
        String mapperName = name.toLowerCase();
        Sitemapper mapper = sitemappers.get(mapperName);
        Assert.notNull(mapper, "Unable to find source with name " + name);
        return mapper;
    }

    public abstract void writeSitemapEntries(PrintWriter writer, Sitemapper m) throws IOException;

    public Map<String, Sitemapper> getSitemappers() {
        return sitemappers;
    }

    @Required
    @Autowired
    public void setSitemappers(Set<Sitemapper> newMappers) {
        sitemappers.clear();
        for (Sitemapper mapper : newMappers) {
            String mapperName = getMapperName(mapper.getClass());
            sitemappers.put(mapperName, mapper);
        }
    }

    protected String getMapperName(Class<? extends Sitemapper> sitemapperClass) {
        String className = sitemapperClass.getSimpleName();
        Assert.isTrue(className.endsWith(SUFFIX));
        int endIndex = className.length() - SUFFIX.length();
        return className.substring(0, endIndex).toLowerCase();
    }

    @Required
    public void setSitemapServerUrlResolver(SitemapServerUrlResolver serverUrlResolver) {
        this.serverUrlResolver = serverUrlResolver;
    }
}
