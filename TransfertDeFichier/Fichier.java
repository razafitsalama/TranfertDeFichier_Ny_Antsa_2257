package server;

public class Fichier {
    int id;
    String nom;
    byte[] donnee;
    String fichierExtens;
    public Fichier(int id, String nom, byte[] donnee, String fichierExtens) {
        setId(id);
        setNom(nom);
        setDonnee(donnee);
        setFichierExtens(fichierExtens);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public byte[] getDonnee() {
        return donnee;
    }
    public void setDonnee(byte[] donnee) {
        this.donnee = donnee;
    }
    public String getFichierExtens() {
        return fichierExtens;
    }
    public void setFichierExtens(String fichierExtens) {
        this.fichierExtens = fichierExtens;
    }
}