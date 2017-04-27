package io.keypunchers.xa.app;

import android.os.*;
import io.keypunchers.xa.*;
import android.view.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity
{
	private String GAME_PERMALINK;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (getIntent().getExtras() != null) {
			GAME_PERMALINK = getIntent().getExtras().getString("game_permalink");
		}
		
		Toast.makeText(this, GAME_PERMALINK, Toast.LENGTH_SHORT).show();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
