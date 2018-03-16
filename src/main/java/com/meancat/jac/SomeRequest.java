package com.meancat.jac;

public class SomeRequest {

    private String storefront = "test_store";
    private long wgid = 0;
    private String language = "en";
    private String country = "US";

    public SomeRequest() {}

    public String getStorefront() {
        return storefront;
    }

    public void setStorefront(String storefront) {
        this.storefront = storefront;
    }

    public long getWgid() {
        return wgid;
    }

    public void setWgid(long wgid) {
        this.wgid = wgid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
