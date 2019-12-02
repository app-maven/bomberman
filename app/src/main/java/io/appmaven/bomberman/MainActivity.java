package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {
    public static final String EXTRA_MESSAGE = "io.appmaven.bomberman.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launch);
    }

    public void hostNode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, 0);
        startActivity(intent);
    }

    public void joinNode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, 1);
        startActivity(intent);
    }

}
