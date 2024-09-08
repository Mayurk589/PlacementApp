package com.proj.placementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<EventModel> eventList;

    public EventsAdapter(Context context, ArrayList<EventModel> eventList) {
        this.context = context;
        this.eventList = eventList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lst_items_view_events, parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.tvName);
        TextView eventDate = convertView.findViewById(R.id.tvDate);
        TextView eventLocation = convertView.findViewById(R.id.tvLoc);
        TextView eventDescription = convertView.findViewById(R.id.tvDesc);

        EventModel event = eventList.get(position);

        eventName.setText(event.getName());
        eventDate.setText(event.getDate());
        eventLocation.setText(event.getLocation());
        eventDescription.setText(event.getDescription());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clicked event
                EventModel clickedEvent = eventList.get(position);

                // Start the activity to show event details
                Intent intent = new Intent(context, EventsAdapter.class);
                intent.putExtra("eventId", clickedEvent.getEventId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
