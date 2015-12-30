package grails.plugins.sitemapper;

import grails.core.GrailsClass;

import java.util.Date;

/**
 * Sitemap artefacts have to implement this interface.
 *
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public interface GrailsSitemapClass extends GrailsClass {

    Date getPreviousUpdate();

    void withEntryWriter(EntryWriter entryWriter, Integer pageIndex, Integer offset);

    Integer getPerPageCount();

    Long getTotalCount();

    Integer getPagesCount();

    Integer getOffset(Integer pageIndex);

    String getServerUrl();

}
