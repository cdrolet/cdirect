package org.cdrolet.cdirect.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventDetailTest {

    @Test
    public void fromJson() throws Exception {
        String sample = " {" +
                "'type':'SUBSCRIPTION_ORDER'," +
                "'marketplace':{'partner':'ACME','baseUrl':'https://acme.appdirect.com'}," +
                "'applicationUuid':null," +
                "'flag':'STATELESS', " +
                "'creator':{" +
                "'uuid':'ec5d8eda-5cec-444d-9e30-125b6e4b67e2'," +
                "'openId':'https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2'," +
                "'email':'test-email+creator@appdirect.com'," +
                "'firstName':'DummyCreatorFirst'," +
                "'lastName':'DummyCreatorLast'," +
                "'language':'fr'," +
                "'address':null," +
                "'attributes':null" +
                "}," +
                "'payload':{" +
                "'user':null," +
                "'company':{'uuid':'d15bb36e-5fb5-11e0-8c3c-00262d2cda03'," +
                "'externalId':null," +
                "'name':'Example Company Name'," +
                "'email':'company-email@example.com'," +
                "'phoneNumber':'415-555-1212'," +
                "'website':'http://www.example.com'," +
                "'country':'CA'" +
                "}," +
                "'account':null,'addonInstance':null," +
                "'addonBinding':null," +
                "'order':{" +
                "'editionCode':'BASIC'," +
                "'addonOfferingCode':null," +
                "'pricingDuration':'MONTHLY'," +
                "'items':[{'unit':'USER'," +
                "'quantity':10}," +
                "{'unit':'MEGABYTE'," +
                "'quantity':15}]}," +
                "'notice':null," +
                "'configuration':{" +
                "'domain':'" +
                "mydomain'}}," +
                "'returnUrl':'https://www.appdirect.com/finishprocure?token=dummyOrder'," +
                "'links':[]}";
        assertThat(EventDetail.fromJson(sample)).isNotNull();
    }
}
