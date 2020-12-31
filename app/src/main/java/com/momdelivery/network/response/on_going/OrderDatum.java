package com.momdelivery.network.response.on_going;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDatum {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("mom_mobile")
    @Expose
    private String momMobile;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("productList")
    @Expose
    private List<ProductList> productList = null;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("id")
    @Expose
    private Integer id;

    String createdAt;
    String updatedAt;
    String image_name;
    String note;
    String mom_code;
    Double mom_longitude;
    Double mom_latitude;
    String mom_address;

    String firstName;
    String middleName;
    String lastName;





    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMomMobile() {
        return momMobile;
    }

    public void setMomMobile(String momMobile) {
        this.momMobile = momMobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note=note;
    }

    public String getMom_code() {
        return mom_code;
    }

    public void setMom_code(String mom_code) {
        this.mom_code = mom_code;
    }

    public Double getMom_longitude() {
        return mom_longitude;
    }

    public void setMom_longitude(Double mom_longitude) {
        this.mom_longitude = mom_longitude;
    }

    public Double getMom_latitude() {
        return mom_latitude;
    }

    public void setMom_latitude(Double mom_latitude) {
        this.mom_latitude = mom_latitude;
    }

    public String getMom_address() {
        return mom_address;
    }

    public void setMom_address(String mom_address) {
        this.mom_address = mom_address;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
