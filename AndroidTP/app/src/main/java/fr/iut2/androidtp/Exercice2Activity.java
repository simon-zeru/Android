package fr.iut2.androidtp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Exercice2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge le XML pour créer l'arbre graphique
        setContentView(R.layout.activity_exercice2);

        validateButton();
    }

    public void validateButton() {

        RadioGroup radioGroupView = findViewById(R.id.exercice2_group);
        Button submitBtn = findViewById(R.id.exercice2_valider);

        TextView submitMessage = findViewById(R.id.exercice2_valider_message);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioGroupView.getCheckedRadioButtonId() == R.id.
                        exercice2_bonne_reponse) {
                        submitMessage.setText(R.string.bravo_vous_avez_la_bonne_r_ponse);
                } else {
                    submitMessage.setText(R.string.mauvaise_r_ponse);

                }

            }
        });


    }
}
