package fr.iut2.androidtp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ErreurActivity extends AppCompatActivity {

    public static final String NB_ERRORS = "nb_errors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_erreur);

        int nb_errors = getIntent().getIntExtra(NB_ERRORS, 0);

        TextView nb_errors_view = findViewById(R.id.exercice5_text_nb_erreurs);
        nb_errors_view.setText("Nombre d'erreurs : "+nb_errors);

        Button comeback = findViewById(R.id.exercice5_corriger_reponses);

        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });


    }
}