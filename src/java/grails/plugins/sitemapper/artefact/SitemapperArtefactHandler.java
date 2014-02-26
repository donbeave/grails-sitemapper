package grails.plugins.sitemapper.artefact;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public class SitemapperArtefactHandler extends ArtefactHandlerAdapter {

    static public final String TYPE = "Sitemapper";
    static public final String SUFFIX = "Sitemapper";

    public SitemapperArtefactHandler() {
        super(TYPE, SitemapperClass.class, DefaultSitemapperClass.class, SUFFIX);
    }

}
