package ryan.jake.mentorme;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.*;
import android.content.Intent;
import java.lang.StringBuilder;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class SearchActivity extends AppCompatActivity {

    RadioButton diet, religion, housing, sports;
    Button buttonSearch;

    private boolean isMentee;
    private String mUser;

    private String mTopic;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        mUser = intent.getStringExtra("username");
        isMentee = intent.getBooleanExtra("usertype", true);


        diet = (RadioButton) findViewById(R.id.dietaryradioButton3);
        religion = (RadioButton) findViewById(R.id.religionradioButton);
        housing = (RadioButton) findViewById(R.id.housingradioButton);
        sports = (RadioButton) findViewById(R.id.sportsradioButton);
        buttonSearch = (Button) findViewById(R.id.mentorsearchbutton);


        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (diet.isChecked())
                    mTopic = "dietary";

                else if (religion.isChecked())
                    mTopic = "religion";

                else if (housing.isChecked())
                    mTopic = "housing";

                else
                    mTopic = "sports";


                Log.v("Hey", mTopic);

                goResults();
            }

        });


}


    public void goResults() {
        Intent intent = new Intent(this, search_results.class);
        intent.putExtra("username", mUser);
        intent.putExtra("usertype", isMentee);
        intent.putExtra("topic", mTopic);
        startActivity(intent);
    }


}

