package com.comp3711.eva.biblibot;

/**
 * Created by Bobo on 18/11/2016.
 */

public class Author {
    private String fName;
    private String lName;

    public Author(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }
}
