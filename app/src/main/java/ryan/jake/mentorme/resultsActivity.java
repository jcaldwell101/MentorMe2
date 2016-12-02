package ryan.jake.mentorme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {


    ArrayList<String> animalsNameList;
    private ListView mAnimals;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);





    }

    void getAnimalNames()
    {
        animalsNameList.add("DOG");
        animalsNameList.add("CAT");
        animalsNameList.add("HORSE");
        animalsNameList.add("ELEPHANT");
        animalsNameList.add("LION");
        animalsNameList.add("COW");
        animalsNameList.add("MONKEY");
        animalsNameList.add("DEER");
        animalsNameList.add("RABBIT");
        animalsNameList.add("BEER");
        animalsNameList.add("DONKEY");
        animalsNameList.add("LAMB");
        animalsNameList.add("GOAT");


    }
}
