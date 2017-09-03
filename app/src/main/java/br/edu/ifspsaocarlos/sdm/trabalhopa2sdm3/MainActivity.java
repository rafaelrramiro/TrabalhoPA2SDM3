package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FragmentActivityListener{

    private Intent serviceSearchContactsIntent;

    private Intent serviceSearchMessagesIntent;

    private TabsAdapter tabsAdapter;

    private ViewPager viewPager;

    private DaoSession daoSession;

    private DataUpdateReceiver dataUpdateReceiver;

    private Contato contatoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        daoSession = ((App)getApplication()).getDaoSession();

        contatoAtual = Utils.getContatoLogado(this);

        tabsAdapter = new TabsAdapter(this, getSupportFragmentManager(),
                new AppStatus(contatoAtual, daoSession.getContatoDao().queryBuilder()
                        .where(new WhereCondition.StringCondition("_id IN " +
                                "(SELECT origem_id FROM mensagem) OR " +
                                "_id IN (SELECT destino_id FROM mensagem)"))
                        .list()),
                new AppStatus(contatoAtual,daoSession.getContatoDao().queryBuilder().list()));

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(tabsAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        serviceSearchContactsIntent = new Intent(getApplicationContext(), SearchContactsService.class);
        startService(serviceSearchContactsIntent);

        serviceSearchMessagesIntent = new Intent(getApplicationContext(), SearchMessagesService.class);
        startService(serviceSearchMessagesIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataUpdateReceiver == null)
            dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilterNewContato = new IntentFilter(SearchContactsService.NEW_CONTATO_TASK);
        IntentFilter intentFilterNewMessage = new IntentFilter(SearchMessagesService.NEW_MESSAGE_TASK);
        registerReceiver(dataUpdateReceiver, intentFilterNewContato);
        registerReceiver(dataUpdateReceiver, intentFilterNewMessage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null)
            unregisterReceiver(dataUpdateReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        stopService(serviceSearchContactsIntent);
        super.onDestroy();
    }

    @Override
    public void updateTalks() {
        Fragment fragment = tabsAdapter.getFragment(0);
        ((TabFragment)fragment).updateView(new AppStatus(contatoAtual, daoSession.getContatoDao().queryBuilder()
                .where(new WhereCondition.StringCondition("_id IN " +
                        "(SELECT origem_id FROM mensagem) OR " +
                        "_id IN (SELECT destino_id FROM mensagem)"))
                .list()));
    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SearchContactsService.NEW_CONTATO_TASK)) {
                Fragment fragment = tabsAdapter.getFragment(1);
                ((TabFragment)fragment).addContato((Contato) intent.getSerializableExtra(SearchContactsService.CONTATO_PARAM));
            }
            else if (intent.getAction().equals(SearchMessagesService.NEW_MESSAGE_TASK)) {
                updateTalks();
            }
        }
    }

}