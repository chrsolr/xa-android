package io.keypunchers.xa.app;

import android.os.*;
import io.keypunchers.xa.*;
import io.keypunchers.xa.loaders.GameDetailsLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.models.GameDetails;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<GameDetails>
{
	private GameDetails mData = new GameDetails();
	private String GAME_PERMALINK;
	private String BASE_URL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (getIntent().getExtras() != null) {
			GAME_PERMALINK = getIntent().getExtras().getString("game_permalink");
			BASE_URL = Common.getGameAchievementsUrlByPermalink(GAME_PERMALINK);
		}
		
		Toast.makeText(this, BASE_URL, Toast.LENGTH_SHORT).show();

		makeNetworkCall();
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

	@Override
	public Loader<GameDetails> onCreateLoader(int id, Bundle args) {
		return new GameDetailsLoader(this, BASE_URL);
	}

	@Override
	public void onLoadFinished(Loader<GameDetails> loader, GameDetails data) {
		mData = data;

		if (getSupportActionBar() != null)
			getSupportActionBar().setTitle(mData.getTitle());
	}

	@Override
	public void onLoaderReset(Loader<GameDetails> loader) {

	}

	private void makeNetworkCall() {
		getSupportLoaderManager().restartLoader(0, null, this);
	}
}
