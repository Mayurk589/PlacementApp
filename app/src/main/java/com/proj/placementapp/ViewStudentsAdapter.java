package com.proj.placementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewStudentsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    ArrayList<HashMap<String, String>> arrLstData;

    HashMap<String, String> hashMapResult = new HashMap<String, String>();

    TextView tvName;
    TextView tvPhone;
    TextView tvUSN;
    ImageView ivUploadedImage;
    String imagepath;

    public ViewStudentsAdapter(Context context,
                               ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        arrLstData = arraylist;
    }

    @Override
    public int getCount() {
        return arrLstData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lst_items_view_students, parent,false);

        hashMapResult = arrLstData.get(position);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
        tvUSN = (TextView) itemView.findViewById(R.id.tvUSN);

        ivUploadedImage = (ImageView) itemView.findViewById(R.id.noteImageView);

        tvName.setText(hashMapResult.get("name"));
        tvPhone.setText(hashMapResult.get("phone"));
        tvUSN.setText(hashMapResult.get("usn"));

        imagepath = "";

        /*Picasso.get()
                .load(imagepath)
                .into(ivUploadedImage);*/

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                hashMapResult = arrLstData.get(position);

                Intent intent = new Intent(context, StudentDetails.class);
                intent.putExtra("usn", hashMapResult.get("usn"));
                intent.putExtra("phone", hashMapResult.get("phone"));

                context.startActivity(intent);
            }
        });
        return itemView;
    }
    


}