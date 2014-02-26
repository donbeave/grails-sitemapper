package demo

import grails.plugins.sitemapper.EntryWriter
import grails.plugins.sitemapper.Sitemapper

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
import static grails.plugins.sitemapper.ContentChangeFrequency.MONTHLY

class ForumSitemapper implements Sitemapper {

    Date previousUpdate = new Date()

    @Override
    public void withEntryWriter(EntryWriter entryWriter) {
        entryWriter.addEntry "/forum/entry/test", new Date() - 1
        entryWriter.addEntry "/forum/entry/test-2", new Date(), MONTHLY, 0.5
    }

}