package ma.ensa;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Utilisateurs {


    private int id;
    private String nom;
    private String email;
    private int age;

    private int count = 0;

    public Utilisateurs(int id, String nom, String email, int age) {
        this.id = id;
        this.nom = nom;
        this.age = age;
        this.email = email;
    }

    public Utilisateurs(String nom, String email, int age) {
        this.id = ++count;
        this.nom = nom;
        this.age = age;
        this.email = email;
    }


    @Override
    public String toString() {
        return id + " - " + nom + " - " + age + " - " + email;
    }
}
