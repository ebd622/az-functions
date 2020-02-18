package net.abc;

import java.util.Objects;

public class BillingItem {

    private String id;
    private String RowKey;
    private String name;
    private String productName;
    private double price;
    private double totalAmount;

    public BillingItem() {
    }

    public BillingItem(String id, String rowKey, String name, String productName, double price) {
        this.id = id;
        this.RowKey = rowKey;
        this.name = name;
        this.productName = productName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRowKey() {
        return RowKey;
    }

    public void setRowKey(String rowKey) {
        RowKey = rowKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillingItem)) return false;
        BillingItem that = (BillingItem) o;
        return Objects.equals(id, that.id) &&
                Double.compare(that.price, price) == 0 &&
                Double.compare(that.totalAmount, totalAmount) == 0 &&
                Objects.equals(RowKey, that.RowKey) &&
                Objects.equals(name, that.name) &&
                Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, RowKey, name, productName, price, totalAmount);
    }

    @Override
    public String toString() {
        return "BillingItem{" +
                "id='" + id + '\'' +
                ", RowKey='" + RowKey + '\'' +
                ", name='" + name + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

