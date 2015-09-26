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
package grails.plugins.sitemapper.extension;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class VideoPlayerLocation {

    private String location;
    private boolean allowEmbed = false;
    private String autoPlay;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAllowEmbed() {
        return allowEmbed;
    }

    public void setAllowEmbed(boolean allowEmbed) {
        this.allowEmbed = allowEmbed;
    }

    public String getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(String autoPlay) {
        this.autoPlay = autoPlay;
    }

}
