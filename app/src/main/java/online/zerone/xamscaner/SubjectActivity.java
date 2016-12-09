package online.zerone.xamscaner;

import android.content.Intent;
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
import java.util.List;

/**THE SECOND ACTIVITY : SIMILAR TO FIRST ACTIVITY*/

public class SubjectActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar pb;
    GridView gv;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Intent i = this.getIntent();
        String sem = i.getExtras().getString("DATA");
        DatabaseReference myRef = database.getReference().child("semester/"+sem);
        pb= (ProgressBar) findViewById(R.id.progress2);
        gv= (GridView) findViewById(R.id.gv2);
        pb.setVisibility(View.VISIBLE);
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

    private void getdata(DataSnapshot dataSnapshot) {
        list.clear();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            list.add(ds.getKey());
        }
        pb.setVisibility(View.GONE);
        StringlistAdapter adapter = new StringlistAdapter(SubjectActivity.this,list);
        gv.setAdapter(adapter);
    }

    }

