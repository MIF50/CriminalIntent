package mif50.com.criminalintent;

import android.support.v4.app.Fragment;

import mif50.com.criminalintent.fragemnt.CrimeListFragment;
import mif50.com.criminalintent.fragemnt.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
