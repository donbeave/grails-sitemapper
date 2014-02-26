/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class SitemapperUrlMappings {

    static mappings = {
        "/sitemap"(controller: 'sitemapper')
        "/sitemap-$name"(controller: 'sitemapper', action: 'source')
    }

}
