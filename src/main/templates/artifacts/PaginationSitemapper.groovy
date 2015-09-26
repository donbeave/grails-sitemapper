@artifact.package@import grails.plugin.sitemapper.EntryWriter

@
import static grails.plugin.sitemapper.ContentChangeFrequency.MONTHLY

class @artifact.name@ extends PaginationSitemapper {

    Date previousUpdate = new Date()

    final int perPageCount = 40
    final long totalCount = 100

    @Override
    void withEntryWriter(EntryWriter entryWriter) {
        switch (pageIndex) {
            case 0:
                1.times { n ->
                    entryWriter.addEntry "/test/${n}", new Date() - 1
                }

                break

            case 1:
                2.times { n ->
                    entryWriter.addEntry "/test/${n}", new Date(), MONTHLY, 0.5
                }
                break
        }
    }
}
