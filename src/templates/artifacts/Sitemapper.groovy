@artifact.package@import grails.plugins.sitemapper.EntryWriter
import grails.plugins.sitemapper.Sitemapper

import static grails.plugins.sitemapper.ContentChangeFrequency.MONTHLY

class @artifact.name@ implements Sitemapper {

    Date previousUpdate = new Date()

    @Override
    public void withEntryWriter(EntryWriter entryWriter) {
        entryWriter.addEntry '/test', new Date() - 1
        entryWriter.addEntry '/test-2', new Date(), MONTHLY, 0.5
    }

}