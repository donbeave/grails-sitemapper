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
package grails.plugin.sitemapper.extension;

import grails.plugin.sitemapper.Extension;

import java.util.List;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class PageMap implements Extension {

    private List<PageMapDataObject> dataObjects;

    public List<PageMapDataObject> getDataObjects() {
        return dataObjects;
    }

    public void setDataObjects(List<PageMapDataObject> dataObjects) {
        this.dataObjects = dataObjects;
    }

}
