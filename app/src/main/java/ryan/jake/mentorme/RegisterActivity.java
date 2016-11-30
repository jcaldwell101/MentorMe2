package ryan.jake.mentorme;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class RegisterActivity extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    public static final String TAG = RegisterActivity.class.getSimpleName();

    Handler mHandler;

    private EditText mName;
    private EditText mPassword;
    private EditText mUsername;
    private EditText mCity;

    private CheckBox mDietary;
    private CheckBox mHousing;
    private CheckBox mReligion;
    private CheckBox mSports;
    private TextView mError;

    private RadioButton mMentee;
    private RadioButton mMentor;

    private Button mRegister;
    private JSONObject json = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = (EditText)findViewById(R.id.nameText);
        mPassword = (EditText)findViewById(R.id.passwordText);
        mUsername = (EditText)findViewById(R.id.usernameText);
        mCity = (EditText)findViewById(R.id.cityText);

        mReligion= (CheckBox)findViewById(R.id.checkBoxReligion);
        mHousing= (CheckBox)findViewById(R.id.checkBoxHousing);
        mSports= (CheckBox)findViewById(R.id.checkBoxSports);
        mDietary= (CheckBox)findViewById(R.id.checkBoxDietary);

        mMentee= (RadioButton)findViewById(R.id.mentee);
        mMentor= (RadioButton)findViewById(R.id.mentor);

        mRegister = (Button)findViewById(R.id.registerButton);
        mError = (TextView)findViewById(R.id.error);

        mHandler = new Handler(Looper.getMainLooper());


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Find me");

                try {

                    json.put("name",mName.getText().toString());
                    json.put("username",mUsername.getText().toString());
                    json.put("password",mPassword.getText().toString());
                    json.put("city",mCity.getText().toString());
                    json.put("dietary",mDietary.isChecked());
                    json.put("religion",mReligion.isChecked());
                    json.put("sports",mSports.isChecked());
                    json.put("housing",mHousing.isChecked());
                    json.put("mentee",mMentee.isChecked());
                    json.put("mentor",mMentor.isChecked());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                postRequest(json.toString());
            }
        });
    }
    private void getRequest(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com")
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

                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                }
            }
        });

    }

    private void postRequest(String json){

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/register")
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


                    }
                    else{
                        Log.e(TAG,"bad");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mError.setVisibility(View.VISIBLE);
                            }
                        });


                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                }
            }
        });

    }


}
