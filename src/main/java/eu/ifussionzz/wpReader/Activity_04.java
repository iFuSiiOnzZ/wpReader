package eu.ifussionzz.wpReader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import eu.ifussionzz.wpReader.Interface.AsyncTask.GetSearchQuery;
import eu.ifussionzz.wpReader.Interface.View.Adapter.ExcerptAdapter;
import eu.ifussionzz.wpReader.Utils.Utils;
import local.host.eci.R;

public class Activity_04 extends AppCompatActivity implements Runnable
{
    @Override protected void onCreate(Bundle SavedInstanceState)
    {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_04);

        Toolbar ToolBar = (Toolbar) findViewById(R.id.lyt_toolbar);
        RecyclerView DataView = (RecyclerView) findViewById(R.id.lyt_news_view);

        ToolBar.setPadding(0, Utils.GetStatusBarHeight(this), 0, 0);
        setSupportActionBar(ToolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("SEARCH_QUERY"));

        DataView.setAdapter(new ExcerptAdapter(null));
        DataView.setLayoutManager(new LinearLayoutManager(this));

        new GetSearchQuery(this, DataView).execute(getIntent().getStringExtra("SEARCH_QUERY"));
        new Thread(this).start();
    }

    @Override public void onResume()
    {
        super.onResume();
        findViewById(R.id.lyt_toolbar).getBackground().setAlpha(255);
    }

    @Override public void run()
    {
        RecyclerView DataView = (RecyclerView) findViewById(R.id.lyt_news_view);
        while(DataView.getChildCount() == 0) try{ Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        final ProgressBar ProgessBar = (ProgressBar) findViewById(R.id.lyt_progess);
        runOnUiThread(new Runnable() { @Override public void run() { ProgessBar.setVisibility(View.GONE); }});
    }

    @Override public boolean onOptionsItemSelected(MenuItem Item)
    {
        if (Item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(Item);
    }
}
