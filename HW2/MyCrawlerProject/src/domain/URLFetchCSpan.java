package domain;

public class URLFetchCSpan {
	private String url;
	private int httpStatusCode;
	
	public URLFetchCSpan() {
		
	}
	
	public URLFetchCSpan(String url, int httpStatusCode) {
		this.url = url;
		this.httpStatusCode = httpStatusCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

}
