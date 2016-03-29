package com.wisenut.openapi.common;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wisenut.openapi.model.WNAuth;

public class WNProperties {
	private static WNProperties instance = null;
	private Document doc;
	private WNAuth auth;
	
	public void load(Document _doc){
        this.doc = _doc;
    }
	
	public WNProperties(int provider){
	    InputStream in = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            
            InputStream xmlInputStream = WNProperties.class.getClassLoader().getResourceAsStream(WNConstants.METAINFO_BY_PROVIDER[provider][WNConstants.PROPERTY_NAME]);
          
            db.setEntityResolver(new EntityResolver(){
              public InputSource resolveEntity(String publicId, String systemId) {
                  return new InputSource();
              }
          });
              
            Document doc = db.parse(xmlInputStream);
                
            this.load(doc);
            
        } catch (ParserConfigurationException e) {
            //TODO logger.error("[getProperties] ParserConfigurationException : " + WNStringUtil.StackTraceToString(e));
        } catch (SAXException e) {
            //TODO logger.error("[getProperties] SAXException : " + WNStringUtil.StackTraceToString(e));
        } catch (IOException e) {
        	//TODO logger.error("[getProperties] IOException : " + WNStringUtil.StackTraceToString(e));
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(IOException ioe){
            	//TODO logger.error("[getProperties] IOException : " + WNStringUtil.StackTraceToString(ioe));
            }
        }
	}
	
	public static synchronized WNProperties getInstance(int provider){
	    if( instance == null){
            instance = new WNProperties(provider);
        }
        
        return instance;
	}
	
	public synchronized WNAuth getPropValues(int provider) {
		if(auth != null) return auth;
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		NodeList nodeList = null;
		
		try{
			String expression = "//*/" + WNConstants.METAINFO_BY_PROVIDER[provider][WNConstants.PROVIDER_NAME];

			auth = new WNAuth();
			nodeList = (NodeList)xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			
			for(int idx=0; idx<nodeList.getLength(); idx++){
				expression = "//*/client_id";
				String clientId = xpath.compile(expression).evaluate(doc);
				
				expression = "//*/client_secret";
				String clientSecret = xpath.compile(expression).evaluate(doc);
				
				auth.setClient_id(clientId);
				auth.setClient_secret(clientSecret);
			}
			
			return auth;
		}catch(Exception e){
		    //TODO logger.error("[getServerInfo] Exception : " + WNStringUtil.StackTraceToString(e));
		}

		return null;
	}
	
	public static void main(String[] args){
		WNProperties wnprop = WNProperties.getInstance(WNConstants.NAVER_ID);
		WNAuth auth = wnprop.getPropValues(WNConstants.NAVER_ID);
		
		System.out.println(auth.getClient_id());
		System.out.println(auth.getClient_secret());
	}
}
