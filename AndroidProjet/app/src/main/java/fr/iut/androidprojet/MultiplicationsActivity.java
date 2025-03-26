package fr.iut.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class MultiplicationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge le XML pour créer l'arbre graphique
        setContentView(R.layout.activity_multiplications);

        NumberPicker numberPicker = findViewById(R.id.multiplication_number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(9);

        Button submitBtn = findViewById(R.id.multiplication_submit_table);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiplicationsActivity.this, TableOperationActivity.class);
                intent.putExtra(TableOperationActivity.TABLE_KEY, numberPicker.getValue());
                startActivity(intent);
            }
        });

    }
}