package ryan.jake.mentorme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class search_results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);



    }


    public void getResultString(){
        Intent intent = getIntent();
        String result = intent.getStringExtra("stringResult");


        //TextView text = (TextView) findViewById(R.id.testText2) ;
       // text.setText(result);
    }

}
