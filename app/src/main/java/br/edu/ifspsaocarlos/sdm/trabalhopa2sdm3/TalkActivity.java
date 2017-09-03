package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Note on 03/09/2017.
 */

public class TalkActivity extends AppCompatActivity {

    private Contato contatoAtual;

    private Contato otherContato;

    private MensagemDao mensagemDao;

    private ListView lvMessages;

    private EditText editTextMensagem;

    private ImageButton imageButtonEnviar;

    private MensagemAdapter mensagemAdapter;

    private InputMethodManager inputManager;

    private DataUpdateReceiver dataUpdateReceiver;

    public static final String OTHER_CONTATO_PARAM = "OTHER_CONTATO_PARAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        setResult(RESULT_CANCELED);

        contatoAtual = Utils.getContatoLogado(this);
        otherContato = (Contato) getIntent().getSerializableExtra(OTHER_CONTATO_PARAM);
        if (otherContato==null) {
            finish();
            return;
        }

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        mensagemDao = daoSession.getMensagemDao();

        lvMessages = (ListView) findViewById(R.id.lvMessages);
        lvMessages.setDivider(null);

        QueryBuilder<Mensagem> queryBuilder = mensagemDao.queryBuilder();
        queryBuilder.where(queryBuilder.or(MensagemDao.Properties.Destino_id.eq(otherContato.getId()),
                MensagemDao.Properties.Origem_id.eq(otherContato.getId())))
                .orderAsc(MensagemDao.Properties.Id);
        List<Mensagem> mensagemList = queryBuilder.list();

        mensagemAdapter = new MensagemAdapter(this, mensagemList, contatoAtual);
        lvMessages.setAdapter(mensagemAdapter);

        editTextMensagem = (EditText) findViewById(R.id.etMensagem);
        imageButtonEnviar = (ImageButton) findViewById(R.id.ibEnviar);

        imageButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextMensagem.getText().toString().trim().length()<=0)
                    return;

                Mensagem mensagem = new Mensagem();
                mensagem.setOrigem(contatoAtual);
                mensagem.setDestino(otherContato);
                mensagem.setCorpo(editTextMensagem.getText().toString());

                WebService.cadastraMensagem(mensagem, TalkActivity.this, handler);

                mensagemAdapter.add(mensagem);
                mensagemAdapter.notifyDataSetChanged();

                editTextMensagem.setText("");
                hideKeyboard();
                scrollListToBottom();
            }
        });

        inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(otherContato.getNome_completo());
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollListToBottom();
        if (dataUpdateReceiver == null)
            dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(SearchMessagesService.NEW_MESSAGE_TASK);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null)
            unregisterReceiver(dataUpdateReceiver);
    }

    private void scrollListToBottom(){
        lvMessages.post(new Runnable(){
            public void run() {
                lvMessages.setSelection(lvMessages.getCount() - 1);
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WebService.SUCESS:
                    Gson gson = new Gson();
                    Mensagem mensagem = gson.fromJson((String)msg.obj, Mensagem.class);
                    saveMensagem(mensagem);
                    setResult(RESULT_OK);
                    break;
                case WebService.ERROR:
                    break;
                default:
                    break;
            }
        }
    };

    public void saveMensagem(Mensagem mensagem){
        Mensagem mensagemAux = mensagemDao.queryBuilder()
                .where(MensagemDao.Properties.Id.eq(mensagem.getId()))
                .unique();
        if (mensagemAux==null)
            mensagemDao.insert(mensagem);
    }

    private void hideKeyboard(){
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SearchMessagesService.NEW_MESSAGE_TASK)) {
                if (intent.getLongExtra(SearchMessagesService.CONTATO_ORIGEM_PARAM, 0) != otherContato.getId())
                    return;
                mensagemAdapter.add((Mensagem) intent.getSerializableExtra(SearchMessagesService.MESSAGE_PARAM));
                mensagemAdapter.notifyDataSetChanged();
                scrollListToBottom();
            }
        }
    }

}
