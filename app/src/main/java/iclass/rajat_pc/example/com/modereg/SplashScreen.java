package iclass.rajat_pc.example.com.modereg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);

                }catch (InterruptedException e){
                    e.printStackTrace();

                }finally {
                    Intent intent = new Intent("android.intent.action.LOGINACTIVITY");
                    startActivity(intent);
                    finish();
                }

            }
        };
        thread.start();

    }

}
