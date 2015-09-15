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
package grails.plugin.sitemapper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static grails.plugin.sitemapper.ValidationUtils.assertPriority;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class Entry {

    List<Extension> extensions;
    private String location;
    private Date modifiedAt;
    private ContentChangeFrequency changeFrequency;
    private double priority;
    private Date expires;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ContentChangeFrequency getChangeFrequency() {
        return changeFrequency;
    }

    public void setChangeFrequency(ContentChangeFrequency changeFrequency) {
        this.changeFrequency = changeFrequency;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        assertPriority(priority);

        this.priority = priority;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Collection<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

}
