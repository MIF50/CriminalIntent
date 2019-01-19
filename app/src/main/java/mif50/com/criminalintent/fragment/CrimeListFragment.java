package mif50.com.criminalintent.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import mif50.com.criminalintent.CrimePagerActivity;
import mif50.com.criminalintent.R;
import mif50.com.criminalintent.adapter.AdapterCrimeList;
import mif50.com.criminalintent.listener.ItemClickListener;
import mif50.com.criminalintent.model.Crime;
import mif50.com.criminalintent.model.CrimeLab;


/**
 * Created by mohamed on 2/9/17.
 */

public class CrimeListFragment extends Fragment implements ItemClickListener {


    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    Group groupPlaceHolder;
    AppCompatButton addNewCrimeBtn;
    List<Crime> crimes;
    private RecyclerView recyclerView;
    private AdapterCrimeList adapterCrimeList;
    private int mLastAdapterClickPosition = -1;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        View view = inflater.inflate(R.layout.crime_lab_fragment, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        groupPlaceHolder = view.findViewById(R.id.group_placeholder);
        addNewCrimeBtn = view.findViewById(R.id.add_new_crime);

        updateUI();
        setListener();

    }

    private void setListener() {
        addNewCrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCrime();
            }
        });
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        crimes = crimeLab.getCrimes();
        //Log.d("size",crimes.size()+"");
        if (adapterCrimeList == null) {
            adapterCrimeList = new AdapterCrimeList(crimes);
            adapterCrimeList.setItemClickListener(this);
            recyclerView.setAdapter(adapterCrimeList);

        } else {
            if (mLastAdapterClickPosition < 0) {
                adapterCrimeList.setCrimes(crimes);
                adapterCrimeList.notifyDataSetChanged();
            } else {
                adapterCrimeList.notifyItemChanged(mLastAdapterClickPosition);
                mLastAdapterClickPosition = -1;

            }

        }

        updateSubtitle();

    }

    public void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int countCrime = crimeLab.getCrimes().size();
        @SuppressLint("StringFormatMatches") String subtitle = getResources().getQuantityString(R.plurals.subtitle_plurals, countCrime, countCrime);

        if (!mSubtitleVisible) subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        Objects.requireNonNull(activity.getSupportActionBar()).setSubtitle(subtitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        updatePlaceHolder();
    }

    private void updatePlaceHolder() {
        assert adapterCrimeList != null;
        if (adapterCrimeList.getCrimes().size() > 0) groupPlaceHolder.setVisibility(View.GONE);
        else groupPlaceHolder.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleMenu = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible)
            subtitleMenu.setTitle(R.string.hide_subtitle);
        else
            subtitleMenu.setTitle(R.string.show_subtitle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                createNewCrime();
                return true;

            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void createNewCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        startActivity(CrimePagerActivity.newIntent(getContext(), crime.getmID()));
    }

    @Override
    public void onItemClicked(View view, int position, boolean isLongClick) {
        mLastAdapterClickPosition = position;

        // we used CrimePagerActivity Instead of Crime  Activity

        Intent intent = CrimePagerActivity.newIntent(getActivity(), crimes.get(position).getmID());
        startActivity(intent);
    }


}
