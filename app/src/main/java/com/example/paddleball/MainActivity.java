/* Akash Subedi
CS 443 Final Project
5/23/2019
 A simple paddle ball application ,
 */


package com.example.paddleball;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    // Implement bounceThread
    BounceThread bounceThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         // Create a display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        bounceThread = new BounceThread(this, size.x, size.y);
        setContentView(bounceThread);
    }

  //  When user starts the game implement resume and pause methods to make gameplay smooth
    @Override
    //Call when the application resumes
    protected void onResume() {
        super.onResume();
        bounceThread.resume();
    }


    @Override
    // Call wehn the appplication ends
    protected void onPause() {
        super.onPause();

        bounceThread.pause();
    }
}
