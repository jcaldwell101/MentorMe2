package ryan.jake.mentorme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {


    String mUser;
    String mOUser;
    private Handler mHandler;
    public static final String TAG = Main2Activity.class.getSimpleName();
    Bitmap mbitmap;
    private JSONArray jsonArr;

    private ImageView mOProfilePic;
    private TextView mOProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        mUser  = intent.getStringExtra("username");
        mOUser = intent.getStringExtra("ousername");

        mOProfilePic = (ImageView)findViewById(R.id.oProfilePicture);
        mOProfileName = (TextView)findViewById(R.id.oProfileNameText);
        mOProfileName.setText(mOUser);

        getProfile();


    }

    private void getProfile(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/getprofile?name="+mOUser)
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
                        response.close();
                        JSONObject jsonObj = null;

                        jsonObj = new JSONObject(respon);
                        jsonArr = jsonObj.getJSONArray("username");

                        mbitmap = StringToBitMap(respon);
                        //mProfilePic.setImageBitmap(mbitmap);

                        Log.v(TAG,"check1");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG,jsonArr.toString());
                               // mOProfilePic.setImageBitmap();

                            }
                        });

                    }else{
                        Log.v(TAG, "error");

                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
