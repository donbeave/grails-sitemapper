import java.util.zip.GZIPOutputStream

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

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
defaultDestPath = 'target/sitemaps'

siteMapCreate = { String destPath = defaultDestPath ->
    ant.mkdir dir: destPath

    // loading classes
    def paginationSitemapperClass = classLoader.loadClass('grails.plugins.sitemapper.impl.PaginationSitemapper', true)

    def sitemapWriter = appCtx.getBean('sitemapWriter')
    String extension = sitemapWriter.extension

    generateIndexFile(sitemapWriter, destPath)

    for (String mapperName : sitemapWriter.getSitemappers().keySet()) {
        def mapper = sitemapWriter.getSitemappers().get(mapperName);

        if (paginationSitemapperClass.isInstance(mapper)) {
            for (int i = 0; i < mapper.pagesCount; i++) {
                generateUrlSetFile(sitemapWriter, mapper, i, "sitemap.${mapperName}-${i}.${extension}", destPath)
            }
        } else {
            generateUrlSetFile(sitemapWriter, mapper, 0, "sitemap.${mapperName}.${extension}", destPath)
        }
    }

    println 'All sitemaps generated.'
}

generateIndexFile = { sitemapWriter, String destPath = defaultDestPath ->
    println 'Generating sitemap index file ...'

    def indexWriter = getWriter("${destPath}/sitemap.${sitemapWriter.extension}")
    sitemapWriter.writeIndexEntries(indexWriter)
    indexWriter.flush()
    indexWriter.close();
}

generateUrlSetFile = { sitemapWriter, mapper, part, fileName, String destPath = defaultDestPath ->
    println "Generating ${fileName} ..."

    PrintWriter urlsetWriter = getWriter("${destPath}/${fileName}")
    sitemapWriter.writeSitemapEntries(urlsetWriter, mapper, part);
    urlsetWriter.flush()
    urlsetWriter.close();
}

public PrintWriter getWriter(String path) throws Exception {
    if (path.contains('xml.gz')) {
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(new File(path)));
        return new PrintWriter(out)
    } else {
        return new PrintWriter(new File(path))
    }
}