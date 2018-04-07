package cn.qixiongliu.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractingLingks {
	public static void main(String[] args) throws Exception {
		String dirPath = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/DataSet/NBC_News/crawl_data/";
		String csvFilePath = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/DataSet/NBC_News/UrlToHtml_NBCNews.csv";
		
		Map<String, String> IdUrlMap = new HashMap<String, String>(); 
		FileReader in = new FileReader(csvFilePath);
		BufferedReader br = new BufferedReader(in);
		String line = null;
		while ((line = br.readLine()) != null) {
			String id = line.split(",")[0];
			String url = line.split(",")[1];
			IdUrlMap.put(url, id);
		}
		br.close();
		
		String outputFileName = "edgesList.txt";
		String outputFilepath = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/Answer/" + outputFileName;
		PrintWriter pw = new PrintWriter(new FileWriter(outputFilepath));
		
		File directory = new File(dirPath);
		File[] htmlFiles = directory.listFiles();
		for (File file: htmlFiles) {
			File filePath = new File(dirPath + file.getName());
			Document doc = Jsoup.parse(filePath, "UTF-8", "https://www.nbcnews.com/");
			Elements links = doc.select("a[href]");
			
			for (Element link: links) {
				String outUrl = link.attr("abs:href").trim();
				if (IdUrlMap.containsKey(outUrl)) {
					String edge = file.getName() + " " + IdUrlMap.get(outUrl);
					pw.println(edge);
					pw.flush();
				}
			}
		}
		pw.close();
	}
}
