package online.zerone.xamscaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**THE FIRST ACTIVITY*/

public class SemActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<String> list = new ArrayList<>();
    GridView gv;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem);


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference myRef = database.getReference().child("semester");
        gv= (GridView) findViewById(R.id.gv1);
        pb= (ProgressBar) findViewById(R.id.progress1);


        pb.setVisibility(View.VISIBLE);

        /**SETTING LISTENER FOR DATA CHANGE IN DATABASE. WHENEVER DATA CHANGES, SCREENSHOT WILL BE TAKEN*/

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
