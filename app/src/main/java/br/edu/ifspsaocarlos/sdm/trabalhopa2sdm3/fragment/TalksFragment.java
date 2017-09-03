package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Note on 03/09/2017.
 */

public class TalksFragment extends Fragment implements TabFragment {

    public static String APP_STATUS_PARAM = "APP_STATUS_PARAM";

    private ListView listViewContatos;

    private ContatoAdapter adapter;

    public TalksFragment() {}

    public static TalksFragment newInstance(AppStatus appStatus) {
        TalksFragment fragment = new TalksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(APP_STATUS_PARAM, appStatus);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        listViewContatos = (ListView) rootView.findViewById(R.id.lvContacts);

        adapter = new ContatoAdapter(getActivity(), ((AppStatus) getArguments().getSerializable(APP_STATUS_PARAM)).getContatoList());
        listViewContatos.setAdapter(adapter);

        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TalkActivity.class);
                intent.putExtra(TalkActivity.OTHER_CONTATO_PARAM, adapter.getItem(i));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void addContato(Contato contato) {
    }

    @Override
    public void updateView(AppStatus appStatus) {
        adapter.addAll(appStatus.getContatoList());
        adapter.notifyDataSetChanged();
    }
}
