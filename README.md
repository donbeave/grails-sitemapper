Grails Sitemapper Plugin
==================

[![Build Status](https://circleci.com/gh/donbeave/grails-sitemapper.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/donbeave/grails-sitemapper)
[![Analytics](https://ga-beacon.appspot.com/UA-71075299-1/grails-sitemapper/main-page)](https://github.com/igrigorik/ga-beacon)

Summary
-------
Create sitemaps on the fly. Easily adds support for dynamic or static sitemaps for your site.

About sitemaps
--------------

Sitemaps allows search engines to quickly spot changes on your site without crawling the whole page. Note that search engines are obviously not compelled to index your sites faster if you add a sitemap, but chances are that a lot of them will. Have a look at [sitemaps.org](http://sitemaps.org) for more information about sitemaps. 

Installation
------------

In `BuildConfig.groovy`, add the dependency to "plugins" section:

```groovy
plugins {
    //...
    compile ':sitemapper:0.9'
    //...
}
```

Change the version to reflect the actual version you would like to use.

Quick Start
-----------

Create sitemap generator class by 
    
```
grails create-sitemap
> Add pagination support? [y,n] 
```

Select "y" if you exceed established limits (like 50 000 URLS per file) in one sitemap group.

Then script create sample artefact in `grails-app/sitemaps` folder,

Standard sitemap (single file):

```groovy
import grails.plugin.sitemapper.Sitemapper
    
import static grails.plugin.sitemapper.ContentChangeFrequency.MONTHLY

class ForumSitemapper implements Sitemapper {
        
    Date previousUpdate = new Date()
        
    // The `withEntryWriter` method will be invoked each time the sitemap is requested.
    void withEntryWriter(EntryWriter entryWriter) {
        entryWriter.addEntry '/forum/topic1', new Date() - 1
        entryWriter.addEntry '/forum/topic2', new Date(), MONTHLY, 0.5
        // ...
    }
	
}
```

Sitemaps group (multiple files):

```groovy
import grails.plugin.sitemapper.EntryWriter
import grails.plugin.sitemapper.impl.PaginationSitemapper

class MessagesSitemapper extends PaginationSitemapper {
        
    Date previousUpdate = new Date()

    final int perPageCount = 50000

    @Override
    long getTotalCount() {
        Message.count()
    }
        
    @Override
    void withEntryWriter(EntryWriter entryWriter) {
        def items = Message.list([
                max: perPageCount, 
                offset: pageIndex * perPageCount, 
                sort: 'createdDate', 
                order: 'asc'
        ])
    	    
        items.each {
            entryWriter.addEntry("/messages/${it.id}", it.createdDate)
        }
    }
    	
}
```

Usage
-----

On production mode plugin use gzip compression, so you can check sitemap index file by:

    http://localhost:8080/sitemap.xml.gz

On the development mode directly:

    http://localhost:8080/sitemap.xml
    
Config
------

To change sitemap filename (only filename, exclude extension), you can set the following in `Config.groovy`:

```groovy
sitemap.prefix = 'mysitemap'
```

To disable gzip on production mode:

```groovy
environments {
    production {
        sitemap.gzip = false
    }
}
```
    
Static files generation
-----------------------

Suggest use this plugin under proxy servers like [nginx](http://nginx.org/) or [Apache HTTP Server](http://httpd.apache.org/) with enabled caching.

Anyway, plugin also support to generate static files by script:

    grails generate-static-sitemaps

After running this script you can see result in `target/sitemaps` folder.

Search engine ping (EXPERIMENTAL)
---------------------------------

Add something like this to your `Config.groovy` file. The %s will be substituted with sitemap uri. 

    sitemapConsumers {
        bing 'http://www.bing.com/webmaster/ping.aspx?siteMap=%s'
        google 'http://www.google.com/webmasters/sitemaps/ping?sitemap=%s'
    }

Important! This has not yet been fully implemented. 

Roadmap
-------

 1. Implement support for search engine ping - in (slow) progress.
 2. Support for Grails Cache plugin.

Copyright and license
---------------------

Copyright 2010-2015 Kim A. Betti, Alexey Zhokhov under the [Apache License, Version 2.0](LICENSE). Supported by [AZ][zhokhov].

[zhokhov]: http://www.zhokhov.com
