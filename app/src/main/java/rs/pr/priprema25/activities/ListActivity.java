package rs.pr.priprema25.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import rs.pr.priprema25.R;
import rs.pr.priprema25.db.Actor;
import rs.pr.priprema25.db.ORMLightHelper;
import rs.pr.priprema25.dialogs.AboutDialog;
import rs.pr.priprema25.preferences.PrefsActivity;



@CoordinatorLayout.DefaultBehavior(FloatingActionButton.Behavior.class)
public class ListActivity extends AppCompatActivity  {

    private ORMLightHelper databaseHelper;
    //kljuc za intent kad kliknemo na ACTOR da otvori novu aktivnost koja prikazuje podatke o glumcu
    public static String ACTOR_KEY = "ACTOR_KEY";

    private SharedPreferences prefs;
    //podesenja aplikacije, u zadatku predstavljaju mogucnost da je ukljuceno/iskljuceno obavestenje o unosu,izmeni i brisanju podataka
    //kljuc toast notifikacije i kljuc statusbar notifikacije
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";

    public FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //podaci o podesenjima se cuvaju u SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.lista_glumaca);
        try {
            List<Actor> list = getDatabaseHelper().getActorDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Actor p = (Actor) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                    intent.putExtra(ACTOR_KEY, p.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Toast.makeText(ListActivity.this, "Lista glumaca", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add_actor:

                //DIALOG ZA UNOS PODATAKA
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.action_add_actor_layout);

                Button add = (Button) dialog.findViewById(R.id.add_actor);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.actor_name);
                        EditText surname = (EditText) dialog.findViewById(R.id.actor_surname);
                        EditText biography = (EditText) dialog.findViewById(R.id.actor_biography);
                        RatingBar rating = (RatingBar) dialog.findViewById(R.id.acrtor_score);
                        EditText birth = (EditText) dialog.findViewById(R.id.actor_birth);

                        Actor a = new Actor();
                        a.setmName(name.getText().toString());
                        a.setmSurname(surname.getText().toString());
                        a.setmBiography(biography.getText().toString());
                        a.setmBirth(birth.getText().toString());
                        a.setmScore(rating.getRating());

                        try {
                            getDatabaseHelper().getActorDao().create(a);
                            //obavezno refresh() da se prikaze novi sadrzaj koji je unesen u bazu
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                         showMessage("Added new actor");

                       /* Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

                        dialog.dismiss();

                    }
                });

                dialog.show();
                break;


            case R.id.action_about:

                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.action_settings:
                startActivity(new Intent(ListActivity.this, PrefsActivity.class));
                break;
        }
                return super.onOptionsItemSelected(item);
        }
    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    // refresh() prikazuje novi sadrzaj.Povucemo nov sadrzaj iz baze i popunimo listu
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.lista_glumaca);

        if (listview != null){
            ArrayAdapter<Actor> adapter = (ArrayAdapter<Actor>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Actor> list = getDatabaseHelper().getActorDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //obavestenje prikazano pomocu NotificationManager-a je zapravo jedan message koji se prosledjuje metodi showStatusMessage()
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


    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
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
