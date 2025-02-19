package fr.iut2.androidtp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.iut2.androidtp.exercice3Data.Jeu;
import fr.iut2.androidtp.exercice3Data.Resultat;

public class Exercice3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge le XML pour créer l'arbre graphique
        setContentView(R.layout.activity_exercice3);

        play();
    }

    public void play() {
        ImageButton paperBtn = findViewById(R.id.exercice3_player_paper);
        ImageButton rockBtn = findViewById(R.id.exercice3_player_rock);
        ImageButton scissorsBtn = findViewById(R.id.exercice3_player_scissors);


        // Instance du résultat des parties
        Resultat resultat = new Resultat();

        paperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHand(Jeu.PAPIER, resultat);
            }
        });

        rockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHand(Jeu.CAILLOUX, resultat);
            }
        });

        scissorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHand(Jeu.CISEAUX, resultat);
            }
        });

    }

    public void submitHand(int mainJoueur, Resultat resultat) {
        LinearLayout cpuHandLayout = findViewById(R.id.exercice3_cpu_hand_layout);
        TextView gameState = findViewById(R.id.exercice3_game_state);

        // Récupérer les compteurs
        TextView compteurVictoire = findViewById(R.id.exercice3_victory_count);
        TextView compteurDefaites = findViewById(R.id.exercice3_defeat_count);
        TextView compteurEgalite = findViewById(R.id.exercice3_equality_count);

        // **FIX: Remove the previous CPU hand image**
        cpuHandLayout.removeAllViews();

        // Initialiser la main de l'ordinateur
        Jeu jeu = new Jeu();

        // Initialiser la main du joueur
        jeu.setMainJoueur(mainJoueur);

        // Afficher la main de l'ordinateur
        int mainCpu = jeu.getMainOrdinateur();
        ImageButton imageCpuHand = new ImageButton(this);

        int imageCpu = mainCpu == Jeu.CAILLOUX ? R.drawable.caillou : mainCpu == Jeu.PAPIER ? R.drawable.papier : R.drawable.ciseaux;

        imageCpuHand.setImageResource(imageCpu);

        cpuHandLayout.addView(imageCpuHand);

        // Afficher le résultat et l'actualiser
        if (jeu.joueurGagne()) {
            gameState.setText(R.string.victoire);
            resultat.addVictoire();
            compteurVictoire.setText(String.valueOf(resultat.getNombreVictoire()));
        } else if (jeu.egalite()) {
            gameState.setText(R.string.egalit);
            resultat.addEgalite();
            compteurEgalite.setText(String.valueOf(resultat.getNombreEgalite()));
        } else {
            gameState.setText(R.string.d_faite);
            resultat.addDefaite();
            compteurDefaites.setText(String.valueOf(resultat.getNombreDefaite()));
        }
    }
}
