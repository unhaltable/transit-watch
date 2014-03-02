package ca.cryptr.transit_watch.weather;

public class City {

    private String code, nameEn, province;

    public City() {}

    public City(String code, String nameEn,String province) {
        this.code = code;
        this.nameEn = nameEn;
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

}
