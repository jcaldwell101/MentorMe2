package ryan.jake.mentorme;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
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

public class search_results extends AppCompatActivity {


    public static final String TAG = ResultsActivity.class.getSimpleName();
    Handler mHandler;
    private JSONObject mainJSONObject;
    private JSONArray jsonArr;

    private ListView mNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        mHandler = new Handler(Looper.getMainLooper());

        mNames= (ListView)findViewById(R.id.searchResultList);

        Log.v(TAG, "searchcheck");

        getNames();



    }


    private void getNames(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/search")
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

                        //response here

                        String jsonData = response.body().string();
                        JSONObject jsonObj = null;

                        jsonObj = new JSONObject(jsonData);
                        jsonArr = jsonObj.getJSONArray("username");



                        response.close();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, "check");

                            }
                        });

                    }else{
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });

    }



    public void getResultString(){
        Intent intent = getIntent();
        String result = intent.getStringExtra("stringResult");


        //TextView text = (TextView) findViewById(R.id.testText2) ;
       // text.setText(result);
    }

}
