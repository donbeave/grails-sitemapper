grails.app.context = '/'

log4j = {
    error 'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    debug 'grails.plugins.sitemapper'
}

environments {
    development {
        searchEnginePingUrls {
            bing = 'http://www.bing.com/webmaster/ping.aspx?siteMap=%s'
            google = 'http://www.google.com/webmasters/sitemaps/ping?sitemap=%s'
        }
    }

    test {
        searchEnginePingUrls {
        }
    }
}

grails.views.default.codec = 'none' // none, html, base64
grails.views.gsp.encoding = 'UTF-8'
