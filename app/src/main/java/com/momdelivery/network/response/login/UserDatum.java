
package com.momdelivery.network.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDatum {

    @SerializedName("aadharNo")
    @Expose
    private String aadharNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("driveringLicense")
    @Expose
    private String driveringLicense;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("vehicleNo")
    @Expose
    private String vehicleNo;
    String image;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDriveringLicense() {
        return driveringLicense;
    }

    public void setDriveringLicense(String driveringLicense) {
        this.driveringLicense = driveringLicense;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

}
