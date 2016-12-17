package online.zerone.xamscaner;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static online.zerone.xamscaner.BranchActivity.dbref;

public class Pdfreader extends AppCompatActivity {

    PDFView pdfView;
    String name;
    ProgressBar pb;
    TextView textView;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        pdfView= (PDFView) findViewById(R.id.pdfView);
        textView= (TextView) findViewById(R.id.textview2);
        pb= (ProgressBar) findViewById(R.id.progressBar4);
        pdfView.setVerticalScrollBarEnabled(true);
        Intent i = this.getIntent();
        name = i.getExtras().getString("NAME");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String path;
                path=dataSnapshot.child(name).getValue().toString();
                if(!URLUtil.isValidUrl(path))
                {
                    Toast.makeText(getApplicationContext(),"This URL is not valid",Toast.LENGTH_LONG).show();
                    return;
                }
                Find_file_exists(path,name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }





    private void Find_file_exists(String path, String fileName) {
        Uri destinationUri = null;
//        if(!exists(path))
//        {
//            Toast.makeText(getApplicationContext(),"URL does not exist",Toast.LENGTH_LONG).show();
//            return;
//        }
        if(isExternalStorageWritable()) {
            destinationUri = Uri.parse(this.getExternalCacheDir().toString() + "/" + fileName + ".pdf");
            if (new File(Uri.parse(this.getExternalCacheDir().toString() + "/" + fileName + ".pdf").toString()).exists()) {
                display(destinationUri,path);
            } else
                Dwn_frm_path(path,destinationUri);

        }
    }






    void display(Uri path,String link)
    {
//        try {
//            byte[] key = generateKey(link.substring(link.lastIndexOf("-")));
//            File file = new File(path.toString());
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            byte[] b = new byte[(int) file.length()];
//            byte[] decodedData=decodeFile(key,b);
//            bos.write(decodedData);
//            bos.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (path!=null) {
            File file = new File(String.valueOf(path));
            if(file.canRead()){
                pdfView.fromFile(file).defaultPage(0).enableSwipe(true).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                }).load();
            }
        }
    }




    private void Dwn_frm_path(final String DownloadUrl, final Uri destinationUri) {

        final Uri downloadUri = Uri.parse(DownloadUrl);
        pb.setVisibility(View.VISIBLE);
        pb.setProgress(0);

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .addCustomHeader("Auth-Token", "YourTokenApiKey")
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Toast.makeText(Pdfreader.this,"Download Completed",Toast.LENGTH_LONG).show();
//                        File file = new File(destinationUri.toString());
//                        byte[] b = new byte[(int) file.length()];
//                        BufferedOutputStream bos = null;
//                        try {
//                            bos = new BufferedOutputStream(new FileOutputStream(file));
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        String pwd = destinationUri.toString().substring(destinationUri.toString().lastIndexOf("-"));
//                        byte[] yourKey = new byte[0];
//                        try {
//                            yourKey = generateKey(pwd);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            byte[] fileBytes = encodeFile(yourKey,b);
//                            bos.write(fileBytes);
//                            bos.flush();
//                            bos.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }



                        pb.setVisibility(View.INVISIBLE);
                        textView.setText("");
                        display(destinationUri,downloadUri.toString());
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Toast.makeText(Pdfreader.this,"Download Failed",Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);
                        textView.setText("");

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                        double pers =100*downlaodedBytes/totalBytes;
                        pb.setProgress((int)pers);
                        String s =(int)pers+"%";
                        textView.setText(s);

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










    public byte[] generateKey(String password) throws Exception
    {
        byte[] keyStart = password.getBytes("UTF-8");
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public static byte[] encodeFile(byte[] key, byte[] fileData) throws Exception
    {

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }

    public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception
    {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted_file = cipher.doFinal(fileData);
        return decrypted_file;
    }


}