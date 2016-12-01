package com.comp3711.eva.biblibot;

/**
 * Created by Bobo on 18/11/2016.
 */

public class Citation {
    private String [] fName;
    private String [] lName;
    private String    type;
    private char      initial;
    private String    title;
    private String    publisher;
    private int       volume;
    private int       issue;
    private String    container;
    private double    version;
    private String    location;
    private String[]  contributors;
    private String[]  authors;
    private String    accessDate;
    private String    subtitle;
    private String    artTitle;
    private String    perTitle;
    private String    url;
    private String    doi;
    private String    pages;
    private int       pubYear;
    private int       pubMonth;
    private int       pubDay;
    private int       accessYear;
    private int       accessMonth;
    private int       accessDay;
    private String    pubDate;
    private int      AccessDate;

    public String [] getfName() {
        return fName;
    }

    public void setfName(String [] fName) {
        this.fName = fName;
    }

    public String [] getlName() {
        return lName;
    }

    public void setlName(String [] lName) {
        this.lName = lName;
    }

    public char getInitial() {
        return initial;
    }

    public void setInitial(char initial) {
        this.initial = initial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getContributors() {
        return contributors;
    }

    public void setContributors(String[] contributors) {
        this.contributors = contributors;
    }

    public String getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(String accessDate) {
        this.accessDate = accessDate;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getArtTitle() {
        return artTitle;
    }

    public void setArtTitle(String artTitle) {
        this.artTitle = artTitle;
    }

    public String getPerTitle() {
        return perTitle;
    }

    public void setPerTitle(String perTitle) {
        this.perTitle = perTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getPubYear() {
        return pubYear;
    }

    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }


    public int getPubMonth() {
        return pubMonth;
    }

    public void setPubMonth(int pubMonth) {
        this.pubMonth = pubMonth;
    }

    public int getPubDay() {
        return pubDay;
    }

    public void setPubDay(int pubDay) {
        this.pubDay = pubDay;
    }

    public int getAccessYear() {
        return accessYear;
    }

    public void setAccessYear(int accessYear) {
        this.accessYear = accessYear;
    }

    public int getAccessMonth() {
        return accessMonth;
    }

    public void setAccessMonth(int accessMonth) {
        this.accessMonth = accessMonth;
    }

    public int getAccessDay() {
        return accessDay;
    }

    public void setAccessDay(int accessDay) {
        this.accessDay = accessDay;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
