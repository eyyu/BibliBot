package com.example.bobo.biblibot;

/**
 * Created by Bobo on 17/11/2016.
 */

public class APAFormat implements Formatable {

    public static String bookFormat(String lName, char fName, String pubDate, String title,
                                    String subtitle, String location, String publisher) {
        String citation;

        citation  = lName + ", ";
        citation += fName + ". ";
        citation += "(" + pubDate + ") ";
        citation += title;
        if (subtitle != null)
            citation += ": " + subtitle + ".";
        citation += " " + location + ": ";
        citation += publisher + ".";

        return citation;
    }

    public static String periodicalFormat(String lName, char fName, String year, String artTitle,
                                          String perTitle, String volume, String issue, String pages,
                                          String url) {
        String citation;

        citation  = lName + ", ";
        citation += fName + ". ";
        citation += "(" + year + "). ";
        citation += artTitle + ". ";
        citation += perTitle + ", ";
        citation += volume;
        citation += "(" + issue + "), ";
        citation += pages + ". ";
        citation += url;

        return citation;
    }

    public static String electronicFormat() {
        throw new UnsupportedOperationException();
    }
}