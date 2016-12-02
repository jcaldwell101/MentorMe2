package ryan.jake.mentorme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

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

    private RadioButton mMale;


    private Button mRegister;
    private Button mPicture;

    private ImageView mPreviewPic;
    private String mPicString;

    private String mPicPath;
    private int mGender;


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
        mMale = (RadioButton)findViewById(R.id.maleradioButton);


        mRegister = (Button)findViewById(R.id.nextButton);
        mPicture = (Button)findViewById(R.id.pictureButton);

        mError = (TextView)findViewById(R.id.error);

        mPreviewPic = (ImageView)findViewById(R.id.previewPic);

        mHandler = new Handler(Looper.getMainLooper());


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getRequest();
            }
        });



    }

    private boolean validName(String name) {
        return name.length() > 1 && name.length() < 25;
    }



    private void next(String name, String username, String password, String city, Boolean mentor, Boolean mentee) {
        Intent intent = new Intent(this, Register2Activity.class);
        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("city", city);
        intent.putExtra("mentor", mentor);
        intent.putExtra("mentee", mentee);

        if(mMale.isChecked()){
            intent.putExtra("gender",1);
        }
        else{
            intent.putExtra("gender",0);
        }

        startActivity(intent);

    }


    private void getRequest() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/checkusername?name=" + mUsername.getText().toString())
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
                    if (response.isSuccessful()) {
                        Log.v(TAG, response.body().string());

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, mName.getText().toString());
                                next(mName.getText().toString(), mUsername.getText().toString(), mPassword.getText().toString(), mCity.getText().toString(), mMentor.isChecked(), mMentee.isChecked());
                            }
                        });

                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mError.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception Caught", e);
                }
            }
        });

    }

}
