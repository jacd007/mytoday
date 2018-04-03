package com.zippyttech.mytoday;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zippyttech.mytoday.common.ApiCall;
import com.zippyttech.mytoday.models.Noticia;
import com.zippyttech.mytoday.models.NoticiasSQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHARED_KEY ="shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        settings = getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = findViewById(R.id.lista);

      //GetData getData = new GetData(this);
     // getData.execute();
         NoticiasDB noticiasDB = new NoticiasDB(this);
    refreshCustomerList(noticiasDB.getList());
      //  noticiasDB.fillDB();

    }

    public void llenarLista(){

    }

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerAdapter adapter;

    public void refreshCustomerList(List<Noticia> listado) {
        if (listado != null) {
           recyclerView.setHasFixedSize(true);
            // layoutManager = new LinearLayoutManager(this);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RecyclerAdapter(listado, this, this);
            recyclerView.setAdapter(adapter);

        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            editor.clear();
            editor.commit();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public class GetData extends AsyncTask<String,String,String> {
     private    ApiCall call;
     private ProgressDialog dialog;
        public GetData(Context context){
        this.call = new ApiCall(context);
        dialog = new ProgressDialog(context);
        dialog.setMessage("Cargando data");
        dialog.setIndeterminate(true);
        dialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
             String resp = call.callGet("https://lanacionweb.com/wp-json/wp/v2/posts");
            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            dialog.dismiss();
            try {
                JSONArray array = new JSONArray(resp);
                List<Noticia> noticiaList = new ArrayList<>();

                for(int i=0; i<array.length(); i++){
                    JSONObject item = array.getJSONObject(i);
                    Noticia noticia = new Noticia();
                    noticia.setTitulo(item.getJSONObject("title").getString("rendered"));
                    noticia.setContenido(item.getJSONObject("excerpt").getString("rendered"));
                  //  noticia.setFecha(item.getJSONObject("date").getString("date"));
                    noticia.setFecha(item.getJSONObject("_links").getJSONArray("self").getJSONObject(0).getString("href"));
                    noticiaList.add(noticia);

                }
                refreshCustomerList(noticiaList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
