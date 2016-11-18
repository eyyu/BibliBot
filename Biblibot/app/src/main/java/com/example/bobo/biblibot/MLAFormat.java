package com.example.bobo.biblibot;

/**
 * Created by Bobo on 30/10/2016.
 */

public class MLAFormat implements  Formatable {

    public static String bookFormat(String[] fName, String[] lName, String title,
                             String publisher, String date) {
        String citation;

        if (fName[0].compareTo(publisher) == 0) {
            citation ="";

        } else if (fName.length == 1 && lName == null) {
            citation = fName[0] + ". ";

        } else if (fName.length == 1) {
            citation = lName[0] + ", " + fName[0] + ". ";

        } else if (fName.length == 2) {
            citation = lName[0] + ", " + fName[0] + ", and " + fName[1] + " " + lName[1];

        } else {
            citation = lName[0] + ", " + fName[0] + ", et al. ";

        }

        citation += title     + ". ";  //italicize???
        citation += publisher + ", ";
        citation += date      + ".";

        return citation;
    }

    public static String periodicalFormat(String[] fName, String[] lName, String title,
                                          String container, String[] contributors, String version,
                                          String volume, String publisher, String date,
                                          String location) {

        String citation;

        if (fName.length == 1) {
            citation = lName[0] + ", " + fName[0] + ". ";

        } else if (fName.length == 2) {
            citation = lName[0] + ", " + fName[0] + ", and " + fName[1] + " " + lName[1];

        } else {
            citation = lName[0] + ", " + fName[0] + ", et al. ";

        }

        citation += title + ". ";
        citation += container + ", ";

        for (String contributor : contributors) {
            citation += contributor + ", ";
        }

        citation += version   + ", ";
        citation += volume    + ", ";
        citation += publisher + ", ";
        citation += date      + ", ";
        citation += location  + ".";

        return citation;
    }

    public static String electronicFormat(String[] fName, String[] lName, String title,
                                          String container, String[] contributors, String version,
                                          String volume, String publisher, String date,
                                          String location, String accessDate) {
        String citation;

        if (fName.length == 1) {
            citation = lName[0] + ", " + fName[0] + ". ";

        } else if (fName.length == 2) {
            citation = lName[0] + ", " + fName[0] + ", and " + fName[1] + " " + lName[1];

        } else {
            citation = lName[0] + ", " + fName[0] + ", et al. ";

        }

        citation += "\"" + title + "\"" + ". ";
        citation += container + ", ";

        for (String contributor : contributors) {
            citation += contributor + ", ";
        }

        citation += version    + ", ";
        citation += volume     + ", ";
        citation += publisher  + ", ";
        citation += date       + ", ";
        citation += location   + ", ";
        citation += accessDate + ".";

        return citation;
    }
}
