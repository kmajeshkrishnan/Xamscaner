package online.zerone.xamscaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.File;

public class Pdfreader extends AppCompatActivity {

    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        pdfView= (PDFView) findViewById(R.id.pdfView);
        pdfView.setVerticalScrollBarEnabled(true);
        Intent i = this.getIntent();
        String path = i.getExtras().getString("PATH");
        if (path!=null) {
            File file = new File(path);
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
}
