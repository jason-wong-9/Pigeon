package com.jasonkcwong.pigeon.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.jasonkcwong.pigeon.Models.Message;

import java.util.List;

/**
 * Created by jason on 16-08-02.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private List<Message> mList;
    public MessageAdapter(Context context, List<Message> messages){
        super(context, android.R.layout.simple_list_item_1, messages);
        mList = messages;
    }
}
