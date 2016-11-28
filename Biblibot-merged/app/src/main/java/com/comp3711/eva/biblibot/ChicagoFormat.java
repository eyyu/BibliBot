package com.comp3711.eva.biblibot;

/**
 * Created by Bobo on 17/11/2016.
 */

public class ChicagoFormat implements Formatable {

    public static String bookFormat(String lName, String fName, String title, String pubLocation,
                                    String publisher, String pubYear) {
        String citation;

        citation  = lName + ", ";
        citation += fName + ". ";
        citation += title + ". ";
        citation += pubLocation + ": ";
        citation += publisher + ", ";
        citation += pubYear + ".";

        return citation;
    }

    public static String periodicalFormat(String lName, String fName, String artTitle,
                                          String perTitle, String volume, String issue,
                                          String year, String pages) {
        String citation;

        citation  = lName + ", ";
        citation += fName + ". ";
        citation += "\"" + artTitle + ".\" ";
        citation += perTitle + " ";
        citation += volume;
        if (issue != null)
            citation += ", no. " + issue;
        citation += " (" + year + "): ";
        citation += pages + ".";

        return citation;
    }

    public static String webSourceFormat(String lName, String fName, String pageTitle,
                                         String pubOrWebsite, String pubDateOrAccessDate,
                                         String url) {
        String citation;

        citation  = lName + ", ";
        citation += fName + ". ";
        citation += "\"" + pageTitle + ".\" ";
        citation += pubOrWebsite + ". ";
        citation += pubDateOrAccessDate + ". ";
        citation += url;

        return citation;
    }
}
