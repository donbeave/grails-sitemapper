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
package grails.plugin.sitemapper;

import java.io.IOException;
import java.util.Date;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public interface EntryWriter {

    void addEntry(String location, Date modifiedAt) throws IOException;

    void addEntry(String location, Date modifiedAt, ContentChangeFrequency freq, double priority) throws IOException;

}