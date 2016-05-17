package com.fada21.android.kissendo.contact;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fada21.android.kissendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fada21.android.kissendo.utils.Consts.PREF_NUMBER;

public class ContactPickerFragment extends Fragment {

    public static final String TAG_ContactPickerFragment = "TAG_ContactPickerFragment";

    @BindView(R.id.edit_addressee_number) EditText phoneNumberInput;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content_picker, container, false);
        ButterKnife.bind(this, view);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        phoneNumberInput.setText(prefs.getString(PREF_NUMBER, ""));
        phoneNumberInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                storeNumber(phoneNumberInput.getText().toString());
            }
        });
        return view;
    }

    private void storeNumber(final String number) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (TextUtils.isEmpty(number) || !PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            throw new IllegalStateException("Nope");
        } else {
            prefs.edit().putString(PREF_NUMBER, number).apply();
        }
    }

}
