package mechanic.com.mechanic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class settings extends AppCompatActivity {

    private TextView aboutTextView;
    private ListView hiddenListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        aboutTextView = (TextView) findViewById(R.id.about);
        setSupportActionBar(toolbar);

        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* DataShow<String> stringList  = new ArrayList<String>();
                stringList
                ArrayAdapter arrayAdapter = new ArrayAdapter(settings.class,stringList);*/
            }
        });
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
