package com.comp3711.eva.biblibot;

/**
 * Created by Bobo on 17/11/2016.
 */

public class APAFormat implements Formatable {

    public static String bookFormat(String [] lName, String [] fName, String pubDate, String title,
                                    String subtitle, String location, String publisher) {
        String citation;

        if (fName[0].compareTo(publisher) == 0) {
            citation ="";

        } else if (fName.length == 1 && lName == null) {
            citation = fName[0] + ". ";

        } else if (fName.length == 1) {
            citation = lName[0] + ", " + fName[0] + ". ";

        } else if (fName.length == 2) {
            citation = lName[0] + ", " + fName[0] + ", and " + fName[1] + " " + lName[1] + ". ";

        } else {
            citation = lName[0] + ", " + fName[0] + ", et al. ";

        }
//        citation  = lName + ", ";
//        citation += fName + ". ";
        citation += "(" + pubDate + ") ";
        citation += "<i>" + title + "</i>";
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

    public static String electronicFormat(String[] lName, char[] fName, String pubDate, String title,
                                          String url) {
        String citation = "";

        for (int i = 0; i < lName.length; i++) {
            if (i == lName.length - 1) {
                citation += lName[i] + ", " + fName[i] +". ";
            } else
                citation += lName[i] + ", " + fName[i] + "., &";
        }
        citation += "(" + pubDate + "). ";
        citation += title + ". ";
        citation += url;

        return citation;
    }
}