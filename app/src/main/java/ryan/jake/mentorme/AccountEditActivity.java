package ryan.jake.mentorme;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CheckBox;
import android.view.View.OnClickListener;

public class AccountEditActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mLocationView;
    private RadioButton mMaleButton;
    private RadioButton mFemaleButton;
    private RadioGroup mGenderGroup;
    private CheckBox mDietCheck;
    private CheckBox mReligionCheck;
    private CheckBox mHousingCheck;
    private CheckBox mSportsCheck;

    private String mOldName;
    private String mOldPassword;
    private String mOldLocation;

    private View mEditView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        mNameView = (EditText) findViewById(R.id.edit_name_box);
        mPasswordView = (EditText) findViewById(R.id.edit_password_box);
        mLocationView = (EditText) findViewById(R.id.edit_location_box);

        final Button mChangeButton = (Button) findViewById(R.id.save_button);
        mChangeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptProfileChange();
            }
        });

    }

    private void attemptProfileChange () {

        mNameView.setError(null);
        mPasswordView.setError(null);
        mLocationView.setError(null);

        String newName = mNameView.getText().toString();
        String newPassword = mPasswordView.getText().toString();
        String newLocation = mLocationView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(newName)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(newPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(newLocation)) {
            mLocationView.setError(getString(R.string.error_field_required));
            focusView = mLocationView;
            cancel = true;
        } else if (!isPasswordValid(newPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if(uploadData()) {
                goToLanding();
            } else {
                mNameView.setError("There was a server error");
                mNameView.requestFocus();
            }
        }
    }

    private void goToLanding() {
        //TODO go to landing
        Intent intent = new Intent(this,Main2Activity .class);
        startActivity(intent);
    }

    private boolean isPasswordValid(String string) {
        return (string.length() > 5 && string.length() < 17);
    }

    // True if stuff uploads, false if something bad happens
    private boolean uploadData() {
        //Todo send stuff to webservice
        return true;
    }
}

