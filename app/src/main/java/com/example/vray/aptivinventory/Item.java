package com.example.vray.aptivinventory;

public class Item {
  private String name;
  private double price;
  private int quantity;
  private int owner_id;
  private int itemid;

  private String utag;
  private int ram;
  private int hdd;
  private double cpu;

  public Item(String name, double price, int quantity, int owner_id) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.owner_id = owner_id;
  }

  public Item(int itemid, String name, double price, int quantity, int owner_id) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.itemid = itemid;
    this.owner_id = owner_id;
  }

  public Item(int itemid, String name, double price, int quantity, int owner_id, int ram, String utag, int hdd, double cpu) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.itemid = itemid;
    this.owner_id = owner_id;
    this.utag = utag;
    this.ram = ram;
    this.hdd = hdd;
    this.cpu = cpu;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) { this.price = price; }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getOwner_id() {
    return owner_id;
  }

  public void setOwner_id(int owner_id) {
    this.owner_id = owner_id;
  }

  public int getItemid() {
    return itemid;
  }

  public void setItemid(int itemid) {
    this.itemid = itemid;
  }

}
