package com.wisenut.openapi.model;

import java.util.ArrayList;

public class ResultData {
	private static final String JSON_COMMA = ",";
	private static final String JSON_COLON = ":";
	private static final String JSON_QUOT ="\"";
	
	private String provider;
	private String sort;
	private int totalCount;
	private int currentCount;
	private int startPos;
	private ArrayList<ResultItem> itemList;
	
	public String toString(){
		StringBuffer sbResult = new StringBuffer();
		sbResult.append("{");
		sbResult.append(JSON_QUOT + "Result" + JSON_QUOT + JSON_COLON);
		
		sbResult.append(	"{");
		sbResult.append(	JSON_QUOT + "provider" + JSON_QUOT + JSON_COLON).append(this.provider);
		sbResult.append(	JSON_COMMA);
		sbResult.append(	JSON_QUOT + "totalCount" + JSON_QUOT + JSON_COLON).append(this.totalCount);
		sbResult.append(	JSON_COMMA);
		sbResult.append(	JSON_QUOT + "currentCount" + JSON_QUOT + JSON_COLON).append(this.currentCount);
		sbResult.append(	JSON_COMMA);
		sbResult.append(	JSON_QUOT + "startPos" + JSON_QUOT + JSON_COLON).append(this.startPos);
		sbResult.append(	JSON_COMMA);
		sbResult.append(	JSON_QUOT + "itemList" + JSON_QUOT + JSON_COLON);
		sbResult.append(		"[");
		for(int i=0; i<itemList.size(); i++){
			sbResult.append(	"{");
			sbResult.append(	JSON_QUOT + "title" + JSON_QUOT + JSON_COLON).append(this.itemList.get(i).getTitle());
			sbResult.append(	JSON_COMMA);
			sbResult.append(	JSON_QUOT + "contents" + JSON_QUOT + JSON_COLON).append(this.itemList.get(i).getContents());
			sbResult.append(	JSON_COMMA);
			sbResult.append(	JSON_QUOT + "createDate" + JSON_QUOT + JSON_COLON).append(this.itemList.get(i).getCreateDate());
			sbResult.append(	JSON_COMMA);
			sbResult.append(	JSON_QUOT + "author" + JSON_QUOT + JSON_COLON).append(this.itemList.get(i).getAuthor());
			sbResult.append(	"}");
			
			if( i != itemList.size()-1 ){
				sbResult.append(JSON_COMMA);
			}
		}
		sbResult.append(		"]");
		sbResult.append(	"}");
		sbResult.append(	JSON_COMMA);
		
		sbResult.append("}");
		
		return sbResult.toString();
	}
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
	public ArrayList<ResultItem> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<ResultItem> itemList) {
		this.itemList = itemList;
	}
	public void addItem(String title, String contents, String createDate, String author){
		ResultItem item = new ResultItem();
		item.setTitle(title);
		item.setContents(contents);
		item.setCreateDate(createDate);
		item.setAuthor(author);
		
		itemList.add(item);
	}
}
