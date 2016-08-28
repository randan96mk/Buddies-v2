package com.buddies.mkg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.trendit.mkg.trendit.R;

public class SearchFilter extends AppCompatActivity {
    private String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example", "ds", "yre", "gfdffd", "jjfjj", "fdgfdf", "fdgg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        Spinner popupSpinner = (Spinner) findViewById(R.id.popupspinner);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, SPINNERLIST);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupSpinner.setAdapter(adapter);

        Button searchfilter = (Button) findViewById(R.id.searchfilter);
        searchfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Buddies.class);
                startActivity(intent);
            }
        });

    }
}
