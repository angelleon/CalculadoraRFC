package com.pandalab.calculadoraRfc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SegundaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String rfc = bundle.getString("rfc", "");
            ((TextView) findViewById(R.id.txtRFC)).setText(rfc);
        }
    }
}
