package com.gorobaby.jh.phonebook.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gorobaby.jh.phonebook.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by love on 2016-05-26.
 */
public class PersonAdapter extends BaseAdapter {
    private Context mContext;
    private Activity mActivity;
    private List<Person> mContactList;
    private LayoutInflater mInflater;
    private int mLayout;

    public PersonAdapter(Context context,Activity activity,
                         List<Person> contactList) {
        this.mContext = context;
        this.mActivity = activity;
        this.mContactList = contactList;
        this.mLayout = R.layout.row_person;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ContactViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayout, parent, false);

            viewHolder = new ContactViewHolder();
                    viewHolder.image = (ImageView) convertView
                            .findViewById(R.id.row_person_image);
                    viewHolder.name = (TextView) convertView
                            .findViewById(R.id.row_person_name);
                    viewHolder.number = (TextView) convertView
                            .findViewById(R.id.row_person_phonenumber);
                    viewHolder.rowLayout = (LinearLayout) convertView.findViewById(R.id.row_person_ll);

                    viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 1. Instantiate an AlertDialog.Builder with its constructor
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                            builder.setMessage(viewHolder.number.getText())
                                    .setTitle(viewHolder.name.getText());

                            AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ContactViewHolder) convertView.getTag();
        }

        viewHolder.id = mContactList.get(position).getId();
        try {
            if (mContactList.get(position).getPicture() != null) {
                Uri picURI = Uri.parse(mContactList.get(position).getPicture());
                InputStream inputStream = mContext.getContentResolver().openInputStream(Uri.parse(mContactList.get(position).getPicture()));
                viewHolder.image.setImageDrawable(Drawable.createFromStream(inputStream, picURI.toString()));
            } else {
                viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
            }

        } catch (FileNotFoundException e) {
            viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
        }

        viewHolder.name.setText(mContactList.get(position).getName());
        viewHolder.number.setText(mContactList.get(position).getPhoneNumber());

        return convertView;
    }

    public class ContactViewHolder {
        public int id;
        public ImageView image;
        public TextView name;
        public TextView number;
        public LinearLayout rowLayout;
    }
}
