package org.example;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;
    public static void main( String[] args ) {
        try {
            Scanner sc = new Scanner(System.in);
            emf = Persistence.createEntityManagerFactory("StoreTest");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: Add new client");
                    System.out.println("2: Add new product");
                    System.out.println("3: Show all clients");
                    System.out.println("4: Show all products");
                    System.out.println("5: Create order");
                    System.out.println("6: Show all orders");
                    System.out.println("7: Show clients orders");
                    System.out.print("-> ");
                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addNewClient(sc);
                            break;
                        case "2":
                            addNewProduct(sc);
                            break;
                        case "3":
                            showAllClients();
                            break;
                        case "4":
                            showAllProducts();
                            break;
                        case "5":
                            createNewOrder(sc);
                            break;
                        case "6":
                            showAllOrders();
                            break;
                        case "7":
                            showUserOrders(sc);
                            break;
                        default: return;
                    }
                }
            } finally {
              sc.close();
              em.close();
              emf.close();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void addNewClient (Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        Integer age = Integer.parseInt(sc.nextLine());

        em.getTransaction().begin();
        try {
            System.out.println("start");
            Client c = new Client(name,age);
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }
    private static void addNewProduct (Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter price: ");
        Integer price = Integer.parseInt(sc.nextLine());

        em.getTransaction().begin();
        try {
            Product p = new Product(name, price);
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void showAllClients () {
        Query query = em.createQuery("select x from Client x", Client.class);
        List<Client> list = (List<Client>) query.getResultList();
        for (Client c: list) {
            System.out.println(c.toString());
        }
    }

    private static void showAllProducts () {
        Query query = em.createQuery("select p from Product p", Product.class);
        List<Product> list = (List<Product>) query.getResultList();
        for (Product p : list) {
            System.out.println(p.toString());
        }
    }

    private static void createNewOrder (Scanner sc) {
        System.out.print("Enter you name: ");
        String name = sc.nextLine();
        TypedQuery<Client> typedQuery = em.createQuery("select c from Client c where c.name = :name", Client.class);
        typedQuery.setParameter("name", name);
        Client client = typedQuery.getSingleResult();
        System.out.print("Enter name of prodct: ");
        String pName = sc.nextLine();
        TypedQuery<Product> typedQuery1 = em.createQuery("select p from Product p where p.name = :pName", Product.class);
        typedQuery1.setParameter("pName", pName);
        Product product = typedQuery1.getSingleResult();
        em.getTransaction().begin();
        try {
            Order order = new Order(client,product);
            product.addOrder(order);
            client.addOrder(order);
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }
    private static void showAllOrders () {
        TypedQuery<Order> typedQuery = em.createQuery("select o from Order o", Order.class);
        List<Order> list = typedQuery.getResultList();
        for (Order o : list) {
            System.out.println(o.toString());
        }
    }
    private static void showUserOrders (Scanner sc) {
        System.out.print("Enter clients name: ");
        String name = sc.nextLine();
        TypedQuery<Client> typedQuery = em.createQuery("select c from Client c where c.name = :name", Client.class);
        typedQuery.setParameter("name", name);
        Client c = typedQuery.getSingleResult();
        List<Order> list = c.getOrders();
        for (Order o:list) {
            System.out.println(o.getProduct().getName());
        }
    }
}
