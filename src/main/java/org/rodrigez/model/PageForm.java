package org.rodrigez.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PageForm {

    @NotNull
    @Size(min=1)
    private String url;

    @NotNull
    @Size(min=1)
    private String searchText;

    @NotNull
    @Min(1)
    private int threadCount;

    @NotNull
    @Min(1)
    private int urlMaxCount;

    public PageForm() {
    }

    public PageForm(String url, String searchText, int threadCount, int urlMaxCount) {
        this.url = url;
        this.searchText = searchText;
        this.threadCount = threadCount;
        this.urlMaxCount = urlMaxCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getUrlMaxCount() {
        return urlMaxCount;
    }

    public void setUrlMaxCount(int urlMaxCount) {
        this.urlMaxCount = urlMaxCount;
    }
}
