package in.goflo.laberintoc.Model;

/**
 * Created by Anisha Mascarenhas on 25-04-2018.
 */

public class UserDetails {

    public UserDetails(String email, String name) {
        this.email = email;
        this.name = name;
    }

    private String email, name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
