package jtb.supermaster.Mini_Jeux;
import java.util.ArrayList;
import java.util.Random;

import jtb.supermaster.Mini_Jeux.Enigme;


public class Enigmes {
    private ArrayList<Enigme> enigmes;
    private int nombre;
    private int compteur;
    private Boolean[] fait;

    public Enigmes(){
        this.enigmes = new ArrayList<>();
        this.compteur = 0;
        this.remplirEnigmes();
    }

    private void remplirEnigmes(){
        String[][] listeEngimes = new String[][]{
                {"Quelle est la formule chimique de la molécule d'eau ?", "H2O", "CO2", "gauche"},
                {"Qui était surnommé 'le Roi-Soleil' ?", "Louis XVI", "Louis XIV", "droite"},
                {"Je suis le troisième d'une course. Je dépasse le deuxième, quelle est ma position ?", "Premier", "Deuxième", "droite"},
                {"Je possède un jardin rectangulaire de 4 mètres sur 8. Quelle est la surface de mon jardin ?", "32 mètres carrés", "12 mètres carrés", "gauche"},
                {"Une année-lumière équivaut à...", "1 Gkm", "1 Mkm", "gauche"},
                {"J'ai invité 37 amis, 8 ne viennent pas. Combien d'amis sont venus me voir? ", "19", "29", "droite"},
                {"Combien de côtés possède un hexagone ?", "8", "6", "droite"},
                {"Quelle est la sixième planète du système Solaire ? ", "Saturne", "Jupiter", "gauche"},
                {"Dans quel océan se trouve l'archipel d'Hawaï ?", "Pacifique", "Atlantique", "gauche"},
                {"J'ai 10 ans, mon petit frère a la moitié de mon âge.\nQuel âge aura t-il quand j'aurais le triple de mon age?", "15 ans", "25 ans", "droite"},
                {"2 + 4 * (6 + 4) ", "= 42", "= 60", "gauche"},
                {"(3 + 8) - 1 * 5 ", "= 80", "= 50", "droite"},
                {"2 * 2 * 2 * 2 * 2", "= 32", "= 64", "gauche"},
                {"100 / 10 * 10", "= 1", "= 100", "droite"},
                {"2€ - 20% = ?", "= 1.6€","1.8€", "gauche"},
                {"(10€ - 90%) * 10", "9€", "10€","droite"},
                {"3 * x = 24", "6", "8", "droite"},
                {"11 * 7 + 3 + 4 * 5", "= 100", "!= 100", "gauche"},
                {"1 / 0.1", "> 1", "< 1", "gauche"},
                {"Kayak est ", "une anagramme", "un palindrome", "droite"},
                {"Argent est ... de gérant.", "l'anagramme", "le synonyme", "gauche"},
                {"Une tomate est un ", "fruit", "légume", "gauche"},
                {"L'avocat est un ", "légume", "fruit", "droite"},
                {"Le potiron est un ", "légume", "fruit", "gauche"},
                {"La souris mange le fromage, le chat mange la souris. Qui reste-t-il ?", "Rien", "Le chat", "droite"},
                {"Quel nombre divisé par lui-même donne son double ?", "0.5", "Aucun", "gauche"},
                {"Une brique pèse 1 kg plus une demi-brique. Combien pèse une brique ?", "1.5 Kg", "2 Kg", "droite"},
                {"Trouvez la suite logique de :\n1, 2, 3, 5, 7, ?", "11", "9", "gauche"},
                {"Trouvez la suite logique de :\n0, 1, 1, 2, 3, ?", "13", "11", "droite"},
                {"Trouvez la suite logique de :\n1, 2, 4, 8, 16, ?", "32", "24", "gauche"},
                {"Votre médecin vous donne 3 cachets à prendre à raison de un toutes les demi-heures. Combien de temps cela dure-t-il ?" ,"1 h 30", "1 h", "droite"},
                {"1,25h équivaut à" ,"1 h 15", "1 h 25", "gauche"},
                {"Un fermier a 20 moutons, tous sauf 11 meurent. Combien reste-il de moutons ?", "9", "11", "droite"},
                {"Combien y a-t-il de paires de chaussettes dans une douzaine de paires ?", "12", "24", "gauche"},
                {"Tu es le petit-fils de mon père, qui es-tu?", "Mon frère", "Mon fils", "droite"},
                {"Que vaut le quart de la moitié du double de 32?", "8" , "16", "gauche"},
                {"Un tonneau rempli de vin pèse 20 kg. Le même tonneau rempli à moitié pèse 12 kg. Combien pèse le tonneau vide?", "10 Kg","4 Kg","droite"},
                {"Quelle note est plus aigüe d'un Sol?","Fa","Ré","gauche"},
                {"Que signifie le P de P.L.S.?","Protection","Position","droite"},
                {"Que signifie le A de A.D.N.?","Acide","Anatomical","gauche"}};

        this.nombre = listeEngimes.length;
        this.fait = new Boolean[nombre];

        for(int i = 0; i < nombre; i++){
            Enigme e = new Enigme(listeEngimes[i][0],listeEngimes[i][1], listeEngimes[i][2], listeEngimes[i][3]);
            this.enigmes.add(e);
            this.fait[i] = false;
        }
    }

    public Enigme next(){
        int indice = this.indice();
        return this.enigmes.get(indice);
    }

    private int indice(){
        if(compteur == this.nombre){
            for(int i = 0; i < this.nombre; i++) this.fait[i] = false;
            this.compteur = 0;
        }

        Random randomGenerator = new Random();
        int indice = randomGenerator.nextInt(this.nombre);

        while(this.fait[indice]){
            indice = randomGenerator.nextInt(this.nombre);
        }

        this.fait[indice] = true;
        this.compteur++;
        return indice;
    }

    public int getNombre (){
        return this.nombre;
    }

    public void setFait (Boolean[] fait){
        System.arraycopy(fait, 0, this.fait, 0, fait.length );
    }

    public Boolean[] getFait (){
        return this.fait;
    }
}
