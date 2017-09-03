package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by Note on 03/09/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editTextNomeCompleto;

    private Button buttonContinuar;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNomeCompleto = (EditText) findViewById(R.id.etNomeCompleto);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonContinuar = (Button) findViewById(R.id.btnContinuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextNomeCompleto.getText().toString().trim().length()<=0){
                    Snackbar.make(view, getString(R.string.msg_nome_incorreto), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                buttonContinuar.setEnabled(false);

                Contato contato = new Contato();
                contato.setNome_completo(editTextNomeCompleto.getText().toString());
                contato.setApelido(Utils.getAppIdBase64(contato.getNome_completo()));
                WebService.cadastraContato(contato, LoginActivity.this, handler);
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);
            switch (msg.what) {
                case WebService.SUCESS:
                    SharedPreferences.Editor editor = Utils.getPreferences(LoginActivity.this).edit();
                    editor.putString(Utils.SP_CURRENT_USER, (String)msg.obj);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case WebService.ERROR:
                    Snackbar.make(buttonContinuar, getString(R.string.msg_login_error), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    buttonContinuar.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

}

