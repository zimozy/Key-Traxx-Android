package com.example.keytraxx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnActivationActivity = findViewById(R.id.btnActivationActivity);
        Button btnMapActivity           = findViewById(R.id.btnMapActivity);
        Button btnCarActivity           = findViewById(R.id.btnCarActivity);

        btnMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(getApplicationContext(), MapActivity.class)
                );
            }
        });

        btnActivationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(getApplicationContext(), ActivationActivity.class)
                );
            }
        });

        btnCarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(getApplicationContext(), CarActivity.class)
                );
            }
        });

    }
}
