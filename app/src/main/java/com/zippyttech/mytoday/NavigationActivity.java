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
import android.widget.Toast;

import com.zippyttech.mytoday.common.ApiCall;
import com.zippyttech.mytoday.models.Noticia;
import com.zippyttech.mytoday.models.NoticiasSQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHARED_KEY ="shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    NoticiasDB noticiasDB;
    public static boolean val=false;

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
      noticiasDB = new NoticiasDB(this);
    refreshCustomerList(noticiasDB.getList());
      //  noticiasDB.fillDB();

        ArrayList<Noticia> lista_noticia= null;

        if(settings.getBoolean("banderadb",false)){
           refreshCustomerList(noticiasDB.getList()); ;
            Toast.makeText(this,"Consultando BD...",Toast.LENGTH_SHORT).show();
        }
        else{
            GetData getData = new GetData(this,0);
              getData.execute();
            Toast.makeText(this,"Consultando Servidor...",Toast.LENGTH_SHORT).show();
        }

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
       // else Toast.makeText(this,"hay algo",Toast.LENGTH_SHORT).show();

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

        if (id == R.id.nav_delete) { /** DELETE **/
            noticiasDB.DeleteNoticiasDB();
            settings.getBoolean("banderadb",false);
            adapter.changeDataItem(noticiasDB.getList());

        } else if (id == R.id.nav_insert) { /** INSERT **/
                Toast.makeText(this,"INSERT",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_update) { /** UPDATE **/

          //  noticiasDB.UpdateNoticiasDB();
           // refreshCustomerList(noticiasDB.getList());
            GetData getData = new GetData(this,1);
            getData.execute();


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
     private int UPDATE;
        public GetData(Context context, int upd){
        this.call = new ApiCall(context);
        this.UPDATE=upd;
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

                for(int i=0; i<array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Noticia noticia = new Noticia();
                   String c = item.getJSONObject("excerpt").getString("rendered").replace("<p>","").replace("<strong>","").replace("&#8220","\"").replace("&#8221","\"");
                    noticia.setCodigo(String.valueOf(item.getInt("id")));
                    noticia.setTitulo(item.getJSONObject("title").getString("rendered"));
                    noticia.setContenido(c);
                    // noticia.setFecha(item.getString("date"));
                    noticia.setFecha("Fecha: "+item.getString("date").substring(0,16).replace("-","/").replace("T"," Hora: "));
                    noticia.setImagen("");
                    //    noticia.setFecha(item.getJSONObject("_links").getJSONArray("self").getJSONObject(0).getString("href"));
                    noticiaList.add(noticia);

                }
                noticiasDB.insertarNoticias(noticiaList);
                editor.putBoolean("banderadb",true);
                editor.commit();
               if(UPDATE==0)
                refreshCustomerList(noticiasDB.getList());
               else
                   adapter.changeDataItem(noticiasDB.getList());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
