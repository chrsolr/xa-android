package io.keypunchers.xa.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import io.keypunchers.xa.R;

public class AchievementCommentsActivity extends AppCompatActivity {
    private String BASE_URL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ach_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            BASE_URL = getIntent().getExtras().getString("comments_url");
        }

        Toast.makeText(this, BASE_URL, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_item_open_in_browser:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
