package com.jasonkcwong.pigeon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jasonkcwong.pigeon.Models.Contact;
import com.jasonkcwong.pigeon.OnItemClickListener;
import com.jasonkcwong.pigeon.R;

import java.util.List;

/**
 * Created by jason on 16-07-31.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    private List<Contact> mList;
    private OnItemClickListener mOnItemClickListener;
    private TextView nameText;

    public ContactAdapter(Context context, List<Contact> contacts, OnItemClickListener listener){
        super(context, android.R.layout.simple_list_item_1, contacts);
        mList = contacts;
        mOnItemClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Contact contact = mList.get(position);
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_adapter_item, null);
        }
        nameText = (TextView) view.findViewById(R.id.nameText);

        nameText.setText(contact.getDisplayName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });
        return view;
    }
}
