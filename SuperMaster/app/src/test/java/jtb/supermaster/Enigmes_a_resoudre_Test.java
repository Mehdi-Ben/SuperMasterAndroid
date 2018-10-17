package jtb.supermaster;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import jtb.supermaster.Mini_Jeux.Enigme;
import jtb.supermaster.Mini_Jeux.Enigmes;

/**
 * Created by Johann on 27/02/2017.
 */

public class Enigmes_a_resoudre_Test {
    Enigmes enigmes = null;
    Enigme monEnigme = null;

    @Before
    public void setup() {
        enigmes = new Enigmes();
        monEnigme = this.enigmes.next();
    }

    @Test
    public void next_isCorrect() {
        String question = monEnigme.getQuestion();
        monEnigme = enigmes.next();
        String question_suivante = monEnigme.getQuestion();
        assertNotEquals(question, question_suivante);
    }

    @Test
    public void indice_isCorrect() {
        int nombre = enigmes.getNombre();
        String[] questions_Faites = new String[nombre];
        Boolean bug = false;
        for (int i = 0; i < nombre; i++) {
            questions_Faites[i] = monEnigme.getQuestion();
            for (int i2 = 0; i2 < i; i2++) {
                if (questions_Faites[i2] == monEnigme.getQuestion()) {
                    bug = true;
                }
            }
        }

        monEnigme.getQuestion();
        int cptFalse = 0;
        Boolean[] fait = enigmes.getFait();
        for (int i = 0; i < nombre; i++)
            if(!fait[i])  cptFalse++;

        assertNotEquals(bug, false);
        assertNotEquals(cptFalse, 1);
    }




}
