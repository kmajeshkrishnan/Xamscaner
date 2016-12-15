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

import static online.zerone.xamscaner.BranchActivity.dbref;

public class PdfListActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar pb;
    ListView gv;
    ArrayList<String> list = new ArrayList<>();
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        dbref=dbref.getParent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        Intent i = this.getIntent();
        String subject = i.getExtras().getString("DATA");
        if(subject!=null)
            dbref=dbref.child(subject);
      //  DatabaseReference myRef = database.getReference().child("semester/"+sem);
        pb= (ProgressBar) findViewById(R.id.progress3);
        gv= (ListView) findViewById(R.id.gv3);
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
        StringlistAdapter adapter = new StringlistAdapter(PdfListActivity.this,list);
        gv.setAdapter(adapter);
    }

}
