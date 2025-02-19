package fr.iut2.androidtp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.iut2.androidtp.exercice3Data.Multiplication;
import fr.iut2.androidtp.exercice3Data.tableMultiplication;

public class TableMultiplicationActivity extends AppCompatActivity {

    public static final String TABLE_KEY = "table_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table_multiplication);
        int table = getIntent().getIntExtra(TABLE_KEY, 1);
        tableMultiplication tableMult = new tableMultiplication(table, true);

        LinearLayout linear = findViewById(R.id.exercice5_table_multiplication);
        for (Multiplication multiplication:
             tableMult.getMultiplications()) {
            // 2. Création de la ligne temporaire
            LinearLayout linearTMP = (LinearLayout) getLayoutInflater().inflate(R.layout.template_calcul, null);

            // 3. Création du texte décrivant l'opération
            TextView calcul = (TextView) linearTMP.findViewById(R.id.template_calcul);
            calcul.setText(multiplication.getOperande1() + " x " + multiplication.getOperande2() + " = ");

            // 4. Création de l'EditText permettant d'interagir avec l'utilisateur
            EditText resultat = (EditText) linearTMP.findViewById(R.id.template_resultat);

            resultat.setText(Integer.toString(multiplication.getResultat()));

            // 5. Ajout au Linear principal
            linear.addView(linearTMP);
        }

        Button submitBtn = findViewById(R.id.exercice5_submit_result);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Il faut compter le nombre de réponses justes
                // puis rediriger vers l'activité Felicitation ou Erreur
            }
        });


    }
}