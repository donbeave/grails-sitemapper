/*
 * Copyright 2015 Kim A. Betti, Alexey Zhokhov
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
package grails.plugins.sitemapper.artefact;

import grails.core.ArtefactHandlerAdapter;
import grails.core.GrailsClass;
import org.grails.core.AbstractGrailsClass;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class SitemapperArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "Sitemapper";
    public static final String SUFFIX = "Sitemapper";

    public SitemapperArtefactHandler() {
        super(TYPE, SitemapperClass.class, DefaultSitemapperClass.class, SUFFIX);
    }

    public interface SitemapperClass extends GrailsClass {
        // Nothing to see here, move along..
    }

    public static class DefaultSitemapperClass extends AbstractGrailsClass implements SitemapperClass {
        public DefaultSitemapperClass(Class<?> clazz) {
            super(clazz, SitemapperArtefactHandler.SUFFIX);
        }
    }

}
