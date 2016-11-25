package rs.pr.priprema25.drugiDeo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

import rs.pr.priprema25.R;
import rs.pr.priprema25.activities.ListActivity;
import rs.pr.priprema25.db.Actor;
import rs.pr.priprema25.db.ORMLightHelper;


/**
 * Created by Tanaskovic on 11/20/2016.
 */

public class DrugiDetailActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;

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

                int key = getIntent().getExtras().getInt(ListActivity.ACTOR_KEY);
            try {
                a = getDatabaseHelper().getActorDao().queryForId(key);

                name = (TextView) findViewById(R.id.actor_name);
                surname = (TextView) findViewById(R.id.actor_surname);
                biography = (TextView) findViewById(R.id.actor_biography);
                birth = (TextView) findViewById(R.id.actor_birth);
                rating = (RatingBar) findViewById(R.id.acrtor_score);

                name.setText(a.getmName());
                surname.setText(a.getmSurname());
                biography.setText(a.getmBiography());
                birth.setText(a.getmBirth());
                rating.setRating(a.getmScore());

            } catch (SQLException e) {
                e.printStackTrace();
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
                //...
                return true;
            case R.id.action_delete_actor:
                //...
                return true;
            case R.id.action_add_movie:
                //..
                return true;
            default:
                return super.onOptionsItemSelected(item);

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