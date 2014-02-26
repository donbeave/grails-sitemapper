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
package grails.plugins.sitemapper;

/**
 * How frequently the page is likely to change. This value provides general information to search
 * engines and may not correlate exactly to how often they crawl the page.
 * <p/>
 * The value "always" should be used to describe documents that change each time they are accessed.
 * The value "never" should be used to describe archived URLs.
 * <p/>
 * Please note that the value of this tag is considered a hint and not a command.
 * Even though search engine crawlers may consider this information when making decisions,
 * they may crawl pages marked "hourly" less frequently than that, and they may crawl pages
 * marked "yearly" more frequently than that. Crawlers may periodically crawl pages marked
 * "never" so that they can handle unexpected changes to those pages.
 * <p/>
 * - from sitemaps.org
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public enum ContentChangeFrequency {

    ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER

}
