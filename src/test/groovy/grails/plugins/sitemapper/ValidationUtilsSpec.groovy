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

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
@TestMixin(GrailsUnitTestMixin)
class ValidationUtilsSpec extends Specification {

    def "Wrong location url"() {
        when:
        ValidationUtils.assertLocation("/test")
        then:
        SitemapperException ex = thrown()
        ex.message == 'no protocol: /test'
    }

    def "Proper location url"() {
        when:
        ValidationUtils.assertLocation("http://test.com")
        then:
        noExceptionThrown()

        when:
        ValidationUtils.assertLocation("https://test.com/file.html?query=value#page-2")
        then:
        noExceptionThrown()
    }

    def "Priority can't be lower than 0"() {
        when:
        ValidationUtils.assertPriority(-0.1)
        then:
        SitemapperException ex = thrown()
        ex.message == 'Priority has to be between 0 and 1, not -0.1'
    }

    def "Priority can'b be greater than 1"() {
        when:
        ValidationUtils.assertPriority(1.1)
        then:
        SitemapperException ex = thrown()
        ex.message == 'Priority has to be between 0 and 1, not 1.1'
    }

    def "Pass priority: 0.1"() {
        when:
        ValidationUtils.assertPriority(0.1)
        then:
        noExceptionThrown()
    }

}
