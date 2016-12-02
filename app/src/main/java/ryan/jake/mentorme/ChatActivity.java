package ryan.jake.mentorme;

        import android.app.Activity;
        import android.bluetooth.BluetoothClass;
        import android.content.Context;
        import android.database.DataSetObserver;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Looper;
        import android.support.design.widget.FloatingActionButton;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.AbsListView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.appindexing.Action;
        import com.google.android.gms.appindexing.AppIndex;
        import com.google.android.gms.appindexing.Thing;
        import com.google.android.gms.common.api.GoogleApiClient;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.ArrayList;

        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;


public class ChatActivity extends Activity {
    private static final String TAG = "ChatActivity";

    public String requestid    = "";
    public String userid       = "";
    public String receipientid = "";
    public String usertype     = "";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private FloatingActionButton buttonSend;
    private boolean alignRight = true;
    Handler mHandler;
    private String mMessage;
    private JSONObject mainJSONObject;
    private JSONArray jsonArr;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        FloatingActionButton buttonSend = (FloatingActionButton) findViewById(R.id.fab);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            //values that must be passed to activity from intent from request screen
            requestid    = extras.getString("requestid");
            userid       = extras.getString("userid");
            receipientid = extras.getString("recipientid");
            usertype     = extras.getString("usertype");
        }

        //populate chat


        /*//Toast start
        Context context = getApplicationContext();
        CharSequence text = "requestid: " + requestid + "userid: " + userid;
        int dur = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,dur);
        toast.show();
        //Toast stop*/
        mHandler = new Handler(Looper.getMainLooper());
        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        getChat();
    }

    private boolean sendChatMessage() {
        if(!chatText.getText().toString().matches("")) {
            chatArrayAdapter.add(new ChatMessage(alignRight, chatText.getText().toString()));
            chatText.setText("");
            return true;
        }
        //side = !side;



        return false;
    }

    private void sendMsgCallback(Boolean boolRight, String msg) {
        chatArrayAdapter.add(new ChatMessage(boolRight, msg));
    }

    private void getChat(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/getChat?requestid="+requestid.toString())
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
                        //Log.v(TAG, response.body().string());
                        String jsonData = response.body().string();
                        try {
                            //clear msgs
                            if(chatArrayAdapter.getCount()>1) {
                                chatArrayAdapter.clear();
                                listView.deferNotifyDataSetChanged();
                            }
                            JSONObject jsonObj = new JSONObject(jsonData);
                            jsonArr = jsonObj.getJSONArray("messages");

                            //for(int i= 0;i<jsonArr.length();i++) {
                                //JSONObject object = jsonArr.getJSONObject(i);
                                //Log.v("get string : ",object.getString("MentorId").toString());
                                //mMessage=object.getString("Message").toString();
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run()  {
                                            for(int i=0;i<jsonArr.length();i++) {
                                                try {
                                                    mainJSONObject = jsonArr.getJSONObject(i);
                                                    if(mainJSONObject.getString("SenderId").toString().matches(userid)) {
                                                        sendMsgCallback(alignRight,mainJSONObject.getString("Message"));
                                                    } else {
                                                        sendMsgCallback(false,mainJSONObject.getString("Message"));
                                                    }
                                                    } catch (JSONException e) {e.printStackTrace();}

                                            }
                                        }
                                    });


                            //}
                            //Log.v("JSON DATA: ", jsonArr.toString());
                        } catch (JSONException e) {
                            Log.v(TAG, e.toString());
                        }
                    }else{ }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Caught",e);
                }
            }
        });


    }

/*    private void postRequest(String json){

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

    }*/

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Chat Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}







/*import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
*/