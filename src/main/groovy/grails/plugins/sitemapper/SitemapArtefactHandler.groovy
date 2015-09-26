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
package grails.plugins.sitemapper

import grails.core.ArtefactHandlerAdapter
import org.codehaus.groovy.ast.ClassNode
import org.grails.compiler.injection.GrailsASTUtils
import org.springframework.util.ReflectionUtils

import java.lang.reflect.Method
import java.util.regex.Pattern

import static org.grails.io.support.GrailsResourceUtils.GRAILS_APP_DIR
import static org.grails.io.support.GrailsResourceUtils.REGEX_FILE_SEPARATOR

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
class SitemapArtefactHandler extends ArtefactHandlerAdapter {

    static final String TYPE = 'Sitemap'
    static Pattern JOB_PATH_PATTERN = Pattern.compile(".+" + REGEX_FILE_SEPARATOR + GRAILS_APP_DIR + REGEX_FILE_SEPARATOR + "sitemaps" + REGEX_FILE_SEPARATOR + "(.+)\\.(groovy)");

    public SitemapArtefactHandler() {
        super(TYPE, GrailsSitemapClass.class, DefaultGrailsSitemapClass.class, TYPE)
    }

    boolean isArtefact(ClassNode classNode) {
        if (classNode == null ||
                !isValidArtefactClassNode(classNode, classNode.getModifiers()) ||
                !classNode.getName().endsWith(DefaultGrailsSitemapClass.SITEMAP) ||
                !classNode.getMethods(GrailsSitemapClassConstants.WITH_ENTRY_WRITER)) {
            return false
        }

        URL url = GrailsASTUtils.getSourceUrl(classNode)

        url && JOB_PATH_PATTERN.matcher(url.getFile()).find()
    }

    boolean isArtefactClass(Class clazz) {
        // class shouldn't be null and should ends with Job suffix
        if (clazz == null || !clazz.getName().endsWith(DefaultGrailsSitemapClass.SITEMAP)) return false
        // and should have one of execute() or execute(JobExecutionContext) methods defined
        Method method = ReflectionUtils.findMethod(clazz, GrailsSitemapClassConstants.WITH_ENTRY_WRITER,
                [EntryWriter, Integer, Integer] as Class[])
        method != null
    }

}
