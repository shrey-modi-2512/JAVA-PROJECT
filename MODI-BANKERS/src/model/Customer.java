package model;

import java.io.Serializable;

public class Customer implements Cloneable, Serializable {

private static final long serialVersionUID = 1L;

private String customerId;
private String name;
private Address address;

public Customer(String customerId, String name, Address address) {
    this.customerId = customerId;
    this.name = name;
    this.address = address;
}

public String getCustomerId() {
    return customerId;
}

public String getName() {
    return name;
}

public Address getAddress() {
    return address;
}

public void setName(String name) {
    this.name = name;
}

@Override
public Customer clone() {
    try {
        Customer copy = (Customer) super.clone();

        if (address != null) {
            copy.address = address.clone();
        }

        return copy;

    } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
    }
}

@Override
public String toString() {
    return "Customer ID: " + customerId
            + ", Name: " + name
            + ", Address: " + address;
}

public static class Address implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String city;
    private String state;
    private String pinCode;

    public Address(String city, String state, String pinCode) {
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPinCode() {
        return pinCode;
    }

    @Override
    public Address clone() {
        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return city + ", " + state + " - " + pinCode;
    }
}

}
