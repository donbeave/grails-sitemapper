package grails.plugins.sitemapper;

import java.util.Date;

/**
 * Sitemapper artefacts have to implement this interface.
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public interface Sitemapper {

    Date getPreviousUpdate();

    void withEntryWriter(EntryWriter entryWriter);

}