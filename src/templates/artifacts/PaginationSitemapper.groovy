@artifact.package@import grails.plugin.sitemapper.EntryWriter
import grails.plugin.sitemapper.impl.PaginationSitemapper

import static grails.plugin.sitemapper.ContentChangeFrequency.MONTHLY

class @artifact.name@ extends PaginationSitemapper {

    Date previousUpdate = new Date()

    final Integer perPageCount = 40
    final Long totalCount = 100

    @Override
    public void withEntryWriter(EntryWriter entryWriter) {
        switch (pageIndex) {
            case 0:
                1.times { n ->
                    entryWriter.addEntry "/test/${n}", new Date() - 1
                }

                break;

            case 1:
                2.times { n ->
                    entryWriter.addEntry "/test/${n}", new Date(), MONTHLY, 0.5
                }
                break;
        }
    }

}