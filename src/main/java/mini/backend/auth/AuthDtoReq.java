package mini.backend.auth;

public class AuthDtoReq {
    private String username;
    private String password;

    public AuthDtoReq() {
    }

    public AuthDtoReq(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

