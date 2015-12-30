/*
 * Copyright 2014 the original author or authors.
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
import grails.build.logging.GrailsConsole

/**
 * Gant script that creates a new Grails Sitemap Artefact
 *
 * @author David Estes
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */

includeTargets << grailsScript('_GrailsInit')
includeTargets << grailsScript('_GrailsCreateArtifacts')

target(createSitemap: 'Creates a new sitemap') {
    depends(checkVersion, parseArguments)

    pagination = GrailsConsole.instance.userInput('Add pagination support? ', ['y', 'n'] as String[])

    def type = pagination.equals('y') ? 'PaginationSitemap' : 'Sitemapper'
    promptForName(type: type)

    for (name in argsMap['params']) {
        name = purgeRedundantArtifactSuffix(name, type)
        createArtifact(name: name, suffix: type, type: type, path: 'grails-app/sitemaps')
    }
}

USAGE = """
    create-sitemap [NAME]

where
    NAME       = The name of the sitemap. If not provided, this
                 command will ask you for the name.
"""

setDefaultTarget(createSitemap)
