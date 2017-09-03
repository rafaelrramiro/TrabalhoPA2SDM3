package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.R;
import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.fragment.ContactsFragment;
import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.fragment.TalksFragment;
import br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model.AppStatus;

/**
 * Created by Note on 03/09/2017.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private AppStatus appStatusTalks;

    private AppStatus appStatusContacts;

    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    public TabsAdapter(Context context, FragmentManager fragmentManager,
                       AppStatus appStatusTalks, AppStatus appStatusContacts) {
        super(fragmentManager);
        this.context = context;
        this.appStatusTalks = appStatusTalks;
        this.appStatusContacts = appStatusContacts;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TalksFragment.newInstance(appStatusTalks);
            case 1:
                return ContactsFragment.newInstance(appStatusContacts);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tittle_talks_fragment);
            case 1:
                return context.getString(R.string.tittle_contacts_fragment);
            default:
                return "";
        }
    }
}