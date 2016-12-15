package online.zerone.xamscaner;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static online.zerone.xamscaner.BranchActivity.dbref;

public class Pdfreader extends AppCompatActivity {

    PDFView pdfView;
    String name;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        pdfView= (PDFView) findViewById(R.id.pdfView);
        pb= (ProgressBar) findViewById(R.id.progressBar4);
        pdfView.setVerticalScrollBarEnabled(true);
        Intent i = this.getIntent();
        name = i.getExtras().getString("NAME");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String path;
                path=dataSnapshot.child(name).getValue().toString();
                Find_file_exists(path,name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void Find_file_exists(String path, String fileName) {
        Uri destinationUri = null;
        if(!exists(path))
        {
            Toast.makeText(getApplicationContext(),"URL does not exist",Toast.LENGTH_LONG).show();
            return;
        }
        if(isExternalStorageWritable()) {
            destinationUri = Uri.parse(this.getExternalCacheDir().toString() + "/" + fileName + ".pdf");
            if (new File(Uri.parse(this.getExternalCacheDir().toString() + "/" + fileName + ".pdf").toString()).exists()) {
                display(destinationUri);
            } else
                Dwn_frm_path(path,destinationUri);

        }
    }

    void display(Uri path)
    {
        if (path!=null) {
            File file = new File(String.valueOf(path));
            if(file.canRead()){
                pdfView.fromFile(file).defaultPage(1).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Toast.makeText(Pdfreader.this,String.valueOf(nbPages),Toast.LENGTH_LONG).show();
                    }
                }).load();
            }
        }
    }



    private void Dwn_frm_path(String DownloadUrl,Uri destinationUri) {

        Uri downloadUri = Uri.parse(DownloadUrl);
        pb.setVisibility(View.VISIBLE);
        pb.setProgress(0);
        final Uri finalDestinationUri = destinationUri;
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .addCustomHeader("Auth-Token", "YourTokenApiKey")
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Toast.makeText(Pdfreader.this,"Download Completed",Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);
                        display(finalDestinationUri);
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Toast.makeText(Pdfreader.this,"Download Failed",Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                        double pers =100*downlaodedBytes/totalBytes;
                        pb.setProgress((int)pers);

                    }
                });
        ThinDownloadManager downloadManager;
        downloadManager = new ThinDownloadManager();
        int downloadId = downloadManager.add(downloadRequest);

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean exists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}