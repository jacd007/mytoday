package com.zippyttech.mytoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String SHARED_KEY ="shared_key";
    EditText user,pass;
    Button ingresar;


    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.users);
        pass = (EditText) findViewById(R.id.pass);
        ingresar = (Button) findViewById(R.id.ingresar);
       settings = getSharedPreferences(SHARED_KEY,0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       ingresar.setOnClickListener(this);

       if(settings.getBoolean("logged",false)){

           Intent navigation = new Intent(this, NavigationActivity.class);
           startActivity(navigation);
           this.finish();
       }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ingresar){
            if(user.getText().toString().equals("loro") && pass.getText().toString().equals("123456")) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged",true);
                editor.putString("user",user.getText().toString());
                editor.commit();
                this.finish();
                Intent navigation = new Intent(this, NavigationActivity.class);
                startActivity(navigation);
            }
            else {
                Toast.makeText(this,"Error de usuario y/o contraseña",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
