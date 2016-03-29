package com.wisenut.openapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.wisenut.openapi.common.WNConstants;
import com.wisenut.openapi.common.WNProperties;
import com.wisenut.openapi.model.WNResultData;
import com.wisenut.openapi.util.StringUtil;

public class OpenAPIProvider {
	
	
	
	
	public WNResultData getOpenAPIResult(String provider, String query, int startPos, int pageNo, String sort){
		WNResultData data = new WNResultData();
		
		if(provider.equals("twitter")){
			// 이 팩토리인스턴스는 재이용가능하고 thread safe합니다.
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
									 new SimpleDateFormat("yyyyMMddHHmmss").format(status.getCreatedAt()),
									 status.getUser().getScreenName());
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
				
				request.addHeader("X-Naver-Client-Id", wnprop.getPropValues(WNConstants.NAVER_ID).getClient_id()); //발급받은ID
				request.addHeader("X-Naver-Client-Secret", wnprop.getPropValues(WNConstants.NAVER_ID).getClient_secret()); //발급받은 PW
				
				HttpResponse response = client.execute(request);

				InputStream is = response.getEntity().getContent();
				builder = factory.newDocumentBuilder();
				
				Document doc = builder.parse(is);
				
				Element element = doc.getDocumentElement();
				
				NodeList nodes = element.getChildNodes();
				
				for(int i=0; i<nodes.getLength(); i++){
					System.out.println(nodes.item(i).getTextContent());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			        
			
		}else if(provider.equals("daum")){
				
		}else if(provider.equals("facebook")){
			
		}
		return data;
	}
	
	
	public String getAccessToken(){
		String result = "";
		return result;
	}
	
	public static void main(String[] args){
		OpenAPIProvider provider = new OpenAPIProvider();
		WNResultData twitterData = provider.getOpenAPIResult("naver", "더불어민주당", 1, 50, "sim");
		
		System.out.println(twitterData.toString());
	}
}
