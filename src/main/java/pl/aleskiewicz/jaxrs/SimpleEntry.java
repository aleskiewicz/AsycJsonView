// (c) Copyright 3ES Innovation Inc. 2013.  All rights reserved.
package pl.aleskiewicz.jaxrs;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonView;

import pl.aleskiewicz.jaxrs.JsonViews.DetailsWebView;
import pl.aleskiewicz.jaxrs.JsonViews.WebView;


public class SimpleEntry {

    private String standardInfo;
    private String detailedInfo;
    private Date date;

    private SimpleEntry() {
        date = new Date();
    }

    public SimpleEntry(String standardInfo, String detailedInfo) {
        this();
        this.standardInfo = standardInfo;
        this.detailedInfo = detailedInfo;
    }

    @JsonView(WebView.class)
    public String getStandardInfo() {
        return standardInfo;
    }

    @JsonView(WebView.class)
    public void setStandardInfo(String standardInfo) {
        this.standardInfo = standardInfo;
    }

    @JsonView(DetailsWebView.class)
    public String getDetailedInfo() {
        return detailedInfo;
    }

    @JsonView(DetailsWebView.class)
    public void setDetailedInfo(String detailedInfo) {
        this.detailedInfo = detailedInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
