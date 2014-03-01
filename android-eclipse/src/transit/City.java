package transit;

public class City {

	private String code, nameEn, nameFr, province;

	public City() {

	}

	public City(String code, String nameEn, String nameFr, String province) {
		this.code = code;
		this.nameEn = nameEn;
		this.nameFr = nameFr;
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

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
