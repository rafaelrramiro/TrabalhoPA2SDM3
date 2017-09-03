package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.R;
import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model.Contato;
import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model.Mensagem;
/**
 * Created by Note on 03/09/2017.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private LayoutInflater layoutInflater;

    private Contato contatoAtual;

    public MensagemAdapter(Activity activity, List<Mensagem> mensagemList, Contato contatoAtual) {
        super(activity, R.layout.message_item, mensagemList);
        this.contatoAtual = contatoAtual;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.corpo = (TextView) convertView.findViewById(R.id.tvMensagemCorpo);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Mensagem mensagem = getItem(position);
        holder.corpo.setText(mensagem.getCorpo());

        if (mensagem.getOrigem_id().equals(contatoAtual.getId())) {
            holder.corpo.setGravity(Gravity.LEFT);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.chatMy));
        }
        else{
            holder.corpo.setGravity(Gravity.RIGHT);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.chatYours));
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView corpo;
    }

}