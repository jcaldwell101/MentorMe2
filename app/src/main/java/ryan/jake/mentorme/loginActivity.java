package ryan.jake.mentorme;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity {

    public static final String TAG = loginActivity.class.getSimpleName();

    private EditText mName;
    private EditText mPassword;

    private Button mLogin;
    private Button mCreate;
    private Button mResult;
    private TextView mError;

    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_login);


        mName = (EditText)findViewById(R.id.usernameLogin);
        mPassword = (EditText)findViewById(R.id.password);

        mLogin  = (Button)findViewById(R.id.Login);
        mCreate = (Button)findViewById(R.id.cAccount);
        mResult   = (Button)findViewById(R.id.result);

        mError = (TextView)findViewById(R.id.error2) ;
        mHandler = new Handler(Looper.getMainLooper());

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountCreate();
            }
        });



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequest(mName.getText().toString(),mPassword.getText().toString());
            }
        });
        mResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChat();
            }
        });

    }

    private void goToChat() {
        Intent intent = new Intent(this, search_results.class);
        //pass requestId since it's a MUST!
        intent.putExtra("requestid","1");
        intent.putExtra("userid","2");
        startActivity(intent);
    }
    private void accountCreate() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void acceptLogin(String username) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    private void getRequest(String name, String password){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-54-218-89-13.us-west-2.compute.amazonaws.com/login?name="+name+"&password="+password)
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
                        response.close();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG,mName.getText().toString() );
                                acceptLogin(mName.getText().toString());
                            }
                        });

                    }else{
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mError.setVisibility(View.VISIBLE);
                            }
                        });

                    }

            }
        });

    }
}
