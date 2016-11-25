package rs.pr.priprema25.db;



import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by Tanaskovic on 11/22/2016.
 */
@DatabaseTable(tableName = Actor.TABLE_NAME_ACTORS)
public class Actor  {
    public static final String TABLE_NAME_ACTORS="actors";
    public static final String FIELD_NAME_ID="id";
    public static final String FIELD_NAME_NAME="name";
    public static final String FIELD_NAME_SURNAME="surname";
    public static final String FIELD_NAME_BIOGRAPHY="biography";
    public static final String FIELD_NAME_SCORE="score";
    public static final String FIELD_NAME_BIRTH="birth";
    public static final String FIELD_NAME_MOVIES="movies";

    //veza 1 na vise
    //     1 glumac u vise filmova

    @ForeignCollectionField(columnName=Actor.FIELD_NAME_MOVIES,eager=true)
    private ForeignCollection<Movie> movies;

    @DatabaseField(columnName =FIELD_NAME_ID,generatedId = true)
    private int mId;
    @DatabaseField(columnName =FIELD_NAME_NAME)
    private String mName;
    @DatabaseField(columnName =FIELD_NAME_SURNAME)
    private String mSurname;
    @DatabaseField(columnName =FIELD_NAME_BIOGRAPHY)
    private String mBiography;
    @DatabaseField(columnName =FIELD_NAME_SCORE)
    private float mScore;
    @DatabaseField(columnName =FIELD_NAME_BIRTH)
    private String mBirth;


   //obavezan konstruktor bez parametara

    public Actor() {
    }

    public ForeignCollection<Movie> getMovies() {return movies;}

    public void setMovies(ForeignCollection<Movie> movies) {this.movies = movies;}

    public int getmId() {return mId;}

    public void setmId(int mId) {this.mId = mId;}

    public String getmName() {return mName;}

    public void setmName(String mName) {this.mName = mName;}

    public String getmSurname() {return mSurname;}

    public void setmSurname(String mSurname) {this.mSurname = mSurname;}

    public String getmBiography() {return mBiography;}

    public void setmBiography(String mBiography) {this.mBiography = mBiography;}

    public float getmScore() {return mScore;}

    public void setmScore(float mScore) {this.mScore = mScore;}

    public String getmBirth() {return mBirth;}

    public void setmBirth(String mBirth) {this.mBirth = mBirth;}

    //obavezno override toString

    @Override
    public String toString() {
        return mName;}
}
