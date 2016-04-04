package com.wisenut.openapi.worker;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lithium.instagram4j.Instagram4j;
import com.lithium.instagram4j.InstagramError;
import com.lithium.instagram4j.model.InstagramAccessTokenResponse;
import com.lithium.instagram4j.model.InstagramSingleObjectResponse;
import com.lithium.instagram4j.model.InstagramTags;
import com.wisenut.openapi.common.WNConstants;
import com.wisenut.openapi.common.WNProperties;
import com.wisenut.openapi.model.WNResultData;
import com.wisenut.openapi.util.StringUtil;

public class InstagramWorker {
	//private InstagramTags instagram;
	//private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private WNProperties wnprop; 
	private String instagramUrl = "https://api.instagram.com/oauth/access_token";
	private String redirectUri = "https://api.instagram.com/";
	
	public InstagramWorker(){
		wnprop = WNProperties.getInstance(WNConstants.INSTAGRAM_ID);
		/*BufferedReader rd = null;
		
		*/
	}
		
	public void search(String query, int startPos, int pageNo, String sort, WNResultData data){
		
		

		 
	}
	
	public void getAccessToken(){
		//code ::: b99c69fb840d4763b6976842dfa4ebe5
		String param = "client_id=" + wnprop.getPropValues().getClient_id() + "&"
				+ "client_secret=" + wnprop.getPropValues().getClient_secret() + "&"
				+ "grant_type=authorization_code" + "&"
				+ "redirect_uri=http://61.82.137.95:7800/sample/normal/auth.jsp" + "&"
				+ "code=8b44557c6c764c6eadc26868ba085fbd";
		
		System.out.println("response :::: " + getResponse(instagramUrl, param));
		
	}
	
	public void getCode(){
		//https://www.instagram.com/oauth/authorize/?client_id=e6e8412a54ee43d0ac60448d71a4cb76&redirect_uri=https://api.instagram.com/&response_type=code
		String url = "https://www.instagram.com/oauth/authorize/";
		String param = "client_id="+wnprop.getPropValues().getClient_id()
					//+"&client_secret="+wnprop.getPropValues().getClient_secret()
					//+"&grant_type=authorization_code"
					+"&redirect_uri="+redirectUri
					+"&response_type=code";
			
		System.out.println("##### code : " + getResponse(url, param));
	}
	
	public String getResponse(String url, String param){
		StringBuffer receiveMsg = new StringBuffer();
		HttpURLConnection uc = null;
		try {
			URL servletUrl = new URL(url);
			uc = (HttpURLConnection) servletUrl.openConnection();
			uc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setDefaultUseCaches(false);
			DataOutputStream dos = new DataOutputStream (uc.getOutputStream());
			dos.write(param.getBytes());
			dos.flush();
			dos.close();
			
			int errorCode = 0;
			// -- Network error check
			System.out.println("[URLConnection Response Code] " + uc.getResponseCode());
			if (uc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String currLine = "";
                // UTF-8. ..
                /*BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
				
                while ((currLine = in.readLine()) != null) {
                	receiveMsg.append(currLine).append("\r\n");
                }
                in.close();
                */
                
            	//System.out.println("#### HTTP OK : " + receiveMsg.toString());
				System.out.println("#### HTTP OK : " + uc.getURL().getPath());
            }else{
            	System.out.println("#### ERROR MSG : " + IOUtils.toString(uc.getErrorStream()));
            }
		} catch(Exception ex) {
			System.out.println(ex);
		} finally {
			uc.disconnect();
		}
		
		return receiveMsg.toString();
	}
	
	public static void main(String[] args){
		InstagramWorker iWorker = new InstagramWorker();
		iWorker.getCode();
		iWorker.getAccessToken();
	}

}
