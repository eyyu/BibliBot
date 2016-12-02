package com.comp3711.eva.biblibot;

/**
 * Created by Trista on 2016-11-18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by Eva on 11/13/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String DBNAME       = "biblibot.db";
    private static final int    VERSION      = 1;

    private static final String TABLE_CITATIONTYPE          = "CITATIONTYPE";
    private static final String TABLE_PROJECT               = "PROJECT";
    private static final String TABLE_CITATION              = "CITATION";
    private static final String TABLE_AUTHOR                = "AUTHOR";
    private static final String TABLE_CONTRIBUTOR           = "CONTRIBUTOR";
    private static final String TABLE_CITATIONAUTHOR        = "CITATIONAUTHOR";
    private static final String TABLE_CITATIONCONTRIBUTOR   = "CITATIONCONTRIBUTOR";
    private static final String TABLE_PROJECTCITATION       = "PROJECTCITATION";


    private static final String KEY_ID              = "_id";
    private static final String KEY_PROJECTID       = "PROJECTID";
    private static final String KEY_CITATIONID      = "CITATIONID";
    private static final String KEY_AUTHORID        = "AUTHORID";
    private static final String KEY_CONTRIBUTORID   = "CONTRIBUTORID";

    private static final String KEY_TYPE       = "TYPE";
    private static final String KEY_NAME       = "NAME";
    private static final String KEY_DATE       = "DATE";

    private static final String KEY_FNAME       = "FNAME";
    private static final String KEY_LNAME       = "LNAME";

    private static final String KEY_TYPEID      = "TYPEID";
    private static final String KEY_CONTAINTER  = "CONTAINTER";
    private static final String KEY_TITLE       = "TITLE";
    private static final String KEY_SUBTITLE    = "SUBTITLE";
    private static final String KEY_VERSION     = "VERSION";
    private static final String KEY_VOLUME      = "VOLUME";
    private static final String KEY_ISSUE       = "ISSUE";
    private static final String KEY_PAGES       = "PAGES";
    private static final String KEY_URL         = "URL";
    private static final String KEY_DOI         = "DOI";
    private static final String KEY_LOCATION    = "LOCATION";
    private static final String KEY_PUBLISHER   = "PUBLISHER";
    private static final String KEY_PUBDATE     = "PUBDATE";
    private static final String KEY_PUBYEAR     = "PUBYEAR";
    private static final String KEY_PUBDAY      = "PUBDAY";
    private static final String KEY_PUBMONTH    = "PUBMONTH";
    private static final String KEY_ACCESSYEAR  = "ACCESSYEAR";
    private static final String KEY_ACCESSDAY   = "ACCESSDAY";
    private static final String KEY_ACCESSMONTH = "ACCESSMONTH";

    private static final String DEFAULT_PROJECT = "ALL";

    private static boolean hasValues = false;


    private static final String CREATE_TABLE_CITATIONTYPE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CITATIONTYPE + " (" +
                    KEY_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TYPE    + " TEXT " +
           ")";

    private static final String CREATE_TABLE_PROJECT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PROJECT + " (" +
                    KEY_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME   + " TEXT, " +
                    KEY_DATE   + " TEXT"+
            ")";

    private static final String CREATE_TABLE_CITATION =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CITATION + " (" +
                    KEY_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TYPEID        + " INTEGER, " +
                    KEY_CONTAINTER    + " TEXT, " +
                    KEY_TITLE         + " TEXT, " +
                    KEY_SUBTITLE      + " TEXT, " +
                    KEY_VERSION       + " REAL, " +
                    KEY_VOLUME        + " INTEGER, " +
                    KEY_ISSUE         + " INTEGER, " +
                    KEY_PAGES         + " TEXT, " +
                    KEY_URL           + " TEXT, " +
                    KEY_DOI           + " TEXT, " +
                    KEY_LOCATION      + " TEXT, " +
                    KEY_PUBLISHER     + " TEXT, " +
                    KEY_PUBDATE       + " TEXT, " +
                    KEY_PUBYEAR       + " INTEGER, " +
                    KEY_PUBDAY        + " INTEGER, " +
                    KEY_PUBMONTH      + " INTEGER, " +
                    KEY_ACCESSYEAR    + " INTEGER, " +
                    KEY_ACCESSDAY     + " INTEGER, " +
                    KEY_ACCESSMONTH   + " INTEGER "   +
        ")";


    private static final String CREATE_TABLE_AUTHOR =
            "CREATE TABLE IF NOT EXISTS " + TABLE_AUTHOR + " (" +
                    KEY_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FNAME   + " TEXT, " +
                    KEY_LNAME   + " TEXT,"   +
                    KEY_CITATIONID   + " INTEGER"   +
                    ")";

    private static final String CREATE_TABLE_CONTRIBUTOR =
            "CREATE TABLE IF NOT EXISTS " + TABLE_AUTHOR + " (" +
                    KEY_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FNAME   + " TEXT, " +
                    KEY_LNAME   + " TEXT, " +
                    KEY_TITLE   + " TEXT"   +
                    ")";

    private static final String CREATE_TABLE_CITATIONAUTHOR =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CITATIONAUTHOR + " (" +
                    KEY_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_CITATIONID   + " INTEGER, " +
                    KEY_AUTHORID     + " INTEGER, " +
                    "FOREIGN KEY (" + KEY_CITATIONID + ") REFERENCES "
                    + TABLE_CITATION + "(" + KEY_ID + "), " +
                    "FOREIGN KEY (" + KEY_AUTHORID + ") REFERENCES "
                    + TABLE_AUTHOR   + "("  + KEY_ID + ")" +
            ")";

    private static final String CREATE_TABLE_CITATIONCONTRIBUTOR =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CITATIONCONTRIBUTOR + " (" +
                    KEY_ID              + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_CITATIONID      + " INTEGER, " +
                    KEY_CONTRIBUTORID   + " INTEGER, " +
                    "FOREIGN KEY (" + KEY_CITATIONID + ") REFERENCES "
                    + TABLE_CITATION    + "(" + KEY_ID + "), " +
                    "FOREIGN KEY (" + KEY_CONTRIBUTORID + ") REFERENCES "
                    + TABLE_CONTRIBUTOR + "(" + KEY_ID + ")" +
            ")";

    private static final String CREATE_TABLE_PROJECTCITATION =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PROJECTCITATION + " (" +
                    KEY_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_PROJECTID    + " INTEGER, " +
                    KEY_CITATIONID   + " INTEGER, " +
                    "FOREIGN KEY ("  + KEY_PROJECTID + ") REFERENCES "
                    + TABLE_PROJECT   + "(" + KEY_ID + "), " +
                    "FOREIGN KEY ("  + KEY_CITATIONID + ") REFERENCES "
                    + TABLE_CITATION  + "(" + KEY_ID + ")" +
            ")";


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        try
        {
            db.execSQL(CREATE_TABLE_CITATIONTYPE);
            db.execSQL(CREATE_TABLE_PROJECT);
            db.execSQL(CREATE_TABLE_CITATION);
            Log.e(TAG, "onCreate Table Citation: " + CREATE_TABLE_CITATION);
            db.execSQL(CREATE_TABLE_AUTHOR);
            db.execSQL(CREATE_TABLE_CONTRIBUTOR);
            db.execSQL(CREATE_TABLE_CITATIONAUTHOR);
            db.execSQL(CREATE_TABLE_CITATIONCONTRIBUTOR);
            db.execSQL(CREATE_TABLE_PROJECTCITATION);
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.beginTransaction();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTCITATION );
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITATIONCONTRIBUTOR );
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITATIONAUTHOR );
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRIBUTOR );
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR );
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITATION );
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT );
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CITATIONTYPE);
            db.setTransactionSuccessful();
            onCreate(db);
        }
        finally
        {
            db.endTransaction();
        }
    }

    private boolean insertType(String typeName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_TYPE , typeName );

        if  ( db.insert(TABLE_CITATIONTYPE, null, cv) < 0 )
        {
            return false;
        }

        db.close();
        return true;
    }

    private boolean insertCitationAuthor(int citId, int authId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_CITATIONID , citId  );
        cv.put ( KEY_AUTHORID   , authId );

        if  ( db.insert(TABLE_CITATIONAUTHOR, null, cv) < 0 )
        {
            return false;
        }
        db.close();
        return true;
    }

    private boolean insertCitationContributor(int citId, int contribId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_CITATIONID    , citId     );
        cv.put ( KEY_CONTRIBUTORID , contribId );

        if  ( db.insert(TABLE_CITATIONCONTRIBUTOR, null, cv) < 0 )
        {
            return false;
        }
        db.close();
        return true;
    }


    private boolean insertCitationProject(int citId, int proId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_PROJECTID  , proId );
        cv.put ( KEY_CITATIONID , citId );

        if  ( db.insert(TABLE_PROJECTCITATION, null, cv) < 0 )
        {
            return false;
        }
        db.close();
        return true;
    }

    public boolean insertAuthor(String fName, String lName, int citationID )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_FNAME      , fName );
        cv.put ( KEY_LNAME      , lName );
        cv.put ( KEY_CITATIONID , citationID );

        if  ( db.insert(TABLE_AUTHOR, null, cv) < 0 )
        {
            return false;
        }
        db.close();
        return true;
    }

    public boolean insertContributor(String fName, String lName, String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_FNAME , fName );
        cv.put ( KEY_LNAME , lName );
        cv.put ( KEY_TITLE , title );

        if  ( db.insert(TABLE_CONTRIBUTOR, null, cv) < 0 )
        {
            return false;
        }
        db.close();
        return true;
    }

    public boolean insertProject(String name)
    {
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put ( KEY_NAME , name );
        cv.put ( KEY_DATE , date.toString() );

        if  ( db.insert(TABLE_PROJECT, null, cv) < 0 )
        {
            return false;
        }

        db.close();
        return true;
    }

    public boolean insertCitation(Citation c, String [] authorfNames , String []authorlNames) //,
                                  //Author [] authors,
                                  //Contributor [] contribs )
    {
        if(getCitationID(c.getTitle()) > 0){
            return false;
        }

        ContentValues cv = new ContentValues();

        int typeId = getCitationTypeId(c.getType());
        cv.put ( KEY_TYPEID       , typeId );
        SQLiteDatabase db = this.getWritableDatabase();
        if(c.getContainer() != null)
            cv.put ( KEY_CONTAINTER   , c.getContainer() );
        if(c.getTitle() != null)
            cv.put ( KEY_TITLE        , c.getTitle() );
        if(c.getSubtitle() != null)
            cv.put ( KEY_SUBTITLE     , c.getSubtitle());
        if(c.getVolume() > 0)
            cv.put ( KEY_VOLUME       , c.getVolume());
        if(c.getVersion() > 0)
            cv.put ( KEY_VERSION      , c.getVersion());
        if(c.getIssue() > 0)
            cv.put ( KEY_ISSUE        , c.getIssue());
        if(c.getPages() != null)
            cv.put ( KEY_PAGES        , c.getPages());
        if(c.getUrl() != null)
            cv.put ( KEY_URL          , c.getUrl());
        if(c.getDoi() != null)
            cv.put ( KEY_DOI          , c.getDoi());
        if(c.getLocation() != null)
            cv.put ( KEY_LOCATION     , c.getLocation());
        if(c.getPublisher() != null)
            cv.put ( KEY_PUBLISHER    , c.getPublisher());
        if(c.getPubDate() != null)
            cv.put ( KEY_PUBDATE      , c.getPubDate());
        if(c.getPubYear() > 0)
            cv.put ( KEY_PUBYEAR      , c.getPubYear());
        if(c.getPubDay() > 0)
            cv.put ( KEY_PUBDAY       , c.getPubDay());
        if(c.getPubMonth() > 0)
            cv.put ( KEY_PUBMONTH     , c.getPubMonth());
        if(c.getAccessYear() > 0)
            cv.put ( KEY_ACCESSYEAR   , c.getAccessYear());
        if(c.getAccessDay() > 0)
            cv.put ( KEY_ACCESSDAY    , c.getAccessDay());
        if(c.getAccessMonth() > 0)
            cv.put ( KEY_ACCESSMONTH  , c.getAccessMonth());

        long results = db.insertOrThrow(TABLE_CITATION, null, cv);
        if  (  results  < 0 )
        {
            db.close();
            return false;
        }
        db.close();
        int citId = getCitationID(c.getTitle());
        for (int i = 0; i < authorlNames.length; ++i)
        {
            insertAuthor(authorfNames[i], authorlNames[i], citId);
        }

        insertCitationProject(c.getTitle(), DEFAULT_PROJECT);
        return true;

    }

    private int getCitationID(String citationName)
    {
        int id;
        final SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_CITATION + " WHERE "
                + KEY_TITLE + "='" + citationName + "'";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            int count = c.getCount();
            if (count <= 0)
            {
                return c.getCount();
            }

            c.moveToFirst();
        }
        id = c.getInt(c.getColumnIndex(KEY_ID));
        c.close();
        db.close();
        return id;
    }

    public String [] getCitationAuthorfName (int citId)
    {
        final SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> author = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_FNAME + " FROM " + TABLE_AUTHOR + " WHERE "
                + KEY_CITATIONID + "=" + citId + "";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String fName = c.getString(c.getColumnIndex(KEY_FNAME));
                author.add(fName);
            } while (c.moveToNext());
        }
        String [] list = new String[author.size()];
        list = author.toArray(list);
        db.close();
        return list;
    }

    public String [] getCitationAuthorlName (int citId)
    {

        final SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> author = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_LNAME + " FROM " + TABLE_AUTHOR + " WHERE "
                + KEY_CITATIONID + "=" + citId + "";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String lName = c.getString(c.getColumnIndex(KEY_LNAME));
                author.add(lName);
            } while (c.moveToNext());
        }
        String [] list = new String[author.size()];
        list = author.toArray(list);
        db.close();
        return list;
    }

    private int getProjectID(String projectName)
    {
        int id;
        final SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_PROJECT + " WHERE "
                + KEY_NAME + " ='" + projectName + "'";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            c.moveToFirst();
        }

        id = c.getInt(c.getColumnIndex(KEY_ID));
        c.close();
        db.close();
        return id;
    }

    private int getCitationTypeId(String Type)
    {
        int typeId;
        final SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_CITATIONTYPE + " WHERE "
                + KEY_TYPE + " = '" + Type + "'";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            c.moveToFirst();
        }
        typeId = c.getInt(c.getColumnIndex(KEY_ID));
        c.close();
        db.close();
        return typeId;
    }

    public String[] getCitationsByProjectName(final String projectName)
    {
        final SQLiteDatabase db = this.getReadableDatabase();
        ArrayList <String> citations = new ArrayList();
        String selectQuery =
                "SELECT * FROM " + TABLE_CITATION +
                        " C JOIN "  + TABLE_PROJECTCITATION +
                        " PC ON C." + KEY_ID + " = PC." + KEY_CITATIONID +
                        " WHERE PC."  + KEY_PROJECTID +
                        " = (" +
                            "SELECT "  + KEY_ID   + " FROM " + TABLE_PROJECT +
                            " WHERE " + KEY_NAME + " = '" + projectName + "');";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String cTitle = c.getString(c.getColumnIndex(KEY_TITLE));
                citations.add(cTitle);
            } while (c.moveToNext());
        }
        String [] list = new String[citations.size()];
        list = citations.toArray(list);
        db.close();
        return list;
    }

    public void insertCitationProject(String citationTitle , String projectName)
    {
        int citId = getCitationID(citationTitle);
        int proId = getProjectID(projectName);
        insertCitationProject(citId, proId);
    }

    public Citation getCitationByTitle(String citationTitle) {
        final SQLiteDatabase db = this.getReadableDatabase();
        Citation citation = new Citation();
        String selectQuery = "SELECT  * FROM " + TABLE_CITATION + " WHERE "
                + KEY_TITLE + " = '" + citationTitle + "'";

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            c.moveToFirst();
        }

        int typeId = c.getInt(c.getColumnIndex(KEY_TYPEID));
        String Type;
        if (  typeId == 1 )
        {
            Type = "BOOK";
        }
        else if (typeId == 2) {
            Type = "PERIODICAL";
        }
        else {
            Type = "ELECTRONIC";
        }

        citation.setType( Type );
        citation.setContainer( (c.getString(c.getColumnIndex(KEY_CONTAINTER))));
        citation.setTitle( (c.getString(c.getColumnIndex(KEY_TITLE))));
        citation.setSubtitle( (c.getString(c.getColumnIndex(KEY_SUBTITLE))));
        citation.setVolume( (c.getInt(c.getColumnIndex(KEY_VOLUME))));
        citation.setVersion((c.getDouble(c.getColumnIndex(KEY_VERSION))));
        citation.setIssue( (c.getInt(c.getColumnIndex(KEY_ISSUE))));
        citation.setPages( (c.getString(c.getColumnIndex(KEY_PAGES))));
        citation.setUrl( (c.getString(c.getColumnIndex(KEY_URL))));
        citation.setDoi( (c.getString(c.getColumnIndex(KEY_DOI))));
        citation.setLocation( (c.getString(c.getColumnIndex(KEY_LOCATION))));
        citation.setPublisher( (c.getString(c.getColumnIndex(KEY_PUBLISHER))));
        citation.setPubYear( (c.getInt(c.getColumnIndex(KEY_PUBYEAR))));
        citation.setPubDay( (c.getInt(c.getColumnIndex(KEY_PUBDAY))));
        citation.setPubMonth( (c.getInt(c.getColumnIndex(KEY_PUBMONTH))));
        citation.setAccessYear( (c.getInt(c.getColumnIndex(KEY_ACCESSYEAR))));
        citation.setAccessDay( (c.getInt(c.getColumnIndex(KEY_ACCESSDAY))));
        citation.setAccessMonth( (c.getInt(c.getColumnIndex(KEY_ACCESSMONTH))));
        db.close();

        final int citId = getCitationID(citationTitle);
        citation.setfName(getCitationAuthorfName(citId));
        citation.setlName(getCitationAuthorlName(citId));

        return citation;

    }

    public String[] getAllProjects()
    {
        ArrayList<String> projNamesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PROJECT ;

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String pName = c.getString(c.getColumnIndex(KEY_NAME));
                projNamesList.add(pName);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        String []list = new String[projNamesList.size()];
        list = projNamesList.toArray(list);
        return list;
    }

    public void checkIsSet(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_CITATIONTYPE;

        Log.e("SQL QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        int count = c.getInt(0);
        if (count > 0){
            db.close();
            return;
        }
        db.close();
        createTypeVals();
        insertDefaultProject();
    }

    private void createTypeVals()
    {
        insertType("BOOK");
        insertType("ELECTRONIC");
        insertType("PERIODICIAL");
    }

    private void insertFillerCiations()
    {
        Citation c = new Citation();
        String [] fNames ;
        String [] lNames ;
        for (int i = 0; i < 5 ; i++ ) {
            fNames = new String[]{"authFname"+i};
            lNames = new String[]{"authlname"+i};
            c.setType("BOOK");
            c.setContainer("testContainer1" + i);
            c.setTitle("testTitle1" + i);
            c.setSubtitle("testSubtitle1" + i);
            c.setVolume(i);
            c.setVersion(i + i / 10 );
            c.setIssue(i);
            c.setPages("testPages1" + i);
            c.setUrl("testUrl1" + i);
            c.setDoi("testDoi1" + i);
            c.setLocation("testLocation1" + i);
            c.setPublisher("testPublisher1" + i);
            c.setPubYear(2000 + i);
            c.setPubDay(i);
            c.setPubMonth(i + 10);
            c.setAccessYear(2000 + i);
            c.setAccessDay( i);
            c.setAccessMonth(10 + i);

            insertCitation(c, fNames, lNames);
        }
    }

    private void insertDefaultProject()
    {
        insertProject(DEFAULT_PROJECT);
    }
}
