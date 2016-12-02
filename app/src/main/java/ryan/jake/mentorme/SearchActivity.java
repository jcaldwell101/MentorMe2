package ryan.jake.mentorme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.*;
import android.content.Intent;
import java.lang.StringBuilder;
import android.widget.CheckBox;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    CheckBox diet, religion, housing, sports;
    Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        addListenerOnButtonClick();

        //start new activity on button press
        Button buttonSearch = (Button) findViewById(R.id.buttonS);
        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, search_results.class));
            }
        });

    }

    //send result string
    public void onSearchButtonClick(String result) {
        Intent intent = new Intent(SearchActivity.this, search_results.class);
        intent.putExtra("stringResult", result);
        startActivity(intent);
    }

    public void addListenerOnButtonClick() {
        //instances for checkboxes and button
        diet = (CheckBox) findViewById(R.id.checkBoxDiet);
        religion = (CheckBox) findViewById(R.id.checkBoxReligion);
        housing = (CheckBox) findViewById(R.id.checkBoxHousing);
        sports = (CheckBox) findViewById(R.id.checkBoxSports);
        buttonSearch = (Button) findViewById(R.id.buttonS);

        //Listener on Button click
        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder result = new StringBuilder();

                if (diet.isChecked())
                    result.append("Diet");

                if (religion.isChecked())
                    result.append("Religion");

                if (housing.isChecked())
                    result.append("Housing");

                if (sports.isChecked())
                    result.append("Sports");

                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();

            }

        });

    }
}

