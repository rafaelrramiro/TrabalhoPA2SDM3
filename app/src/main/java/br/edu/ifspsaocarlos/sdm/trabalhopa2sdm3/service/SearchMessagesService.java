package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.List;

/**
 * Created by Note on 03/09/2017.
 */

public class SearchMessagesService extends Service {

    public static final String MESSAGE_PARAM = "MESSAGE_PARAM";

    public static final String CONTATO_ORIGEM_PARAM = "CONTATO_ORIGEM_PARAM";

    public static final String NEW_MESSAGE_TASK = "NEW_MESSAGE_TASK";

    private boolean runThread;

    private boolean searchingMessages;

    private static final int timeThread = 3000;

    private MensagemDao mensagemDao;

    private ContatoDao contatoDao;

    private long lastMensagemId;

    private Contato contatoLogado;

    private List<Contato> contatoList;

    private DataUpdateReceiver dataUpdateReceiver;

    public SearchMessagesService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        mensagemDao = daoSession.getMensagemDao();
        contatoDao = daoSession.getContatoDao();

        List<Mensagem> mensagemList = mensagemDao.queryBuilder().limit(1).list();
        lastMensagemId = mensagemList.size()>0?mensagemList.get(0).getId():0;

        contatoList = contatoDao.queryBuilder().list();

        contatoLogado = Utils.getContatoLogado(this);

        runThread = true;
        searchingMessages = false;
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (dataUpdateReceiver == null)
            dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(SearchContactsService.NEW_CONTATO_TASK);
        registerReceiver(dataUpdateReceiver, intentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    private Thread thread = new Thread(){
        @Override
        public void run() {
            while (runThread) {
                try {
                    Thread.sleep(timeThread);
                } catch (InterruptedException e) {e.printStackTrace();}

                for (Contato contato : contatoList){
                    searchingMessages = true;
                    WebService.buscaMensagens(lastMensagemId, contato, contatoLogado, SearchMessagesService.this, handler);
                    while(searchingMessages){}
                }
            }
        }
    };

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WebService.SUCESS:
                    Gson gson = new Gson();
                    List<Mensagem> mensagemList = gson.fromJson((String)msg.obj, new TypeToken<List<Mensagem>>(){}.getType());
                    saveMensagens(mensagemList);
                    break;
                case WebService.ERROR:
                    break;
                default:
                    break;
            }
            searchingMessages = false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dataUpdateReceiver != null)
            unregisterReceiver(dataUpdateReceiver);

        runThread = false;

        stopSelf();
    }

    public void saveMensagens(List<Mensagem> mensagemList){
        for (Mensagem mensagem : mensagemList){
            Mensagem mensagemAux = mensagemDao.queryBuilder()
                    .where(MensagemDao.Properties.Id.eq(mensagem.getId()))
                    .unique();
            if (mensagemAux==null){
                mensagemDao.insert(mensagem);
                lastMensagemId = mensagem.getId();
                Intent intent = new Intent(NEW_MESSAGE_TASK);
                intent.putExtra(CONTATO_ORIGEM_PARAM, mensagem.getOrigem().getId());
                intent.putExtra(MESSAGE_PARAM, mensagem);
                sendBroadcast(intent);
                notifyMessage(mensagem);
            }
        }
    }

    public void notifyMessage(Mensagem mensagem){
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, TalkActivity.class);
        intent.putExtra(TalkActivity.OTHER_CONTATO_PARAM, mensagem.getOrigem());
        PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_message_text_w);
        builder.setTicker(getString(R.string.title_new_message));
        builder.setContentTitle(mensagem.getOrigem().getNome_completo());
        builder.setContentText(mensagem.getCorpo());
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(p);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_message_text_w));
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        notification.vibrate = new long[] {100, 250};
        nm.notify(R.drawable.ic_message_text_w, notification);
    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SearchContactsService.NEW_CONTATO_TASK))
                contatoList.add((Contato) intent.getSerializableExtra(SearchContactsService.CONTATO_PARAM));
        }
    }

}