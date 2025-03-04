package models;

public class Equipe {

    private int idEquipe;
    private String nomEquipe;
    private int nbrEmployee;
    private String nomTeqlead;
    // Constructor
    public Equipe() {} // No-argument constructor

    public Equipe( String nomEquipe, int nbrEmployee, String nomTeqlead) {
        this.nomEquipe = nomEquipe;
        this.nbrEmployee = nbrEmployee;
        this.nomTeqlead = nomTeqlead;
    }

    public Equipe(int idEquipe, String nomEquipe, int nbrEmployee, String nomTeqlead) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.nbrEmployee = nbrEmployee;
        this.nomTeqlead = nomTeqlead;
    }

    public int getIdEquipe() {
        return idEquipe;
    }
    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getNomEquipe() {
        return nomEquipe;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public int getNbrEmployee() {
        return nbrEmployee;
    }

    public void setNbrEmployee(int nbrEmployee) {
        this.nbrEmployee = nbrEmployee;
    }

    public String getNomTeqlead() {
        return nomTeqlead;
    }

    public void setNomTeqlead(String nomTeqlead) {
        this.nomTeqlead = nomTeqlead;
    }

    @Override
    public String toString() {
        return nomEquipe;
    }

}