package org.cdrolet.cdirect.entity;

import com.google.common.collect.Lists;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by c on 4/15/16.
 */
@Entity
@Data
public class Subscription {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "edition", nullable = false)
    private String editionCode;

    @Column(name = "duration", nullable = false)
    private String pricingDuration;

    @OneToMany(mappedBy = "subscription", orphanRemoval = true, cascade = { CascadeType.ALL })
    private List<Customer> customers = Lists.newArrayList();

    public void addCustomer(Customer customer) {
        customer.setSubscription(this);
        customers.add(customer);
    }

    public static Subscription from(Subscription previous,
                                    Subscription newOne) {

        Subscription sub = new Subscription();

        sub.setActive(newOne.getActive() == null
                ? previous.getActive()
                : newOne.getActive());

        sub.setEditionCode(newOne.getEditionCode() == null
                ? previous.getEditionCode()
                : newOne.getEditionCode());

        sub.setPricingDuration(newOne.getPricingDuration() == null
                ? previous.getPricingDuration()
                : newOne.getPricingDuration());

        sub.setCustomers(newOne.getCustomers() == null
                ? previous.getCustomers()
                : newOne.getCustomers());

        sub.setId(newOne.getId());

        return sub;
    }

}
