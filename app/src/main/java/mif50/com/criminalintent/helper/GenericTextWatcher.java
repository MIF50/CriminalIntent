package mif50.com.criminalintent.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import mif50.com.criminalintent.R;
import mif50.com.criminalintent.model.Crime;


public class GenericTextWatcher implements TextWatcher {

    private View view;
    private Crime mCrime;

    public GenericTextWatcher(View view, Crime mCrime) {
        this.view = view;
        this.mCrime = mCrime;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int id = view.getId();
        switch (id) {
            case R.id.m_title:
                mCrime.setmTitle(charSequence.toString());
                break;
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}

