package fr.iut.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.iut.androidprojet.modele.Operation;
import fr.iut.androidprojet.modele.OperationEnum;
import fr.iut.androidprojet.modele.OperationUtilitaire;
import fr.iut.androidprojet.modele.TableOperation;

public class TableOperationActivity extends AppCompatActivity {

    public static final String TABLE_KEY = "table_key";

    public static final String TYPE_OPERATION = "type_operation_key";
    private ArrayList<EditText> userResponses = new ArrayList<>();
    private static final int SCORE_MAX = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table_multiplication);

        int table = getIntent().getIntExtra(TABLE_KEY, 1);
        TableOperation tableMult = new TableOperation(OperationUtilitaire.getTypeOperation(TYPE_OPERATION));

        LinearLayout linear = findViewById(R.id.linear_table_operation);



        for (Operation operation:
                tableMult.getOperations()) {
            // 2. Création de la ligne temporaire
            LinearLayout linearTMP = (LinearLayout) getLayoutInflater().inflate(R.layout.template_calcul, null);

            // 3. Création du texte décrivant l'opération
            TextView calcul = (TextView) linearTMP.findViewById(R.id.template_calcul);
            calcul.setText(operation.getOperande1() + " x " + operation.getOperande2() + " = ");

            // 4. Création de l'EditText permettant d'interagir avec l'utilisateur
            EditText resultat = (EditText) linearTMP.findViewById(R.id.template_resultat);
            userResponses.add(resultat);

            // 5. Ajout au Linear principal
            linear.addView(linearTMP);
        }

        Button submitBtn = findViewById(R.id.exercice5_submit_result);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Il faut compter le nombre de réponses justes
                // puis rediriger vers l'activité Felicitation ou Erreur
//                int i = 0;
//                for (Multiplication multiplication:
//                        tableMult.getMultiplications()) {
//                    EditText res = userResponses.get(i);
//                    if (res == null) {
//                        multiplication.setReponseUtilisateur(-1);
//                    } else {
//                        Log.d("Reponse ligne", res.getText().toString());
//                        multiplication.setReponseUtilisateur(Integer.parseInt(res.getText().toString()));
//                    }
//                    i++;
//                }
//                int score = tableMult.getNbReponsesJustes();
//                int errors = SCORE_MAX - score;
//                Intent intent;
//                if (errors == 0) {
//                    intent = new Intent(TableOperationActivity.this, FelicitationActivity.class);
//
//                } else {
//                    intent = new Intent(TableOperationActivity.this, ErreurActivity.class);
//                    intent.putExtra(ErreurActivity.NB_ERRORS, errors);
//                }
//                startActivity(intent);
            }
        });
    }


}