package service;

import org.cdrolet.cdirect.CDirectApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by c on 4/16/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CDirectApplication.class)
@WebIntegrationTest("server.port.0")
public class CDirectApplicationTest {

    @Autowired
    private WebApplicationContext context;

    @Test
    public void contextLoads() {

    }

}
