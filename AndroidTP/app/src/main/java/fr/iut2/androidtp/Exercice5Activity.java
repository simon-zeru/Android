package fr.iut2.androidtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class Exercice5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge le XML pour créer l'arbre graphique
        setContentView(R.layout.activity_exercice5);

        NumberPicker numberPicker = findViewById(R.id.exercice5_choisir_table);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(9);

        Button submitBtn = findViewById(R.id.exercice5_submit_table);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Exercice5Activity.this, TableMultiplicationActivity.class);
                intent.putExtra(TableMultiplicationActivity.TABLE_KEY, numberPicker.getValue());
                startActivity(intent);
            }
        });

    }
}
