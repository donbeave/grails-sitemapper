@artifact.package@import grails.plugin.sitemapper.EntryWriter
import grails.plugin.sitemapper.Sitemapper

import static grails.plugin.sitemapper.ContentChangeFrequency.MONTHLY

class @artifact.name@ implements Sitemapper {

    Date previousUpdate = new Date()

    void withEntryWriter(EntryWriter entryWriter) {
        entryWriter.addEntry '/test', new Date() - 1
        entryWriter.addEntry '/test-2', new Date(), MONTHLY, 0.5
    }
}
