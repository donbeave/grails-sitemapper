/*
 * Copyright 2010 Kim A. Betti, Alexey Zhokhov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.sitemapper.artefact;

import org.codehaus.groovy.grails.commons.AbstractGrailsClass;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;
import org.codehaus.groovy.grails.commons.GrailsClass;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public class SitemapperArtefactHandler extends ArtefactHandlerAdapter {

    public static class DefaultSitemapperClass extends AbstractGrailsClass implements SitemapperClass {
        public DefaultSitemapperClass(Class<?> clazz) {
            super(clazz, SitemapperArtefactHandler.SUFFIX);
        }
    }

    public interface SitemapperClass extends GrailsClass {
        // Nothing to see here, move along..
    }

    public static final String TYPE = "Sitemapper";
    public static final String SUFFIX = "Sitemapper";

    public SitemapperArtefactHandler() {
        super(TYPE, SitemapperClass.class, DefaultSitemapperClass.class, SUFFIX);
    }

}
