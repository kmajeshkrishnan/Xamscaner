package online.zerone.xamscaner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

/**
 * Created by ANONYMOUS on 05-12-2016.
 * IT'S ROLE WILL BE EXPLAINED SOON..........
 */

public class CardviewAdapter extends BaseAdapter{
    private Context context;
    private List<Pdfdoc> listpdf = new ArrayList<>();

    public CardviewAdapter(Context context,ArrayList<Pdfdoc> listpdfs){
        this.context=context;
        this.listpdf=listpdfs;
    }

    @Override
    public int getCount() {
        return listpdf.size();
    }

    @Override
    public Object getItem(int i) {
        return listpdf.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.modelgrid,viewGroup,false);
        }

        final Pdfdoc pdfdoc = (Pdfdoc) this.getItem(i);

        TextView tv = (TextView) view.findViewById(R.id.pdfname);
        ImageView im = (ImageView) view.findViewById(R.id.pdfimg);
        tv.setText(pdfdoc.getname());
        im.setImageResource(R.mipmap.ic_launcher);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPDFview(pdfdoc.getPath());
            }
        });
        return view;
    }

    private void openPDFview(String path) {
        Intent i = new Intent(context,Pdfreader.class);
        i.putExtra("PATH",path);
        context.startActivity(i);
    }
}
