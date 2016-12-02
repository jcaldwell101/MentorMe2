package ryan.jake.mentorme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register2Activity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static final String TAG = RegisterActivity.class.getSimpleName();

    Handler mHandler;

    private String mName;
    private String mPassword;
    private String mUsername;
    private String mCity;
    private int mGender;

    private CheckBox mDietary;
    private CheckBox mHousing;
    private CheckBox mReligion;
    private CheckBox mSports;
    private TextView mError;

    private SeekBar mIntro;
    private SeekBar mRation;

    private Boolean mMentee;
    private Boolean mMentor;

    private Button mRegister;
    private Button mPicture;

    private ImageView mPreviewPic;
    private String mPicString;

    private String mPicPath;

    private JSONObject json = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);


        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        mName = intent.getStringExtra("name");
        mCity = intent.getStringExtra("city");
        mPassword = intent.getStringExtra("password");

        mMentee = intent.getBooleanExtra("mentee",true);
        mMentor = intent.getBooleanExtra("mentor",true);
        mGender= intent.getIntExtra("gender",0);


        mRation= (SeekBar)findViewById(R.id.Rational);
        mIntro = (SeekBar)findViewById(R.id.Introversion);

        mReligion= (CheckBox)findViewById(R.id.checkBoxReligion);
        mHousing= (CheckBox)findViewById(R.id.checkBoxHousing);
        mSports= (CheckBox)findViewById(R.id.checkBoxSports);
        mDietary= (CheckBox)findViewById(R.id.checkBoxDietary);


        mRegister = (Button)findViewById(R.id.registerButton);
        mPicture = (Button)findViewById(R.id.pictureButton);

        mError = (TextView)findViewById(R.id.error);

        mPreviewPic = (ImageView)findViewById(R.id.previewPic);

        mHandler = new Handler(Looper.getMainLooper());


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    json.put("name",mName);
                    json.put("username",mUsername);
                    json.put("password",mPassword);
                    json.put("city",mCity);
                    json.put("dietary",mDietary.isChecked());
                    json.put("religion",mReligion.isChecked());
                    json.put("sports",mSports.isChecked());
                    json.put("housing",mHousing.isChecked());
                    json.put("mentee",mMentee);
                    json.put("mentor",mMentor);
                    json.put("picture", mPicString);
                    json.put("gender", mGender);
                    json.put("intro",mIntro.getProgress());
                    json.put("ration",mRation.getProgress());

                    try{Log.v(TAG,Integer.toString(mPicString.length()));} catch (Exception e) {}




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                postRequest(json.toString());
            }
        });

        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);

                File picture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                mPicPath = picture.getPath();

                Log.v(TAG, mPicPath);
                Uri data = Uri.parse(mPicPath);

                Log.v(TAG, data.toString());

                photoPicker.setDataAndType(data, "image/*");

                startActivityForResult(photoPicker,20);


            }
        });


    }


    private void acceptLogin(String username) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("username",username);
        startActivity(intent);
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
                        response.close();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                acceptLogin(mUsername);
                            }
                        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 20){
                Uri image = data.getData();

                mPicPath = image.toString().substring(9);
                Log.v(TAG, mPicPath);

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(image);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    mPreviewPic.setImageBitmap(bitmap);
                    mPicString=BitMapToString(bitmap);

                } catch (FileNotFoundException e) {

                    e.printStackTrace();

                }
            }
        }
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }

}
