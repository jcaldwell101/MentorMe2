package ryan.jake.mentorme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class search_results extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String TAG = ResultsActivity.class.getSimpleName();
    Handler mHandler;
    private JSONObject mainJSONObject;
    private JSONArray jsonArr;
    private String mUser;
    private Boolean isMentee;
    private String mTopic;
    private String mOUsers;
    private String mRequest;

    private ListView mNames;
    List<String> listContents;
    final Context context = this;
     JSONObject json = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        mUser  = intent.getStringExtra("username");
        isMentee = intent.getBooleanExtra("usertype",true);
        mTopic = intent.getStringExtra("topic");


        mHandler = new Handler(Looper.getMainLooper());

        mNames= (ListView)findViewById(R.id.listSearchResults);



        Log.v(TAG, "searchcheck");

        if (isMentee){
            mTopic = intent.getStringExtra("topic");

            getNames();
        }
        else {
            getRequests();

        }



        mNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               mOUsers = listContents.get(position);



                if(!isMentee){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);


                // set title
                alertDialogBuilder.setTitle("Request Approval");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to approve!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                getRequestId();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);


                    // set title
                    alertDialogBuilder.setTitle("Request");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Are you sure you want to request "+mOUsers+"?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //goToChat(true);


                                    try {

                                        json.put("mentorid",mOUsers);
                                        json.put("menteeid",mUser);



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    createRequest(json.toString());



                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            }
        });



    }

    private void goToChat(Boolean isMentee){
        if(isMentee){
            Intent intent = new Intent(this, ChatActivity.class);
            //pass requestId since it's a MUST!
            intent.putExtra("requestid",mRequest);
            intent.putExtra("userid",mUser);
            intent.putExtra("recipientid", mOUsers);
            intent.putExtra("usertype", "2");
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(this, ChatActivity.class);
            //pass requestId since it's a MUST!
            intent.putExtra("requestid",mRequest);
            intent.putExtra("userid",mUser);
            intent.putExtra("recipientid", mOUsers);
            intent.putExtra("usertype", "1");
            startActivity(intent);
        }
    }



    private void createRequest(String json){


        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        final Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/request")
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
                if (response.isSuccessful()){

                    mRequest= response.body().string();
                    response.close();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            goToChat(true);
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
            }
        });


    }

    private void getRequests(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/getrequest?name="+mUser)
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

                        Log.v(TAG, jsonArr.toString());



                        response.close();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                listContents = new ArrayList<String>(jsonArr.length());

                                for (int i = 0; i < jsonArr.length(); i++) {
                                    try {
                                        listContents.add(jsonArr.getJSONObject(i).get("MenteeId").toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ArrayAdapter adapter = new  ArrayAdapter<String>(search_results.this, android.R.layout.simple_list_item_1, listContents);
                                mNames.setAdapter(adapter);


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

    private void getNames(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/search?name="+mUser+"&topic="+mTopic)
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

                        Log.v(TAG, jsonArr.toString());



                        response.close();

                         mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, "check");
                                listContents = new ArrayList<String>(jsonArr.length());

                                for (int i = 0; i < jsonArr.length(); i++) {
                                    try {
                                        listContents.add(jsonArr.getJSONObject(i).get("UserId").toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ArrayAdapter adapter = new  ArrayAdapter<String>(search_results.this, android.R.layout.simple_list_item_1, listContents);
                                mNames.setAdapter(adapter);


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

    private void getRequestId(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/getrequestid?name="+mUser+"&other="+mOUsers)
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

                        mRequest=response.body().string();


                        response.close();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                        goToChat(false);

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
