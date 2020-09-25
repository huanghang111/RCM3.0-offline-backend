package com.bosch.rcm.config.dbmigrations;

import com.bosch.rcm.domain.Authority;
import com.bosch.rcm.domain.DataType;
import com.bosch.rcm.domain.Method;
import com.bosch.rcm.domain.User;
import com.bosch.rcm.security.AuthoritiesConstants;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

@ChangeLog(order = "001")
public class InitialSetupMigration {
    private static final String BOOLEAN = "BOOLEAN";
    private static final String NUMERIC = "NUMERIC";

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);

        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        Authority rexrothAuthority = new Authority();
        rexrothAuthority.setName(AuthoritiesConstants.REXROTH);


        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
        mongoTemplate.save(rexrothAuthority);

    }

    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);

        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        Authority rexrothAuthority = new Authority();
        rexrothAuthority.setName(AuthoritiesConstants.REXROTH);

        User rexrothUser = new User();
        rexrothUser.setId("user-rexroth");
        rexrothUser.setLoginName("rexroth");
        rexrothUser.setPassword("$2a$10$mPMNq7kbJ9mH7isM8IuVnOwxrUnLbR23R.exgIhx0q0TtAjQJcqJu");
        rexrothUser.setAccountName("Rexroth Account");
        rexrothUser.setCreatedBy(rexrothUser.getLoginName());
        rexrothUser.setCreatedDate(Instant.now());
        rexrothUser.getAuthorities().add(rexrothAuthority);
        mongoTemplate.save(rexrothUser);

        User endUser = new User();
        endUser.setId("user-user");
        endUser.setLoginName("user");
        endUser.setPassword("$2a$10$K2wvb9SJIW9SpVL9frAjMeIYwkH8bv2fWmFlGankYaL4MXqUQMgZe");
        endUser.setAccountName("Anonymous");
        endUser.setCreatedBy(rexrothUser.getLoginName());
        endUser.setCreatedDate(Instant.now());
        endUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(endUser);

        User adminUser = new User();
        adminUser.setId("user-admin");
        adminUser.setLoginName("admin");
        adminUser.setPassword("$2a$10$Nr7WS8kDdzVrRD0IYrDp8eB68c8nFk2zYVNol5CIS7Ja.fVv.K7Fa");
        adminUser.setAccountName("admin");
        adminUser.setCreatedBy(rexrothUser.getLoginName());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        mongoTemplate.save(adminUser);
    }

    @ChangeSet(order = "03", author = "initiator", id = "03-addMethod")
    public void addMethods(MongoTemplate mongoTemplate) {
        Method addNormal = new Method();
        addNormal.setName("+");
        addNormal.setValue("add");
        addNormal.setType(NUMERIC);
        mongoTemplate.save(addNormal);

        Method subNormal = new Method();
        subNormal.setName("-");
        subNormal.setValue("sub");
        subNormal.setType(NUMERIC);
        mongoTemplate.save(subNormal);

        Method multiplyNormal = new Method();
        multiplyNormal.setName("*");
        multiplyNormal.setValue("multi");
        multiplyNormal.setType(NUMERIC);
        mongoTemplate.save(multiplyNormal);

        Method divideNormal = new Method();
        divideNormal.setName("/");
        divideNormal.setValue("div");
        divideNormal.setType(NUMERIC);
        mongoTemplate.save(divideNormal);

        Method mult10Normal = new Method();
        mult10Normal.setName("x10");
        mult10Normal.setValue("x10");
        mult10Normal.setType(NUMERIC);
        mongoTemplate.save(mult10Normal);

        Method mult100Normal = new Method();
        mult100Normal.setName("x100");
        mult100Normal.setValue("x100");
        mult100Normal.setType(NUMERIC);
        mongoTemplate.save(mult100Normal);

        Method divide10Normal = new Method();
        divide10Normal.setName("x0.1");
        divide10Normal.setValue("x0.1");
        divide10Normal.setType(NUMERIC);
        mongoTemplate.save(divide10Normal);

        Method divide100Normal = new Method();
        divide100Normal.setName("x0.01");
        divide100Normal.setValue("x0.01");
        divide100Normal.setType(NUMERIC);
        mongoTemplate.save(divide100Normal);

        Method lessFault = new Method();
        lessFault.setName("<");
        lessFault.setValue("lesser");
        lessFault.setType(NUMERIC);
        mongoTemplate.save(lessFault);

        Method greatFault = new Method();
        greatFault.setName(">");
        greatFault.setValue("greater");
        greatFault.setType(NUMERIC);
        mongoTemplate.save(greatFault);

        Method equalFault = new Method();
        equalFault.setName("==");
        equalFault.setValue("equal");
        equalFault.setType(NUMERIC);
        mongoTemplate.save(equalFault);

        Method notequalFault = new Method();
        notequalFault.setName("!=");
        notequalFault.setValue("not_equal");
        notequalFault.setType(NUMERIC);
        mongoTemplate.save(notequalFault);

        Method andFault = new Method();
        andFault.setName("AND");
        andFault.setValue("AND");
        andFault.setType(BOOLEAN);
        mongoTemplate.save(andFault);

        Method orFault = new Method();
        orFault.setName("OR");
        orFault.setValue("OR");
        orFault.setType(BOOLEAN);
        mongoTemplate.save(orFault);

        Method notFault = new Method();
        notFault.setName("NOT");
        notFault.setValue("NOT");
        notFault.setType(BOOLEAN);
        mongoTemplate.save(notFault);

    }

    @ChangeSet(order = "05", author = "initiator", id = "05-addDataType")
    public void addDataType(MongoTemplate mongoTemplate) {
        DataType INT16Type = new DataType("INT16");
        mongoTemplate.save(INT16Type);
        DataType INT32Type = new DataType("INT32");
        mongoTemplate.save(INT32Type);
        DataType FLOATType = new DataType("FLOAT");
        mongoTemplate.save(FLOATType);
        DataType BOOLType = new DataType("BOOL");
        mongoTemplate.save(BOOLType);
        DataType INTENSIVE = new DataType("INTENSIVE");
        mongoTemplate.save(INTENSIVE);
        DataType CATALOG = new DataType("CATALOG");
        mongoTemplate.save(CATALOG);
    }
}
