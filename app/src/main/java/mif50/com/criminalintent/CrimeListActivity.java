package mif50.com.criminalintent;

import android.support.v4.app.Fragment;

import mif50.com.criminalintent.fragment.CrimeListFragment;
import mif50.com.criminalintent.fragment.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
