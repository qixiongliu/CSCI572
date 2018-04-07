import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import domain.DiscoveredURLCSpan;
import domain.DownloadFileCSpan;
import domain.URLFetchCSpan;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private final static Pattern MATCH = Pattern.compile(".*(\\.(html|doc|pdf|gif|jpe?g|png|bmp|tiff?))$");
//	private final static Pattern END_NO_EXTENSION = Pattern.compile(".*(\\.(html|doc|pdf|gif|jpe?g|png|bmp|tiff?))$");
	private static final Pattern FILTERS = Pattern.compile(
	        ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" +
	        "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	public static List<URLFetchCSpan> fetchUrlList = new ArrayList<URLFetchCSpan>();
	public static List<DownloadFileCSpan> downloadFileList = new ArrayList<DownloadFileCSpan>();
	public static List<DiscoveredURLCSpan> visitUrlList = new ArrayList<DiscoveredURLCSpan>();
	public static Set<String> visitUrlSet = new HashSet<String>();
	
	// fetch_count
	public static int fetch_succeeded = 0;
	public static int fetch_failed_or_aborted = 0;
	// outgoing_url_count
	public static int total_urls = 0;
	public static int unique_url = 0;
	public static int unique_url_within = 0;
	public static int unique_url_outside = 0;
	// statusCode_count
	public static int codeCount_200 = 0;
	public static int codeCount_301 = 0;
	public static int codeCount_302 = 0;
	public static int codeCount_307 = 0;
	public static int codeCount_404 = 0;
	public static int codeCount_410 = 0;
	// fileSize_count
	public static int fileCount_less_than_1KB = 0;
	public static int fileCount_between_1KB_and_10KB = 0;
	public static int fileCount_between_10KB_and_100KB = 0;
	public static int fileCount_between_100KB_and_1024KB = 0;
	public static int fileCount_more_than_1MB = 0;
	// contentType_count
	public static int contentType_html = 0;
	public static int contentType_gif = 0;
	public static int contentType_tif = 0;
	public static int contentType_jpeg = 0;
	public static int contentType_png = 0;
	public static int contentType_pdf = 0;
	
	public void contentTypeCount(String contentType) {
		if (contentType.equals("text/html")) {
			contentType_html++;
		}
		if (contentType.equals("image/gif")) {
			contentType_gif++;
		}
		if (contentType.equals("image/tif")) {
			contentType_tif++;
		}
		if (contentType.equals("image/jpeg")) {
			contentType_jpeg++;
		}
		if (contentType.equals("image/png")) {
			contentType_png++;
		}
		if (contentType.equals("application/pdf")) {
			contentType_pdf++;
		}
	}
	
	public void fileSizeCount(long size) {
		if (size < 1) {
			fileCount_less_than_1KB++;
		} else if (size >= 1 && size < 10) {
			fileCount_between_1KB_and_10KB++;
		} else if (size >= 10 && size < 100) {
			fileCount_between_10KB_and_100KB++;
		} else if (size >= 10 && size < 1024) {
			fileCount_between_100KB_and_1024KB++;
		} else {
			fileCount_more_than_1MB++;
		}
	}
	
	public void statusCodeCount(int StatusCode) {
		switch(StatusCode) {
		case 200:
			codeCount_200++;
			break;
		case 301:
			codeCount_301++;
			break;
		case 302:
			codeCount_302++;
			break;
		case 307:
			codeCount_307++;
			break;
		case 404:
			codeCount_404++;
			break;
		case 410:
			codeCount_410++;
			break;
		default:
			break;
		}
	}
	
	/**
     * This function is called once the header of a page is fetched. It can be
     * overridden by sub-classes to perform custom logic for different status
     * codes. For example, 404 pages can be logged, etc.
     *
     * @param webUrl WebUrl containing the statusCode
     * @param statusCode Html Status Code number
     * @param statusDescription Html Status COde description
     */
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		fetchUrlList.add(new URLFetchCSpan(webUrl.getURL().toLowerCase().replace(",", "_"), statusCode));
		System.out.println("No." + fetchUrlList.size());
		statusCodeCount(statusCode);
		if (statusCode / 100 == 3 || statusCode / 100 == 4 || statusCode / 100 == 5) {
			fetch_failed_or_aborted++;
		} else {
			fetch_succeeded++;
		}
	}
	/**
	* This method receives two parameters. The first parameter is the page
	* in which we have discovered this new url and the second parameter is
	* the new url. You should implement this function to specify whether
	* the given url should be crawled or not (based on your 	crawling logic).
	* In this example, we are instructing the crawler to ignore urls that
	* have css, js, git, ... extensions and to only accept urls that start
	* with "http://www.viterbi.usc.edu/". In this case, we didn't need the
	* referringPage parameter to make the decision.
	*/
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		
		//System.out.println(referringPage.getStatusCode() + "******");
	    String href = url.getURL().toLowerCase();
	    
	    if (href.startsWith("https://www.c-span.org/") || href.startsWith("http://www.c-span.org/") 
     		   || href.startsWith("https://c-span.org/") || href.startsWith("http://c-span.org/")) {
	    		visitUrlList.add(new DiscoveredURLCSpan(href.replace(",", "_"), "OK"));
	    		if (!visitUrlSet.contains(href.replace(",", "_"))) {
	    			unique_url_within++;
	    		}
	    } else {
		    	visitUrlList.add(new DiscoveredURLCSpan(href.replace(",", "_"), "N_OK"));
		    	if (!visitUrlSet.contains(href.replace(",", "_"))) {
	    			unique_url_outside++;
	    		}
	    }
	    if (!visitUrlSet.contains(href.replace(",", "_"))) {
			unique_url++;
			visitUrlSet.add(href.replace(",", "_"));
		}
	    
//	    if (href.startsWith("https://www.c-span.org/")) {
//		    		visitUrlList.add(new DiscoveredURLCSpan(href.replace(",", "_"), "OK"));
//		} else {
//			    	visitUrlList.add(new DiscoveredURLCSpan(href.replace(",", "_"), "N_OK"));
//		}
	    
	    if (FILTERS.matcher(href).matches()) {
	    		return false;
	    }
//	    if (!href.startsWith("https://www.c-span.org/") && !href.startsWith("http://www.c-span.org/") 
//	     		   && !href.startsWith("https://c-span.org/") && !href.startsWith("http://c-span.org/")) {
//	    		return false;
//	    }
//	    if(MATCH.matcher(href).matches() || ) {
//	    		return true;
//	    }
	    return href.startsWith("https://www.c-span.org/") || href.startsWith("http://www.c-span.org/") 
     		   || href.startsWith("https://c-span.org/") || href.startsWith("http://c-span.org/");
//	    return MATCH.matcher(href).matches() && href.startsWith("http://www.nbcnews.com/");
//	    return MATCH.matcher(href).matches() && (href.startsWith("https://www.c-span.org/") || href.startsWith("http://www.c-span.org/") 
//	        		   || href.startsWith("https://c-span.org/") || href.startsWith("http://c-span.org/"));
	}
	
	/**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
	@Override
	public void visit(Page page) {
	    String url = page.getWebURL().getURL();
	    int httpStatusCode = page.getStatusCode();
	    
//	    fetchUrlList.add(new URLFetchCSpan(url.replace(",", "_"), httpStatusCode));
	    
	    long fileSizeKB = page.getContentData().length / 1024;
	    	String contentType = page.getContentType().split(";")[0];
	    	int numOfOutlink = page.getParseData().getOutgoingUrls().size();
	    	total_urls += numOfOutlink;
	    downloadFileList.add(new DownloadFileCSpan(url.replace(",", "_"), fileSizeKB, numOfOutlink, contentType));
	    	
	    fileSizeCount(fileSizeKB);
	    contentTypeCount(contentType);
	    	
	    	//test
	    System.out.println("URL: " + url);
//	    System.out.println("****" + page.getContentData().length);
	    System.out.println("HttpStatusCode: " + page.getStatusCode());
//	    
//	    if (page.getParseData() instanceof HtmlParseData) {
//	        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//	        String text = htmlParseData.getText();
//	        String html = htmlParseData.getHtml();
//	        Set<WebURL> links = htmlParseData.getOutgoingUrls();
//	        
//	        System.out.println("Text length: " + text.length());
//	        System.out.println("Html length: " + html.length());
//	        System.out.println("Number of outgoing links: " + links.size());
//	    } 
	}
}
