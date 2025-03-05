package models;

public class Remplacent {
    private int id;
    private int idConge;
    private int idRemplacent;
    private PosteRemplace posteRemplace;

    public Remplacent() {
    }

    public Remplacent(int id, int idConge, int idRemplacent, PosteRemplace posteRemplace) {
        this.id = id;
        this.idConge = idConge;
        this.idRemplacent = idRemplacent;
        this.posteRemplace = posteRemplace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdConge() {
        return idConge;
    }

    public void setIdConge(int idConge) {
        this.idConge = idConge;
    }

    public int getIdRemplacent() {
        return idRemplacent;
    }

    public void setIdRemplacent(int idRemplacent) {
        this.idRemplacent = idRemplacent;
    }

    public PosteRemplace getPosteRemplace() {
        return posteRemplace;
    }

    public void setPosteRemplace(PosteRemplace posteRemplace) {
        this.posteRemplace = posteRemplace;
    }

    @Override
    public String toString() {
        return "Remplacent{" +
                "id=" + id +
                ", idConge=" + idConge +
                ", idRemplacent=" + idRemplacent +
                ", posteRemplace=" + posteRemplace +
                '}';
    }
}
