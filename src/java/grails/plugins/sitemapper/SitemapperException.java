package grails.plugins.sitemapper;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
@SuppressWarnings("serial")
public class SitemapperException extends RuntimeException {

    public SitemapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public SitemapperException(String message) {
        super(message);
    }

    public SitemapperException(Throwable cause) {
        super(cause);
    }

}