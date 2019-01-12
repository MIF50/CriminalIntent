package mif50.com.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import mif50.com.criminalintent.fragemnt.CrimeFragment;
import mif50.com.criminalintent.model.Crime;
import mif50.com.criminalintent.model.CrimeLab;


public class CrimePagerActivity extends AppCompatActivity {


    private static final String EXTRA_CRIME_ID = "crime_Id";
    UUID crimeID;
    private ViewPager mViewpager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }


    /*
     * this method to connect between the Fragment
     * two parameter context of the Activity and the data you want to send it
     * */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        if (getIntent() != null)
            crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);


        mViewpager = findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewpager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // this for loop to show the specific fragment when choice it not the default
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getmID().equals(crimeID)) {
                mViewpager.setCurrentItem(i);
                break;
            }
        }


    }
}
