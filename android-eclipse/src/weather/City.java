package weather;

public class City {

	private String _code, _nameEn, _nameFr, _province;

	public City() {

	}

	public City(String code, String nameEn, String nameFr, String province) {
		this._code = code;
		this._nameEn = nameEn;
		this._nameFr = nameFr;
		this._province = province;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		this._code = code;
	}

	public String getNameEn() {
		return _nameEn;
	}

	public void setNameEn(String nameEn) {
		this._nameEn = nameEn;
	}

	public String getNameFr() {
		return _nameFr;
	}

	public void setNameFr(String nameFr) {
		this._nameFr = nameFr;
	}

	public String getProvince() {
		return _province;
	}

	public void setProvince(String province) {
		this._province = province;
	}

}
