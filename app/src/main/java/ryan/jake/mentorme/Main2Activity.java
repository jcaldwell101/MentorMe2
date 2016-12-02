package ryan.jake.mentorme;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.bitmap;

public class Main2Activity extends AppCompatActivity {

    String mUser;
    private Handler mHandler;
    public static final String TAG = Main2Activity.class.getSimpleName();
    Bitmap mbitmap;



    private ImageView mProfilePic;
    private TextView mProfileName;
    private Button mSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        mUser  = intent.getStringExtra("username");
        mSearch = (Button)findViewById(R.id.searchbutton);

        if (mUser.equalsIgnoreCase("Mentee2")){
            mSearch.setText("Search");

        }

        mHandler = new Handler(Looper.getMainLooper());

        mProfilePic = (ImageView)findViewById(R.id.profilePicture);
        mProfileName = (TextView)findViewById(R.id.profileNameText);
        mProfileName.setText(mUser);



        getImage();


        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSearch();
            }
        });

    }

    private void goSearch(){
        Intent intent = new Intent(this, search_results.class);
        intent.putExtra("username", mUser);
        startActivity(intent);


    }


    private void getProfile(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/profile?name="+mUser)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Fail", e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (response.isSuccessful()){
                        //Log.v(TAG, Integer.toString( response.body().string().length()));

                        String respon = response.body().string();

                        mbitmap = StringToBitMap(respon);
                        //mProfilePic.setImageBitmap(mbitmap);

                        Log.v(TAG,"check1");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG,"check2");
                                mProfilePic.setImageBitmap(mbitmap);
                            }
                        });

                    }else{


                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                }
            }
        });
    }



    private void getImage(){


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/image?name="+mUser)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Fail", e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (response.isSuccessful()){
                        //Log.v(TAG, Integer.toString( response.body().string().length()));

                        String respon = response.body().string();

                        mbitmap = StringToBitMap(respon);
                        //mProfilePic.setImageBitmap(mbitmap);

                        Log.v(TAG,"check1");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG,"check2");
                                 mProfilePic.setImageBitmap(mbitmap);
                            }
                        });

                    }else{


                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                }
            }
        });



    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }



}
