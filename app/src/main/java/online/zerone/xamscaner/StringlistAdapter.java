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

/**
 * Created by ANONYMOUS on 09-12-2016.
 * THIS IS THE BASE ADAPTER WHICH FORMS ALL THE CARD LISTS IN THE PGM
 */
class StringlistAdapter extends BaseAdapter{
    private Context context;
    private List<String> arrayList = new ArrayList<>();
    StringlistAdapter(Context context, ArrayList<String> arrayList){
        this.context =context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.modelgrid,viewGroup,false);
        }
        TextView tv = (TextView) view.findViewById(R.id.pdfname);
        ImageView im = (ImageView) view.findViewById(R.id.pdfimg);
        tv.setText(arrayList.get(i));
        if(context.getClass().equals(PdfListActivity.class))
            im.setImageResource(R.drawable.pdf);
        else
            im.setImageResource(R.mipmap.ic_launcher);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string_to_nextA(arrayList.get(i));
            }
        });
        return view;
    }



/**THIS FUNCTION IS TRIGGERED WHEN CLICKED ON A SINGLE CARD*/
    private void string_to_nextA(String s) {
        Intent i;
        if((context.getClass()).equals(BranchActivity.class)){
            i = new Intent(context,SemActivity.class);
            i.putExtra("BRANCH",s);
            context.startActivity(i);
        }
        else if((context.getClass()).equals(SemActivity.class)){
            i = new Intent(context,SubjectActivity.class);
            i.putExtra("SEM",s);
            context.startActivity(i);
        }
        else if((context.getClass().equals(SubjectActivity.class))){
            i= new Intent(context,PdfListActivity.class);
            i.putExtra("DATA",s);
            context.startActivity(i);
        }
        else if((context.getClass().equals(PdfListActivity.class))){

            i= new Intent(context,Pdfreader.class);
            i.putExtra("NAME",s);
            context.startActivity(i);
        }



    }
}
