package com.wisenut.openapi.worker;


import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.wisenut.openapi.model.WNResultData;
import com.wisenut.openapi.util.StringUtil;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Group;
import facebook4j.Location;
import facebook4j.Page;
import facebook4j.Place;
import facebook4j.ResponseList;
import facebook4j.User;

public class TwitterWorker {
	private Twitter twitter;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
	
	public TwitterWorker(){
		twitter = TwitterFactory.getSingleton();
	}
		
	public void search(String query, int startPos, int pageNo, String sort, WNResultData data){
		try {
			Query twitterQuery = new Query(query);
			QueryResult result = twitter.search(twitterQuery);
			
			twitterQuery.setCount(pageNo);
			
			data.setProvider("twitter");
			data.setTotalCount(result.getCount());
			data.setCurrentCount(result.getTweets().size());
			data.setStartPos(startPos);
			
			int pos = 0;
			do{
				if(pos+1 == startPos ){
					for (Status status : result.getTweets()) {
						 data.addItem("", StringUtil.removeSpecialCharacter(status.getText()), sdf.format(status.getCreatedAt()), status.getUser().getScreenName(), "");
					}
				}
				pos++;
			}while(result.hasNext());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
