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
package grails.plugin.sitemapper.impl

import grails.test.spock.IntegrationSpec
import kim.spock.httpmock.HttpServer
import kim.spock.httpmock.TestHttpServer
import kim.spock.httpmock.WithHttpServer

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
@WithHttpServer(port = 23456)
class SearchEnginePingerIntegrationSpec extends IntegrationSpec {

    SearchEnginePinger searchEnginePinger

    def "pinging dummy search engine works"() {

        given: "mocked http server"
        TestHttpServer.mock = Mock(HttpServer)

        and: "register url for a dummy service to ping"
        String pingUri = "http://localhost:23456/searchEngine/ping?sitemap=%s"
        searchEnginePinger.addSearchEnginePingUrl "dummy", pingUri

        when: "ping all registered services"
        boolean allSuccess = searchEnginePinger.pingAll()

        then: "expect one call to the registered service, with the specified sitemap location"
        1 * TestHttpServer.mock.request("get", "/searchEngine/ping",
                { it["sitemap"] == "http://localhost:8080/sitemap.xml" }, _) >> "ok"

        and: "it should return true if every ping returned http 200"
        allSuccess == true

    }

}