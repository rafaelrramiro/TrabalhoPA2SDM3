package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.R;
import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model.Contato;

/**
 * Created by Note on 03/09/2017.
 */

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private LayoutInflater layoutInflater;

    public ContatoAdapter(Activity activity, List<Contato> contatoList) {
        super(activity, R.layout.contact_item, contatoList);
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.contact_item, null);
            holder = new ViewHolder();
            holder.contatoName = (TextView) convertView.findViewById(R.id.tvContatoName);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Contato contato = getItem(position);
        holder.contatoName.setText(contato.getNome_completo());
        return convertView;
    }

    static class ViewHolder {
        public TextView contatoName;
    }

}