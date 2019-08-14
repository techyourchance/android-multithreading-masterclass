package com.techyourchance.multithreading.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techyourchance.multithreading.R;

import androidx.annotation.NonNull;

public class HomeArrayAdapter extends ArrayAdapter<ScreenReachableFromHome> {

    public interface Listener {
        void onScreenClicked(ScreenReachableFromHome screenReachableFromHome);
    }

    private final Listener mListener;

    public HomeArrayAdapter(@NonNull Context context, Listener listener) {
        super(context, 0);
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_screen_reachable_from_home, parent, false);
        }

        final ScreenReachableFromHome screenReachableFromHome = getItem(position);

        // display screen name
        TextView txtName = convertView.findViewById(R.id.txt_screen_name);
        txtName.setText(screenReachableFromHome.getName());

        // set click listener on individual item view
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onScreenClicked(screenReachableFromHome);
            }
        });

        return convertView;
    }

}
