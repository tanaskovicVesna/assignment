package rs.pr.priprema25.activities;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;


import rs.pr.priprema25.R;
import rs.pr.priprema25.db.Actor;
import rs.pr.priprema25.db.Movie;
import rs.pr.priprema25.db.ORMLightHelper;

import static android.R.id.message;
import static rs.pr.priprema25.activities.ListActivity.NOTIF_STATUS;
import static rs.pr.priprema25.activities.ListActivity.NOTIF_TOAST;


/**
 * Created by Tanaskovic on 11/20/2016.
 */

public class DetailActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_status";

    private Actor a;
    private TextView name;
    private TextView surname;
    private TextView biography;
    private TextView birth;
    private RatingBar rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //podaci o podesenjima se cuvaju u SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


                //kljuc na osnovu koga nalazimo u bazi glumca a
                int key = getIntent().getExtras().getInt(ListActivity.ACTOR_KEY);
            try {
                a = getDatabaseHelper().getActorDao().queryForId(key);

                name = (TextView) findViewById(R.id.actor_name);
                surname = (TextView) findViewById(R.id.actor_surname);
                biography = (TextView) findViewById(R.id.actor_biography);
                birth = (TextView) findViewById(R.id.actor_birth);
                rating = (RatingBar) findViewById(R.id.acrtor_score);

                //ispisujemo podatke o glumcu u TextView polja
                name.setText(a.getmName());
                surname.setText(a.getmSurname());
                biography.setText(a.getmBiography());
                birth.setText(a.getmBirth());
                rating.setRating(a.getmScore());

            } catch (SQLException e) {
                e.printStackTrace();
            }


        final ListView listView = (ListView) findViewById(R.id.actor_movies);

        try {
            List<Movie> list = getDatabaseHelper().getMovieDao().queryBuilder()
                    .where()
                    .eq(Movie.FIELD_NAME_ACTOR, a.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie m = (Movie) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, m.getmName()+" "+m.getmGenre()+" "+m.getmYear(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.actor_movies);

        if (listview != null){
            ArrayAdapter<Movie> adapter = (ArrayAdapter<Movie>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Movie> list = getDatabaseHelper().getMovieDao().queryBuilder()
                            .where()
                            .eq(Movie.FIELD_NAME_ACTOR, a.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_actor:

                //DIALOG ZA IZMENU PODATAKA
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.action_edit_actor_layout);

                Button add = (Button) dialog.findViewById(R.id.edit_actor);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editName = (EditText) dialog.findViewById(R.id.actor_name);
                        EditText editSurname = (EditText) dialog.findViewById(R.id.actor_surname);
                        EditText editBiography = (EditText) dialog.findViewById(R.id.actor_biography);
                        EditText editBirth = (EditText) dialog.findViewById(R.id.actor_birth);
                        RatingBar editRating = (RatingBar) dialog.findViewById(R.id.acrtor_score);

                        a.setmName(editName.getText().toString());
                        a.setmSurname(editSurname.getText().toString());
                        a.setmBirth(editBirth.getText().toString());
                        a.setmBiography(editBiography.getText().toString());
                        a.setmScore(editRating.getRating());

                        try {
                            getDatabaseHelper().getActorDao().update(a);
                            showMessage("Actor detail updated");

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                dialog.show();
                break;

            case R.id.action_delete_actor:

                try {
                    getDatabaseHelper().getActorDao().delete(a);
                    showMessage("Actor deleted");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
              break;
            case R.id.action_add_movie:

                //DIALOG ZA UNOS PODATAKA O FILMU
                final Dialog dialogM = new Dialog(this);
                dialogM.setContentView(R.layout.action_add_movie_layout);

                Button addM = (Button) dialogM.findViewById(R.id.add_movie);
                addM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialogM.findViewById(R.id.movie_name);
                        EditText genre = (EditText) dialogM.findViewById(R.id.movie_genre);
                        EditText year = (EditText) dialogM.findViewById(R.id.movie_year);

                        Movie m = new Movie();
                        m.setmName(name.getText().toString());
                        m.setmGenre(genre.getText().toString());
                        m.setmYear(year.getText().toString());
                        m.setmActor(a);

                        try {
                            getDatabaseHelper().getMovieDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //URADITI REFRESH
                        refresh();

                        showMessage("New movie added to actor");

                        dialogM.dismiss();
                    }
                });

                dialogM.show();
                break;
        }
                return super.onOptionsItemSelected(item);
    }



    //obavestenje  je zapravo jedan message koji se prosledjuje metodi showStatusMessage()
    private void showStatusMessage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_status_icon);
        mBuilder.setContentTitle("ActorApp");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_icon);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    //metoda showMessage proverava podesenja
    private void showMessage(String message){

        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMessage(message);
        }
    }


    //Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this,ORMLightHelper.class);
        }
        return databaseHelper;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}