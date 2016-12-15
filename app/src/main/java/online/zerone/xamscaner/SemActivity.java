package online.zerone.xamscaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static online.zerone.xamscaner.BranchActivity.dbref;

/**THE FIRST ACTIVITY*/

public class SemActivity extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<>();
    ListView gv;
    ProgressBar pb;
    String branch;

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        dbref= dbref.getParent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem);


        gv= (ListView) findViewById(R.id.gv1);
        pb= (ProgressBar) findViewById(R.id.progress1);

        pb.setVisibility(View.VISIBLE);

        Intent i = this.getIntent();
        branch = i.getExtras().getString("BRANCH");
        if(branch!=null)
            dbref= dbref.child(branch);

        /**SETTING LISTENER FOR DATA CHANGE IN DATABASE. WHENEVER DATA CHANGES, SCREENSHOT WILL BE TAKEN*/

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getdata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

/**GETTING DATA FROM THE SERVER WITH A SNAPSHOT*/

    private void getdata(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            list.add(ds.getKey());
        }
        pb.setVisibility(View.GONE);
        StringlistAdapter adapter = new StringlistAdapter(SemActivity.this,list);
        gv.setAdapter(adapter);
    }
}
