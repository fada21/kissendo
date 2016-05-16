package com.fada21.android.kissendo.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fada21.android.kissendo.R;

public class ContactPickerFragment extends Fragment {

    public static final String TAG_ContactPickerFragment = "TAG_ContactPickerFragment";

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content_picker, container, false);
        return view;
    }

    // STOPSHIP: 16/05/16  
}
