package grails.plugins.sitemapper.impl;

import grails.plugins.sitemapper.Sitemapper;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public abstract class PaginationSitemapper implements Sitemapper {

    private Integer pageNumber;

    public abstract Integer getPerPageCount();

    public abstract Long getTotalCount();

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPagesCount() {
        return (int) Math.ceil((double) getTotalCount() / getPerPageCount());
    }

    public Integer getOffset() {
        return pageNumber * getPerPageCount();
    }

    protected void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

}
