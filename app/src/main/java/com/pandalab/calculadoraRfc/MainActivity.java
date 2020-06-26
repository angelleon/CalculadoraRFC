package com.pandalab.calculadoraRfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.zip.DataFormatException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btnCalcularRFC)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int anio;
                int mes;
                int dia;
                String nombre = ((TextView) findViewById(R.id.txtNombre))
                        .getText()
                        .toString();
                String apellidoP = ((TextView) findViewById(R.id.txtApellidoP))
                        .getText()
                        .toString();
                String apellidoM = ((TextView) findViewById(R.id.txtApellidoM))
                        .getText()
                        .toString();

                LocalDate fechaNac;
                try {
                    anio = Integer.parseInt(((EditText) findViewById(R.id.txtAnio)).getText().toString());
                    mes = Integer.parseInt(((EditText) findViewById(R.id.txtMes)).getText().toString());
                    dia = Integer.parseInt(((EditText) findViewById(R.id.txtDia)).getText().toString());
                    fechaNac = LocalDate.of(anio, mes, dia);
                } catch (NumberFormatException | DateTimeException ex) {
                    Log.e("", ex.getMessage());
                    return;
                }
                Rfc rfc = new Rfc(nombre, apellidoP, apellidoM, fechaNac);
                Intent intent = new Intent(MainActivity.this, SegundaActivity.class);
                intent.putExtra("rfc", rfc.getRFC());
                startActivity(intent);
            }
        });
    }
}
