package mif50.com.criminalintent.fragment;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.Objects;

import mif50.com.criminalintent.R;
import mif50.com.criminalintent.utils.PictureUtlis;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends DialogFragment {
    public static final String TAG = "ImageFragment";
    private static final String ARG_PATH_IMAGE = "image_path";

    private ImageView showImage;
    private String path;


    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String path) {
        ImageFragment fragment = new ImageFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PATH_IMAGE, path);
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        path = getArguments().getString(ARG_PATH_IMAGE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showImage = view.findViewById(R.id.show_image);
        Bitmap bitmap = PictureUtlis.getScaledBitmap(path, Objects.requireNonNull(getActivity()));
        showImage.setImageBitmap(bitmap);

    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        assert window != null;
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.90), (int) (size.y * 0.90));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
