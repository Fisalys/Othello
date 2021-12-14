package sample;

public class Resultat
{
    private int valeur;
    private Coup coup;

    public Resultat(int valeur, Coup coup) {
        this.valeur = valeur;
        this.coup = coup;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public Coup getCoup() {
        return coup;
    }

    public void setCoup(Coup coup) {
        this.coup = coup;
    }
}
