package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends Activity {
    public static final String EXTRA_TYPE = "io.appmaven.bomberman.TYPE";
    public static final String EXTRA_MONIKER = "io.appmaven.bomberman.MONIKER";

    private String moniker = "Player";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_launch);
    }

    public void hostNode(View view) {
        this.moniker = ((EditText)findViewById(R.id.moniker)).getText().toString();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_TYPE, 0);
        intent.putExtra(EXTRA_MONIKER, this.moniker);

        startActivity(intent);
    }

    public void joinNode(View view) {
        this.moniker = ((EditText)findViewById(R.id.moniker)).getText().toString();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_TYPE, 1);
        intent.putExtra(EXTRA_MONIKER, this.moniker);
        startActivity(intent);
    }
}
