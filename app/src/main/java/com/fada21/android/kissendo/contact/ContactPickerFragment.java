package com.fada21.android.kissendo.contact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fada21.android.kissendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.fada21.android.kissendo.utils.Consts.PREF_NUMBER;

public class ContactPickerFragment extends Fragment {

    public static final String TAG_ContactPickerFragment = "TAG_ContactPickerFragment";
    private static final int CONTACT_PICKER_RESULT = 44;

    @BindView(R.id.edit_addressee_number) EditText phoneNumberInput;
    @BindView(R.id.btn_contact_picker) Button contactPickerBtn;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content_picker, container, false);
        ButterKnife.bind(this, view);

        contactPickerBtn.setOnClickListener(v -> pickContact());

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        phoneNumberInput.setText(prefs.getString(PREF_NUMBER, ""));
        phoneNumberInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                storeNumber(phoneNumberInput.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results
                    break;
            }

        } else {
            // gracefully handle failure
        }
    }

    private void pickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                                                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
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
