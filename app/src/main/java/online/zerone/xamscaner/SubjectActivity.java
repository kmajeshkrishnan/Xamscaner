package online.zerone.xamscaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static online.zerone.xamscaner.BranchActivity.dbref;

/**THE SECOND ACTIVITY : SIMILAR TO FIRST ACTIVITY*/

public class SubjectActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar pb;
    ListView gv;
    ArrayList<String> list = new ArrayList<>();
    String sem;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       dbref=dbref.getParent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Intent i = this.getIntent();
        sem = i.getExtras().getString("SEM");
        if(sem!=null)
        dbref=dbref.child(sem);
     //   DatabaseReference myRef = database.getReference().child("semester/"+sem);
        pb= (ProgressBar) findViewById(R.id.progress2);
        gv= (ListView) findViewById(R.id.gv2);
        pb.setVisibility(View.VISIBLE);
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

