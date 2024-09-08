package com.proj.placementapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class EventsActivity extends Activity {

    EventsAdapter adapter;
    ListView lstView;
    ArrayList<EventModel> eventList;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_events);

        lstView = findViewById(R.id.listStudents);
        eventList = new ArrayList<>();
        adapter = new EventsAdapter();
        lstView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        getEvents();

        // Set item click listener for the ListView
        lstView.setOnItemClickListener((parent, view, position, id) -> {
            EventModel clickedEvent = eventList.get(position);
            String eventid = clickedEvent.getEventId();
            String eventDetailsUrl = "http://" + Config.ipAddress + "/PlacementApp/getevent.php?eve" +
                    "ntid=" + eventid;
            fetchAndShowDetails(eventDetailsUrl, "Event Details");
        });
    }

    private void getEvents() {
        String url = Config.getAllEvents;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String eventId = jsonObject.getString("event_id");
                            String name = jsonObject.getString("name");
                            String date = jsonObject.getString("date");
                            String location = jsonObject.getString("location");
                            String description = jsonObject.getString("description");
                            EventModel event = new EventModel(eventId, name, date, location, description);
                            eventList.add(event);
                        }

                        // Sort the eventList based on date in descending order
                        Collections.sort(eventList, (event1, event2) -> {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = sdf.parse(event1.getDate());
                                Date date2 = sdf.parse(event2.getDate());
                                return date2.compareTo(date1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        });

                        adapter.notifyDataSetChanged(); // Notify adapter about data change
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog("JSON Parsing Error: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                    showErrorDialog("Volley Error: " + error.getMessage());
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchAndShowDetails(String url, String dialogTitle) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    showDetailsDialog(response, dialogTitle);
                },
                error -> {
                    showErrorDialog("Failed to fetch data. Please try again.");
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void showDetailsDialog(JSONArray details, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Build the message to display based on the JSON response
        StringBuilder message = new StringBuilder();
        try {
            for (int i = 0; i < details.length(); i++) {
                JSONObject detail = details.getJSONObject(i);
                message.append("Event ID: ").append(detail.optString("event_id")).append("\n");
                message.append("Name: ").append(detail.optString("name")).append("\n");
                message.append("Date: ").append(detail.optString("date")).append("\n");
                message.append("Location: ").append(detail.optString("location")).append("\n");
                message.append("Description: ").append(detail.optString("description")).append("\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorDialog("JSON Parsing Error: " + e.getMessage());
            return;
        }

        builder.setMessage(message.toString());
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    class EventsAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(EventsActivity.this).inflate(R.layout.lst_items_view_events, parent, false);
            }

            // Get references to views in lst_items_view_events.xml
            TextView eventName = convertView.findViewById(R.id.tvName);
            TextView eventDate = convertView.findViewById(R.id.tvDate);
            TextView eventLocation = convertView.findViewById(R.id.tvLoc);
            TextView eventDescription = convertView.findViewById(R.id.tvDesc);

            // Get the current event object
            EventModel event = eventList.get(position);

            // Set data to the views
            eventName.setText(event.getName());
            eventDate.setText(event.getDate());
            eventLocation.setText(event.getLocation());
            eventDescription.setText(event.getDescription());

            return convertView;
        }
    }
}
