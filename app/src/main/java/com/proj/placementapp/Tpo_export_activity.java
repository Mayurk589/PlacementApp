package com.proj.placementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tpo_export_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpo_export_activity);

        Button exportOldButton = findViewById(R.id.exportOldButton);
        Button exportNEWButton = findViewById(R.id.exportNewButton);

        exportOldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEvent = new Intent(Tpo_export_activity.this, Tpo_old_display.class);
                startActivity(createEvent);
            }
        });


        exportNEWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEvent = new Intent(Tpo_export_activity.this, Tpo_new_display.class);
                startActivity(createEvent);
            }
        });


    }
}
