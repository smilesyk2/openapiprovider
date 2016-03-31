package com.wisenut.openapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.wisenut.icf.exception.CrawlingException;
import com.wisenut.icf.slave.crawler.runner.facebook.FacebookWorker;
import com.wisenut.openapi.common.WNConstants;
import com.wisenut.openapi.common.WNProperties;
import com.wisenut.openapi.model.WNResultData;
import com.wisenut.openapi.util.StringUtil;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;

public class OpenAPIProvider {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
	
	
	public WNResultData getOpenAPIResult(String provider, String query, int startPos, int pageNo, String sort){
		WNResultData data = new WNResultData();
		data.setProvider(provider);
		
		if(provider.equals("twitter")){
			Twitter twitter = TwitterFactory.getSingleton();
		    Query twitterQuery = new Query(query);
		    twitterQuery.setCount(pageNo);

		    QueryResult result;
			try {
				result = twitter.search(twitterQuery);
				
				data.setProvider("twitter");
				data.setTotalCount(result.getCount());
				data.setCurrentCount(result.getTweets().size());
				data.setStartPos(startPos);
				
				int pos = 0;
				do{
					if(pos+1 == startPos ){
						for (Status status : result.getTweets()) {
							 data.addItem("NO TITLE",
									 StringUtil.removeSpecialCharacter(status.getText()),
									 sdf.format(status.getCreatedAt()),
									 status.getUser().getScreenName(),
									 "{LINK}");
						}
					}
					pos++;
				}while(result.hasNext());
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(provider.equals("naver")){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			WNProperties wnprop = WNProperties.getInstance(WNConstants.NAVER_ID);
			String strUrl = "https://openapi.naver.com/v1/search/news.xml?"
					+ "query=" + query + "&"
					+ "display=" + pageNo + "&"
					+ "start=" + startPos + "&"
					+ "sort=" + sort;
			
			try {
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet(strUrl);
				
				request.addHeader("X-Naver-Client-Id", wnprop.getPropValues().getClient_id()); //발급받은ID
				request.addHeader("X-Naver-Client-Secret", wnprop.getPropValues().getClient_secret()); //발급받은 PW
				
				HttpResponse response = client.execute(request);

				InputStream is = response.getEntity().getContent();
				builder = factory.newDocumentBuilder();
				
				Document doc = builder.parse(is);
				data.setTotalCount(Integer.parseInt(doc.getElementsByTagName("total").item(0).getTextContent()));
				data.setCurrentCount(Integer.parseInt(doc.getElementsByTagName("display").item(0).getTextContent()));
				data.setStartPos(Integer.parseInt(doc.getElementsByTagName("start").item(0).getTextContent()));
				
				NodeList itemList = doc.getElementsByTagName("item");
				
				for(int i=0; i<itemList.getLength(); i++){
					Node item = itemList.item(i);
					
					if (item.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) item;
						data.addItem(eElement.getElementsByTagName("title").item(0).getTextContent(),
								StringUtil.removeSpecialCharacter(eElement.getElementsByTagName("description").item(0).getTextContent()),
								eElement.getElementsByTagName("pubDate").item(0).getTextContent(),
								"",
								eElement.getElementsByTagName("link").item(0).getTextContent());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			        
			
		}else if(provider.equals("daum")){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			WNProperties wnprop = WNProperties.getInstance(WNConstants.DAUM_ID);
			String strUrl = "https://apis.daum.net/search/web?"
					+ "apikey=" + wnprop.getPropValues().getClient_id() + "&"
					+ "q=" + query + "&"
					+ "result=" + pageNo + "&"
					+ "pageno=" + startPos + "&"
					+ "sort=" + sort + "&"
					+ "output=xml";
			
			try {
				//API 요청 및 반환
				URL url = new URL(strUrl);
				URLConnection conn = url.openConnection();
				builder = factory.newDocumentBuilder();
				Document doc = builder.parse(conn.getInputStream());

				data.setTotalCount(Integer.parseInt(doc.getElementsByTagName("totalCount").item(0).getTextContent()));
				data.setCurrentCount(Integer.parseInt(doc.getElementsByTagName("result").item(0).getTextContent()));
				data.setStartPos(startPos);
				
				NodeList itemList = doc.getElementsByTagName("item");
				
				for(int i=0; i<itemList.getLength(); i++){
					Node item = itemList.item(i);
					
					if (item.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) item;
						data.addItem(eElement.getElementsByTagName("title").item(0).getTextContent(),
								StringUtil.removeSpecialCharacter(eElement.getElementsByTagName("description").item(0).getTextContent()),
								eElement.getElementsByTagName("pubDate").item(0).getTextContent(),
								"",
								eElement.getElementsByTagName("link").item(0).getTextContent());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(provider.equals("facebook")){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			WNProperties wnprop = WNProperties.getInstance(WNConstants.NAVER_ID);
			String strUrl = "https://graph.facebook.com/search?"
					+ "q=" + query + "&"
					+ "type=event&"
					+ "access_token=CAAC3d9FI3NUBAAQwRJUwKRz778bjkPnVKbz7r7ZBrtwElDNlaLDJN87XbJ7K91GQOwYK4ZATXaZChxKUGVi57gIdBZAAJoZB1T4xuY45LszI5ZCcMV6PThaMqK0vG7IxLCMxatSXGKtXsotpC6qqBwf5zMKRGyfr6PN2B0pOCffNmZAhOPvmOlT";
			
			try {
				//API 요청 및 반환
				URL url = new URL(strUrl);
				URLConnection conn = url.openConnection();
				builder = factory.newDocumentBuilder();
				
				System.out.println("### facebook result : " + IOUtils.toString(conn.getInputStream(), "UTF-8"));
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			/*Facebook facebook = new FacebookFactory().getInstance();
			try {
				ResponseList<Post> results = facebook.searchPosts(query);
				
				data.setProvider("facebook");
				data.setTotalCount(results.getCount());
				for (int i=0; i<results.getCount(); i++){
					Post thisPost = results.get(i);
					data.addItem(thisPost.getName(), thisPost.getDescription(), sdf.format(thisPost.getCreatedTime()), thisPost.getId(), thisPost.getLink().toString());
				}
			} catch (FacebookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
		return data;
	}
	
	
	public String getAccessToken(){
		String result = "";
		return result;
	}
	
	public static void main(String[] args){
		OpenAPIProvider provider = new OpenAPIProvider();
		WNResultData twitterData = provider.getOpenAPIResult("facebook", "%EC%84%A0%EA%B1%B0", 1, 50, WNConstants.METAINFO_BY_PROVIDER[WNConstants.FACEBOOK_ID][WNConstants.SORT_BY_RANK]);
		
		System.out.println(twitterData.toString());
	}
}
