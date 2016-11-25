package rs.pr.priprema25.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static rs.pr.priprema25.db.Movie.FIELD_NAME_ACTOR;

/**
 * Created by Tanaskovic on 11/22/2016.
 */
@DatabaseTable(tableName = Movie.TABLE_NAME_MOVIES)
public class Movie {

    public static final String TABLE_NAME_MOVIES = "movies";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_GENRE = "genre";
    public static final String FIELD_NAME_YEAR = "year";
    public static final String FIELD_NAME_ACTOR = "actor";

    public Movie() {
    }

    @DatabaseField(columnName =FIELD_NAME_ACTOR,foreign =true,foreignAutoRefresh = true)
    private Actor mActor;

    @DatabaseField(columnName =FIELD_NAME_ID,generatedId = true)
    private int mId;
    @DatabaseField(columnName =FIELD_NAME_NAME)
    private String mName;
    @DatabaseField(columnName =FIELD_NAME_GENRE)
    private String mGenre;
    @DatabaseField(columnName =FIELD_NAME_YEAR)
    private String mYear;


    public Actor getmActor() {return mActor;}

    public void setmActor(Actor mActor) {this.mActor = mActor;}

    public int getmId() {return mId;}

    public void setmId(int mId) {this.mId = mId;}

    public String getmName() {return mName;}

    public void setmName(String mName) {this.mName = mName;}

    public String getmGenre() {return mGenre;}

    public void setmGenre(String mGenre) {this.mGenre = mGenre;}

    public String getmYear() {return mYear;}

    public void setmYear(String mYear) {this.mYear = mYear;}

    @Override
    public String toString() {
        return mName;
    }

}
