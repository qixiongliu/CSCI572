package domain;

public class DownloadFileCSpan {
	private String url;
	private long fileSize;
	private int numOfOutlink;
	private String contentType;
	
	public DownloadFileCSpan() {
		
	}
	
	public DownloadFileCSpan(String url, long fileSize, int numOfOutlink, String contentType) {
		super();
		this.url = url;
		this.fileSize = fileSize;
		this.numOfOutlink = numOfOutlink;
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getNumOfOutlink() {
		return numOfOutlink;
	}

	public void setNumOfOutlink(int numOfOutlink) {
		this.numOfOutlink = numOfOutlink;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
}
