package domain;

public class DiscoveredURLCSpan {
	private String url;
	private String is_ok;
	
	public DiscoveredURLCSpan() {
		
	}
	
	public DiscoveredURLCSpan(String url, String is_ok) {
		super();
		this.url = url;
		this.is_ok = is_ok;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIs_ok() {
		return is_ok;
	}

	public void setIs_ok(String is_ok) {
		this.is_ok = is_ok;
	}
	
	
}
