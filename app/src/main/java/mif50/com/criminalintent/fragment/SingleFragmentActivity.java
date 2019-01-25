package mif50.com.criminalintent.fragment;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import mif50.com.criminalintent.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {


    private Fragment fragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutRes(){
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment, "fragment");
            fragmentTransaction.commit();
        }
    }
}
