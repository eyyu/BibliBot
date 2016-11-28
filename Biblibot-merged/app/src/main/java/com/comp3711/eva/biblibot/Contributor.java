package com.comp3711.eva.biblibot;

/**
 * Created by Bobo on 18/11/2016.
 */

public class Contributor {
    private String fName;
    private String lName;
    private String title;

    public Contributor(String fName, String lName, String title) {
        this.fName = fName;
        this.lName = lName;
        this.title = title;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getTitle() {
        return title;
    }
}
