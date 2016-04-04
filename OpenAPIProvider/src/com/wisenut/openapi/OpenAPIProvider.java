package com.wisenut.openapi;

import com.wisenut.openapi.common.WNConstants;
import com.wisenut.openapi.model.WNResultData;
import com.wisenut.openapi.worker.DaumWorker;
import com.wisenut.openapi.worker.FacebookWorker;
import com.wisenut.openapi.worker.InstagramWorker;
import com.wisenut.openapi.worker.NaverWorker;
import com.wisenut.openapi.worker.TwitterWorker;

public class OpenAPIProvider {
	
	public WNResultData getOpenAPIResult(String provider, String query, int startPos, int pageNo, String sort){
		WNResultData data = new WNResultData();
		data.setProvider(provider);
		
		if(provider.equals("twitter")){
			TwitterWorker tWorker = new TwitterWorker();
			tWorker.search(query, startPos, pageNo, sort, data);
		}else if(provider.equals("naver")){
			NaverWorker nWorker = new NaverWorker();
			nWorker.search(query, startPos, pageNo, sort, data);
		}else if(provider.equals("daum")){
			DaumWorker dWorker = new DaumWorker();
			dWorker.search(query, startPos, pageNo, sort, data);
		}else if(provider.equals("facebook")){
			FacebookWorker fbWorker = new FacebookWorker("group");
			fbWorker.search(query, data);
		}else if(provider.equals("instagram")){
			InstagramWorker iWorker = new InstagramWorker();
			iWorker.search(query, 0, 100, "rank", data);
		}
		return data;
	}
	
	public static void main(String[] args){
		OpenAPIProvider provider = new OpenAPIProvider();
		WNResultData resultData = provider.getOpenAPIResult("instagram", "세월호 :)", 1, 100, WNConstants.METAINFO_BY_PROVIDER[WNConstants.DAUM_ID][WNConstants.SORT_BY_RANK]);
		
		System.out.println(resultData.toString());
	}
}
