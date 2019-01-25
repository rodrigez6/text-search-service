package org.rodrigez.model;

import java.util.Objects;

public class Page {

    private String url;

    private PageStatus status;

    private boolean processed;

    private int repeatsCount;

    private String error;

    public Page(String url) {
        this.url = url;
        this.status = PageStatus.NOT_PROCESSED;
        this.processed = false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PageStatus getStatus() {
        return status;
    }

    public void setStatus(PageStatus status) {
        this.status = status;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public int getRepeatsCount() {
        return repeatsCount;
    }

    public void setRepeatsCount(int repeatsCount) {
        this.repeatsCount = repeatsCount;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAdditionalInformation(){
        if (error!=null){
            return "Error: " + error;
        } else if (processed){
            return "Repeats count: " + String.valueOf(repeatsCount);
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(url,page.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Page{");
        sb.append("url='").append(url).append('\'');
        sb.append(", status=").append(status);
        sb.append(", processed=").append(processed);
        sb.append(", repeatsCount=").append(repeatsCount);
        sb.append(", error='").append(error).append('\'');
        sb.append('}');
        return sb.toString();
    }
}