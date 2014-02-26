@artifact.package@import grails.plugins.sitemapper.EntryWriter
import grails.plugins.sitemapper.impl.PaginationSitemapper

import static grails.plugins.sitemapper.ContentChangeFrequency.MONTHLY

class @artifact.name@ extends PaginationSitemapper {

    Date previousUpdate = new Date()

    final Integer perPageCount = 40
    final Long totalCount = 100

    @Override
    public void withEntryWriter(EntryWriter entryWriter) {
        switch (pageNumber) {
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