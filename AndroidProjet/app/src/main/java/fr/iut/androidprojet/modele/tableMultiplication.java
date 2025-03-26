package fr.iut.androidprojet.modele;

import java.util.ArrayList;
import java.util.Collections;

public class tableMultiplication {
    private ArrayList<Multiplication> multiplications;
    private int table;
    public tableMultiplication(int table, boolean estMelange) {
        this.table = table;
        this.multiplications = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            multiplications.add(new Multiplication(table, i));
        }
        if (estMelange) {
            Collections.shuffle(multiplications);
        }
    }

    public ArrayList<Multiplication> getMultiplications() {
        return multiplications;
    }
    public int getNbReponsesJustes() {
        int compteur = 0;
        for (Multiplication uneMult:
             multiplications) {
            if (uneMult.isReponseJuste()) {
                compteur += 1;
            }
        }
        return compteur;
    }

    public int getTable() {
        return table;
    }
}
