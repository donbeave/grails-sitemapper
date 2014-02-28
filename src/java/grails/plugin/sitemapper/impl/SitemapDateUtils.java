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
package grails.plugin.sitemapper.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleDateFormat is not thread-safe, neither is this class.
 *
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public class SitemapDateUtils {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    public String formatForSitemap(final Date date) {
        final String formatted = dateFormat.format(date);
        final String postfix = formatted.substring(formatted.length() - 2); // Hack for timezone format
        return formatted.substring(0, formatted.length() - 2) + ":" + postfix;
    }

}