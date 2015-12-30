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

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
public abstract class AbstractSitemapWriter {

    protected SitemapServerUrlResolver serverUrlResolver;
    protected Map<String, DefaultGrailsSitemapClass> sitemaps = new HashMap<String, DefaultGrailsSitemapClass>();

    @Required
    public void setSitemapServerUrlResolver(SitemapServerUrlResolver serverUrlResolver) {
        this.serverUrlResolver = serverUrlResolver;
    }

    public void writeSitemapEntries(PrintWriter writer, String sourceName, int pageNumber)
            throws IOException {
        GrailsSitemapClass mapper = getMapperByName(sourceName);

        writeSitemapEntries(writer, mapper, pageNumber);
    }

    public abstract void writeSitemapEntries(PrintWriter writer, GrailsSitemapClass sitemapper, Integer pageIndex) throws IOException;

    public abstract void writeIndexEntries(PrintWriter writer) throws IOException;

    public Map<String, DefaultGrailsSitemapClass> getSitemaps() {
        return sitemaps;
    }

    public void setSitemaps(List<DefaultGrailsSitemapClass> newMappers) {
        sitemaps.clear();

        for (DefaultGrailsSitemapClass mapper : newMappers) {
            String mapperName = mapper.getName().toLowerCase();

            sitemaps.put(mapperName, mapper);
        }
    }

    protected GrailsSitemapClass getMapperByName(String name) {
        String mapperName = name.toLowerCase();

        GrailsSitemapClass mapper = sitemaps.get(mapperName);

        Assert.notNull(mapper, "Unable to find source with name " + name);
        return mapper;
    }

}
