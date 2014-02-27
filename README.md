grails-sitemapper
=================

Grails Sitemaps plugin

Summary
-------
Create sitemaps on the fly. Easy add support for dynamic or static sitemaps for you site.

About sitemaps
--------------

Sitemaps allows search engines to quickly spot changes on your site without crawling the whole page. Note that search engines are obviously not compelled to index your sites faster if you add a sitemap, but chances are that a lot of them will. Have a look at [sitemaps.org](http://sitemaps.org) for more information about sitemaps. 

Installation
------------

In `BuildConfig.groovy`, add the dependency to "plugins" section:

```groovy
    plugins {
        //...
        compile ':sitemapper:0.8'
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

Then script screate sample artefacts in `grails-app/sitemaps` folder,

Standard sitemap (single file):

```groovy
import grails.plugins.sitemapper.Sitemapper
    
import static grails.plugins.sitemapper.ContentChangeFrequency.MONTHLY

class ForumSitemapper implements Sitemapper {
        
    Date previousUpdate = new Date()
        
    @Override
    // The `withEntryWriter` method will be invoked each time the sitemap is requested.
    public void withEntryWriter(EntryWriter entryWriter) {
        entryWriter.addEntry '/forum/topic1', new Date() - 1
        entryWriter.addEntry '/forum/topic2', new Date(), MONTHLY, 0.5
        // ...
    }
	
}
```

Sitemaps group (multiple files):

```groovy
import grails.plugins.sitemapper.EntryWriter
import grails.plugins.sitemapper.impl.PaginationSitemapper

class MessagesSitemapper extends PaginationSitemapper {
        
    Date previousUpdate = new Date()

    final Integer perPageCount = 50000

    @Override
    public Long getTotalCount() {
        Message.count()
    }
        
	@Override
	public void withEntryWriter(EntryWriter entryWriter) {
	    def items = Message.list([max: perPageCount, offset: pageIndex * perPageCount, sort: 'createdDate', order: 'asc'])
    	    
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

Suggested use this plugin with proxy servers like [nginx](http://nginx.org/) or [Apache HTTP Server](http://httpd.apache.org/) and use caching functional.

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

Copyright 2010-2014 Kim A. Betti, Alexey Zhokhov under the [Apache License, Version 2.0](LICENSE).
