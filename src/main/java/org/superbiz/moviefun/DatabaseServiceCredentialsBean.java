package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DatabaseServiceCredentialsBean {

    public static DatabaseServiceCredentials createCredetial(@Value("VCAP_SERVICES") String vcapService) {
        return new DatabaseServiceCredentials(vcapService);
    }
}
