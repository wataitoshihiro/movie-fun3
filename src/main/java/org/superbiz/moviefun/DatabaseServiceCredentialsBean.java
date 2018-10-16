package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class DatabaseServiceCredentialsBean {

    private DatabaseServiceCredentials credetials;

    @Autowired
    public DatabaseServiceCredentialsBean(@Value("${VCAP_SERVICES}") String vcapService) {
        this.credetials = new DatabaseServiceCredentials(vcapService);
    }

    @Bean
    public DatabaseServiceCredentials getCredentials() {
        return credetials;
    }
}
