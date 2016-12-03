package ryan.jake.mentorme;



import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CheckBox;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountEditActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mLocationView;
    JSONObject json = new JSONObject();
    private RadioButton mMaleButton;
    private RadioGroup mGenderGroup;
    private CheckBox mDietCheck;
    private CheckBox mReligionCheck;
    private CheckBox mHousingCheck;
    private CheckBox mSportsCheck;
    private String mGender;
    private String mUsername;

    private String mOldName;
    private String mOldPassword;
    private String mOldLocation;

    private View mEditView;
    private View mProgressView;

    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        mNameView = (EditText) findViewById(R.id.edit_name_box);
        mPasswordView = (EditText) findViewById(R.id.edit_password_box);
        mLocationView = (EditText) findViewById(R.id.edit_location_box);
        mDietCheck=(CheckBox)findViewById(R.id.checkBox);
        mReligionCheck=(CheckBox)findViewById(R.id.checkBox3);
        mSportsCheck=(CheckBox)findViewById(R.id.checkBox4);
        mHousingCheck=(CheckBox)findViewById(R.id.checkBox2);
        mMaleButton=(RadioButton)findViewById(R.id.editmaleradioButton);

        mHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");



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

        if(mMaleButton.isChecked()){
           mGender = "1";
        }
        else{
           mGender="0";
        }

        try {

            json.put("name", mNameView.getText().toString());
            json.put("username",mUsername);
            json.put("password",mPasswordView.getText().toString());
            json.put("city",mLocationView.getText().toString());
            json.put("dietary",mDietCheck.isChecked());
            json.put("religion",mReligionCheck.isChecked());
            json.put("sports",mSportsCheck.isChecked());
            json.put("housing",mHousingCheck.isChecked());
            json.put("gender", mGender);





        } catch (JSONException e) {
            e.printStackTrace();
        }

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

           uploadData(json.toString());

        }
    }

    private void goToLanding() {
        //TODO go to landing
        Intent intent = new Intent(this,Main2Activity .class);
        intent.putExtra("username",mUsername);
        startActivity(intent);
    }

    private boolean isPasswordValid(String string) {
        return (string.length() > 5 && string.length() < 17);
    }

    // True if stuff uploads, false if something bad happens
    private boolean uploadData(String json) {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/edit")
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Fail", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()){

                        Log.v(TAG, response.body().string());
                        response.close();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                goToLanding();
                            }
                        });

                    }
                    else{
                        Log.e(TAG,"bad");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });


                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                }
            }
        });


        return true;
    }
}

