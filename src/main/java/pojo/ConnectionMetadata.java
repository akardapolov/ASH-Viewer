package pojo;


import lombok.Data;

@Data
public class ConnectionMetadata {
    private String connName;
    private String userName;
    private String password;
    private String url;
    private String jar;
    private String driver;
    private String profileName;

}
