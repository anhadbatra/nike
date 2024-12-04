package com.nike.nike.Models;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "products")
public class Product {

    @Id
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String productname;

    private int price;

    private String description;
    private String profileImagePath;
    private String bannerImagePath;
    private String thumbnailImagePath;

    private LocalDate productdate;

    
    // Save category as a string in the database
   

    public Product() {}

    public Product(String productname, int price, String description, Category category) {
        this.productname = productname;
        this.price = price;
        this.description = description;
    }
    @PrePersist
    protected void onCreate() {
        this.productdate = LocalDate.now(); // Set to current system date
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productname;
    }

    public void setProductName(String productname) {
        this.productname = productname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LocalDate getProductdate() {
        return productdate;
    }

    public void setProductdate(LocalDate productdate) {
        this.productdate = productdate;
    }
    public String getProfileImagePath() {
        return profileImagePath;
    }
    
    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
    
    public String getBannerImagePath() {
        return bannerImagePath;
    }
    
    public void setBannerImagePath(String bannerImagePath) {
        this.bannerImagePath = bannerImagePath;
    }
    
    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }
    
    public void setThumbnailImagePath(String thumbnailImagePath) {
        this.thumbnailImagePath = thumbnailImagePath;
    }
    
}
