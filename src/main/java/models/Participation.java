package models;

import java.util.Date;

public class Participation {
    private int id_participation;
    private int id_event;
    private int id_user;
    private Date date_participation;
    private String role_participant;
    private String depart_participant;
    private String contact;
    private String experience_event;
    private String remarque;

    public Participation() {}

    public Participation(int id_event, int id_user, Date date_participation, String role_participant,
                         String depart_participant, String contact, String experience_event, String remarque) {
        this.id_event = id_event;
        this.id_user = id_user;
        this.date_participation = date_participation;
        this.role_participant = role_participant;
        this.depart_participant = depart_participant;
        this.contact = contact;
        this.experience_event = experience_event;
        this.remarque = remarque;
    }

    public Participation(int id_participation, int id_event, int id_user, Date date_participation, String role_participant,
                         String depart_participant, String contact, String experience_event, String remarque) {
        this.id_participation = id_participation;
        this.id_event = id_event;
        this.id_user = id_user;
        this.date_participation = date_participation;
        this.role_participant = role_participant;
        this.depart_participant = depart_participant;
        this.contact = contact;
        this.experience_event = experience_event;
        this.remarque = remarque;
    }

    public int getId_participation() {
        return id_participation;
    }

    public void setId_participation(int id_participation) {
        this.id_participation = id_participation;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Date getDate_participation() {
        return date_participation;
    }

    public void setDate_participation(Date date_participation) {
        this.date_participation = date_participation;
    }

    public String getRole_participant() {
        return role_participant;
    }

    public void setRole_participant(String role_participant) {
        this.role_participant = role_participant;
    }

    public String getDepart_participant() {
        return depart_participant;
    }

    public void setDepart_participant(String depart_participant) {
        this.depart_participant = depart_participant;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getExperience_event() {
        return experience_event;
    }

    public void setExperience_event(String experience_event) {
        this.experience_event = experience_event;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id_participation=" + id_participation +
                ", id_event=" + id_event +
                ", id_user=" + id_user +
                ", date_participation=" + date_participation +
                ", role_participant='" + role_participant + '\'' +
                ", depart_participant='" + depart_participant + '\'' +
                ", contact='" + contact + '\'' +
                ", experience_event='" + experience_event + '\'' +
                ", remarque='" + remarque + '\'' +
                '}';
    }
}
