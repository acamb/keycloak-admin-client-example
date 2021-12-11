import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Example client for keycloak java API to generate a new user with a temp password
 */
public class MainClass {

    public final static String SERVER_URL = "http://localhost:8100/auth";
    public final static String REALM = "test";
    public final static String USERNAME = "admin";
    public final static String PASSWORD = "admin";
    public final static String CLIENT_ID = "admin-cli";
    public final static String CLIENT_SECRET = "137f1cec-d856-42b3-b130-7f0a27f02258";

    public static void main(String[] args){
        Keycloak keycloak = Keycloak.getInstance(
                SERVER_URL,
                REALM,
                USERNAME,
                PASSWORD,
                CLIENT_ID,
                CLIENT_SECRET
        );
        GroupRepresentation userGroup;
        userGroup = keycloak.realm(REALM).groups()
                .groups()
                .stream()
                .filter(g -> g.getName().equals("test-client-users")) //already existing group, using the name property only for example purpose
                .findFirst().orElseThrow();

        UserRepresentation user = new UserRepresentation();
        user.setUsername("testUser");
        user.setFirstName("testUser");
        user.setEnabled(true);
        //to add the user to group(s) use the group path
        user.setGroups(List.of(userGroup.getPath()));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getUsername());
        credentialRepresentation.setTemporary(true);

        user.setCredentials(List.of(credentialRepresentation));

        keycloak.realm(REALM).users().create(user);


    }
}
