package mif50.com.criminalintent;

import android.support.v4.app.Fragment;

import mif50.com.criminalintent.fragment.CrimeFragment;
import mif50.com.criminalintent.fragment.CrimeListFragment;
import mif50.com.criminalintent.fragment.SingleFragmentActivity;
import mif50.com.criminalintent.model.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.CallBacks, CrimeFragment.CallBacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.details_fragment_container) == null) {
            startActivity(CrimePagerActivity.newIntent(this, crime.getmID()));
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment_container, CrimeFragment.newInstance(crime.getmID()))
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
