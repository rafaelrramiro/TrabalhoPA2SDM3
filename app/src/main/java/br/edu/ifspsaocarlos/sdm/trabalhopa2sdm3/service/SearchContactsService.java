package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.List;

/**
 * Created by Note on 03/09/2017.
 */

public class SearchContactsService extends Service {

    public static final String CONTATO_PARAM = "CONTATO_PARAM";

    public static final String NEW_CONTATO_TASK = "NEW_CONTATO_TASK";

    private boolean runThread;

    private boolean searchingContacts;

    private static final int timeThread = 3000;

    private ContatoDao contatoDao;

    private long lastContatoId;

    private Contato contatoLogado;

    public SearchContactsService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        contatoDao = daoSession.getContatoDao();

        List<Contato> contatos = contatoDao.queryBuilder().limit(1).list();
        lastContatoId = contatos.size()>0?contatos.get(0).getId():0;

        contatoLogado = Utils.getContatoLogado(this);

        runThread = true;
        searchingContacts = false;
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private Thread thread = new Thread(){
        @Override
        public void run() {
            while (runThread) {
                try {
                    Thread.sleep(timeThread);
                } catch (InterruptedException e) {e.printStackTrace();}

                if (searchingContacts)
                    continue;

                searchingContacts = true;
                WebService.buscaContatos(SearchContactsService.this, handler);
            }
        }
    };

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WebService.SUCESS:
                    Gson gson = new Gson();
                    List<Contato> contatos = gson.fromJson((String)msg.obj, new TypeToken<List<Contato>>(){}.getType());
                    saveContacts(contatos);
                    break;
                case WebService.ERROR:
                    break;
                default:
                    break;
            }
            searchingContacts = false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        runThread = false;
        stopSelf();
    }

    public void saveContacts(List<Contato> contatos){
        for (Contato contato : contatos){
            if ((contato.getId()>lastContatoId) &&
                    (!contato.getId().equals(contatoLogado.getId())) &&
                    (contato.getApelido().trim().length() > 0) &&
                    (contato.getNome_completo().trim().length() > 0) &&
                    (contato.getApelido().equals(Utils.getAppIdBase64(contato.getNome_completo())))) {
                Contato contatoAux = contatoDao.queryBuilder()
                        .where(ContatoDao.Properties.Id.eq(contato.getId()))
                        .unique();
                if (contatoAux==null){
                    contatoDao.insert(contato);
                    lastContatoId = contato.getId();
                    Intent intent = new Intent(NEW_CONTATO_TASK);
                    intent.putExtra(CONTATO_PARAM, contato);
                    sendBroadcast(intent);
                }
            }
        }
    }

}