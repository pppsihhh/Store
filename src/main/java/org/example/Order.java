package org.example;

import javax.persistence.*;

@Entity
@Table (name = "Orders")
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn (name = "Client_id")
    private Client client;
    @ManyToOne
    @JoinColumn (name = "Product_id")
    private Product product;

    public Order () {}
    public Order (Client client, Product product) {
        this.client = client;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "clietnt - " + this.client.getName() + " bought " + "product - " + this.product.getName();
    }
}
