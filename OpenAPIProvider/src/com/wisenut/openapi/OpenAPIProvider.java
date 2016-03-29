package com.wisenut.openapi;

import java.text.SimpleDateFormat;

import org.apache.http.client.HttpClient;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.wisenut.openapi.model.ResultData;

public class OpenAPIProvider {
	
	
	
	
	public ResultData getOpenAPIResult(String provider, String query, int startPos, int pageNo, String sort){
		ResultData data = new ResultData();
		
		if(provider.equals("twitter")){
			// 이 팩토리인스턴스는 재이용가능하고 thread safe합니다.
		    Twitter twitter = TwitterFactory.getSingleton();
		    
		    Query twitterQuery = new Query(query);

		    QueryResult result;
			try {
				result = twitter.search(twitterQuery);
				
				data.setTotalCount(result.getCount());

				for (Status status : result.getTweets()) {
					 data.addItem("NO TITLE", status.getText(), new SimpleDateFormat("yyyyMMddHHmmss").format(status.getCreatedAt()), status.getUser().getScreenName());
				}
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(provider.equals("naver")){
			
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
		provider.getOpenAPIResult("twitter", "더불어민주당", 1, 10, "recent");
		System.out.println();
	}
}
