package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Note on 03/09/2017.
 */

public class ContactsFragment extends Fragment implements TabFragment {

    private FragmentActivityListener mCallback;

    public static int SHOW_TALK = 1;

    public static String APP_STATUS_PARAM = "APP_STATUS_PARAM";

    private ListView listViewContatos;

    private ContatoAdapter adapter;

    public ContactsFragment(){}

    public static ContactsFragment newInstance(AppStatus appStatus) {
        ContactsFragment fragment = new ContactsFragment();
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
                startActivityForResult(intent, SHOW_TALK);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void addContato(Contato contato) {
        adapter.add(contato);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateView(AppStatus appStatus) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_TALK && resultCode == RESULT_OK)
            mCallback.updateTalks();
    }
}
