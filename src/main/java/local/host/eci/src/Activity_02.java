package local.host.eci.src;

import local.host.eci.src.Interface.View.Adapter.InfinitePagerAdapter;
import local.host.eci.src.Utils.Utils;
import local.host.eci.R;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;

import android.text.method.LinkMovementMethod;
import android.content.DialogInterface;

import android.view.ViewGroup;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;

import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.text.Html;


public class Activity_02 extends AppCompatActivity implements ViewPager.OnPageChangeListener, Runnable, View.OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    private long m_LastTimePressBack = System.currentTimeMillis();
    private static long m_TimeToExitBackPress = 2000;
    private SearchView m_SView = null;

    @Override protected void onCreate(Bundle SavedInstanceState)
    {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_02);

        Toolbar ToolBar = (Toolbar) findViewById(R.id.lyt_toolbar);
        ViewPager ViewPager = (ViewPager) findViewById(R.id.lyt_news_pager);

        ToolBar.setPadding(0, Utils.GetStatusBarHeight(this), 0, 0);
        setSupportActionBar(ToolBar); getSupportActionBar().setTitle(getString(R.string.activity_02_toolbar));

        ViewPager.setOffscreenPageLimit(5);
        ViewPager.addOnPageChangeListener(this);
        ViewPager.setAdapter(new InfinitePagerAdapter(this));

        findViewById(R.id.lyt_page_indicator).setOnClickListener(this);
        new Thread(this).start();
    }

    @Override public void onClick(View v)
    {
    }

    @Override public boolean onQueryTextSubmit(String query)
    {
        m_SView.setQueryHint("");
        m_SView.setIconified(true);
        m_SView.onActionViewCollapsed();

        if(query.length() < 3)
        {
            Toast.makeText(this, getString(R.string.toast_minsearch_length), Toast.LENGTH_SHORT).show();
            return false;
        }

        Intent SearchIntent = new Intent(this, Activity_04.class);
        String QuerySearch = query.trim();

        SearchIntent.putExtra("SEARCH_QUERY", QuerySearch);
        startActivity(SearchIntent);

        return false;
    }

    @Override public boolean onQueryTextChange(String newText)
    {
        return false;
    }

    @Override public boolean onClose()
    {
        TextView SearchText = (TextView) m_SView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        SearchText.setText("");

        m_SView.setQueryHint("");
        return false;
    }

    @Override public boolean onCreateOptionsMenu(Menu Menu)
    {
        getMenuInflater().inflate(R.menu.activity_02, Menu);
        m_SView = (SearchView) Menu.findItem(R.id.menu_search).getActionView();

        m_SView.setOnSearchClickListener(this);
        m_SView.setOnQueryTextListener(this);
        m_SView.setOnCloseListener(this);

        return true;
    }

    @Override public void onResume()
    {
        super.onResume();
        findViewById(R.id.lyt_toolbar).getBackground().setAlpha(255);
    }

    @Override public boolean onOptionsItemSelected(MenuItem Item)
    {
        if (Item.getItemId() == R.id.menu_about)
        {
            ShowAbout();
            return true;
        }
        else if(Item.getItemId() == R.id.menu_updates)
        {
            ShowUpdatesLog();
            return true;
        }

        return super.onOptionsItemSelected(Item);
    }

    @Override public boolean onKeyDown(int KeyCode, KeyEvent Event)
    {
        if (KeyCode == KeyEvent.KEYCODE_BACK)
        {
            if(m_LastTimePressBack + m_TimeToExitBackPress > System.currentTimeMillis()) finish();
            else Toast.makeText(this, getString(R.string.toast_backpress_exit), Toast.LENGTH_SHORT).show();

            m_LastTimePressBack = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(KeyCode, Event);
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override public void onPageSelected(int position)
    {
        TextView PageIndicator = (TextView) findViewById(R.id.lyt_page_number);
        PageIndicator.setText("" + (position + 1));
    }

    @Override public void onPageScrollStateChanged(int state)
    {
    }

    @Override public void run()
    {
        ViewPager ViewPager = (ViewPager) findViewById(R.id.lyt_news_pager);
        while(ViewPager.getChildCount() == 0) try{ Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        final ProgressBar ProgessBar = (ProgressBar) findViewById(R.id.lyt_progess);
        runOnUiThread(new Runnable() { @Override public void run() { ProgessBar.setVisibility(View.GONE); }});
    }

    private Boolean isNumber(String Val)
    {
        byte[] nVal = Val.getBytes();
        for(int i = 0; i < nVal.length; ++i) if(nVal[i] < '0' || nVal[i] > '9') return false;

        return true;
    }

    private void ShowAbout()
    {
        AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(this);


        String Message = "<strong>Versión</strong>: 1.0.0<br />";
        Message += "Andrei Jianu<br /><br />";

        Message += "<strong>Disclaimer</strong>: Esta es una aplicación no oficial desarrollada por un lector de la web del <a href=\"https://elchapuzasinformatico.com\">chapuzasinformatico</a>. ";
        Message += "No existe ninguna afiliación con <em>elchapuzasinformatico.com</em>.<br /> <br />";

        Message += "<strong>Código fuente</strong>:<br />";
        Message += "<a href=\"https://github.com/iFuSiiOnzZ/ECI\">https://github.com/iFuSiiOnzZ/ECI</a><br /><br />";

        Message += "<strong>Haz tu contribución</strong> (PayPal):<br />";
        Message += "andreijianu@hotmail.com";

        TextView TextAbout = new TextView(this);
        TextAbout.setText(Html.fromHtml(Message));

        TextAbout.setPadding(75, 25, 25, 25);
        TextAbout.setMovementMethod(LinkMovementMethod.getInstance());

        AlertBuilder.setView(TextAbout);
        AlertBuilder.setTitle(R.string.menu_about);

        AlertBuilder.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener()
        {
            @Override public void onClick(DialogInterface dialog, int which) {}
        });

        AlertBuilder.setIcon(R.drawable.ic_launcher);
        AlertBuilder.setCancelable(false);

        AlertBuilder.create().show();
    }

    private void ShowUpdatesLog()
    {
        String Message = "<strong>Leyenda</strong><br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;# Mejoras<br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;* Nuevas características <br />";
        Message += "<br />";

        Message += "<strong>Código versiones (x.y.z)</strong><br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;<strong>x</strong>: Nueva versión (incompatibilidad con la anterior)<br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;<strong>y</strong>: Nuevas características<br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;<strong>z</strong>: Corrección de fallos<br />";
        Message += "<br />";

        Message += "<strong>1.0.0</strong><br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;* Sistema de búsqueda<br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;* Reproducción vídeos Youtube<br />";
        Message += "&nbsp;&nbsp;&nbsp;&nbsp;* Compartir, abrir en nagegador<br />";
        Message += "<br />";

        ScrollView LogScroll = new ScrollView(this);
        LogScroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        HorizontalScrollView hScrollView = new HorizontalScrollView(this);
        hScrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        TextView LogText = new TextView(this);
        LogText.setText(Html.fromHtml(Message));
        LogText.setPadding(75, 25, 25, 25);

        hScrollView.addView(LogText);
        LogScroll.addView(hScrollView);

        AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(this);
        AlertBuilder.setTitle(R.string.dialog_title_log_updates);

        AlertBuilder.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener()
        {
            @Override public void onClick(DialogInterface dialog, int which) {}
        });

        AlertBuilder.setIcon(R.drawable.ic_launcher);
        AlertBuilder.setCancelable(false);

        AlertBuilder.setView(LogScroll);
        AlertBuilder.create().show();
    }
}
