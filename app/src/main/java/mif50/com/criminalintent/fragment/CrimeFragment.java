package mif50.com.criminalintent.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import mif50.com.criminalintent.R;
import mif50.com.criminalintent.helper.GenericTextWatcher;
import mif50.com.criminalintent.model.Crime;
import mif50.com.criminalintent.model.CrimeLab;
import mif50.com.criminalintent.utils.PictureUtlis;


public class CrimeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_IMAGE = 2;


    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private Crime mCrime;
    private Button btn_date;
    private Button mReportBtn;
    private Button mSuspectBtn;
    private Button callBtn;
    private ImageButton cameraIb;
    private ImageView photoIv;

    private File photoFile;

    Uri contactUri;
    String[] queryFields;
    Cursor c;
    String suspectId;


    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //use arguments
        assert getArguments() != null;
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        mCrime = crimeLab.getCrime(crimeID);
        photoFile = crimeLab.getFilePhoto(mCrime);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crime_fragmrnt, container, false);
        EditText mTitle = v.findViewById(R.id.m_title);
        mTitle.setText(mCrime.getmTitle());
        mTitle.addTextChangedListener(new GenericTextWatcher(mTitle, mCrime));
        btn_date = v.findViewById(R.id.btn_data);

        updateDate();
        btn_date.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            DatePickerFragment dialogDate = DatePickerFragment.newInstance(mCrime.getmData());
            dialogDate.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            assert fm != null;
            dialogDate.show(fm, DIALOG_DATE);
        });
        CheckBox check_solved = v.findViewById(R.id.check_solved);
        check_solved.setChecked(mCrime.ismSolved());
        check_solved.setOnCheckedChangeListener(this);

        mReportBtn = v.findViewById(R.id.crime_report);
        mReportBtn.setOnClickListener(view -> {
//                sendTextContactShareCompat();
            sendTextContact();
        });

        // to open contact of mobile
        final Intent pickIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        pickIntent.addCategory(Intent.CATEGORY_HOME);  // dummy date to test no contact app in your device
        mSuspectBtn = v.findViewById(R.id.crime_suspect);
        mSuspectBtn.setOnClickListener(view -> startActivityForResult(pickIntent, REQUEST_CONTACT));
        if (mCrime.getmSuspect() != null)
            mSuspectBtn.setText(mCrime.getmSuspect());
        // to check if no app to contact then disable btn not prevent crash app when clicked
        PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        if (packageManager.resolveActivity(pickIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectBtn.setEnabled(false);
        }

        callBtn = v.findViewById(R.id.crime_call);
        callBtn.setOnClickListener(view -> {
            if (mCrime.getNumber() != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCrime.getNumber()));
                startActivity(intent);
            }

        });

        cameraIb = v.findViewById(R.id.crime_camera);
        photoIv = v.findViewById(R.id.crime_photo);

        // take image
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakeImage = photoFile != null && captureImage.resolveActivity(packageManager) != null;
        cameraIb.setEnabled(canTakeImage);
        if (canTakeImage) {
            Uri uri = Uri.fromFile(photoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        cameraIb.setOnClickListener(view -> {
            //
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            startActivityForResult(captureImage, REQUEST_IMAGE);
        });

        updatePhotoView();

        photoIv.setOnClickListener(view -> {
            if (photoFile == null || !photoFile.exists())
                return;
            ImageFragment fragment = ImageFragment.newInstance(photoFile.getPath());
            assert getFragmentManager() != null;
            fragment.show(getFragmentManager(),ImageFragment.TAG);
        });


        return v;
    }


    private void sendTextContactShareCompat() {
        final Activity activity = Objects.requireNonNull(getActivity());
        Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(getCrimeReport())
                .setChooserTitle(getString(R.string.send_report))
                .getIntent();
        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    private void sendTextContact() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
        i = Intent.createChooser(i, getString(R.string.send_report));
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.check_solved:
                mCrime.setmSolved(isChecked);
                break;
        }
    }

    public String changeFormatDate(Date date) {
        return DateFormat.getDateFormat(getContext()).format(date);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Objects.requireNonNull(getActivity()).finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmData(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT) {
            contactUri = data.getData();
            // specif which field you want your query to return value for.
            queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.LOOKUP_KEY,
            };
            // perform your query contactUri is like a where  clause here
            assert contactUri != null;

            c = Objects.requireNonNull(getActivity()).getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                // double check that you actualy get result
                assert c != null;
                // put put the first column of the first row of data that you suspect's name
                c.moveToFirst();
                String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                suspectId = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                mCrime.setmSuspect(suspect);
                mSuspectBtn.setEnabled(true);
                mSuspectBtn.setText(suspect);
            } finally {
                assert c != null;
                c.close();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else getNumberFormContact();
        } else if (requestCode == REQUEST_IMAGE) {
            updatePhotoView();
        }
    }

    private void getNumberFormContact() {
        Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryFields = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor c = Objects.requireNonNull(getActivity()).getContentResolver().
                query(contactUri, queryFields, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?", new String[]{suspectId}, null);
        try {
            assert c != null;
            c.moveToFirst();
            String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            mCrime.setNumber(number);

        } finally {
            assert c != null;
            c.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getNumberFormContact();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDate() {
        btn_date.setText(mCrime.getmData().toString());
    }

    private String getCrimeReport() {
        String solvedString;
        if (mCrime.ismSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getmData()).toString();

        String suspect = mCrime.getmSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect);
        }

        return getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedString, suspect);
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            photoIv.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtlis.getScaledBitmap(photoFile.getPath(), Objects.requireNonNull(getActivity()));
            photoIv.setImageBitmap(bitmap);
        }
    }
}
