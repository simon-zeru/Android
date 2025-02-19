package fr.iut2.androidtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Exercice4Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge le XML pour créer l'arbre graphique
        setContentView(R.layout.activity_exercice4);

        EditText prenom = findViewById(R.id.exercice4_prenom);
        Button validateBtn = findViewById(R.id.exercice4_valider);

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Exercice4Activity.this, HelloActivity.class);
                intent.putExtra(HelloActivity.PRENOM_KEY, prenom.getText().toString());
                startActivity(intent);
            }
        });


    }
}
