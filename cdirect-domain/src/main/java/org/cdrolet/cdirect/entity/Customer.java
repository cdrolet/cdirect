package org.cdrolet.cdirect.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by c on 4/17/16.
 */

@Entity
@Data
public class Customer {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "default", nullable = false)
    private Boolean defaultUser = false;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "subscriptionId")
    private Subscription subscription;
}
