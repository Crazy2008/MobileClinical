package com.winning.mobileclinical.action;

import android.content.Context;

import com.winning.mobileclinical.model.Bwl;
import com.winning.mobileclinical.model.MediaModel;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.HttpResult;
import com.winning.mobileclinical.web.WebUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewBwlAction {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	/*
	 * 添加数据库信息
	 */
	public static int addBwl(Context context,String note,String title,String type, String syxh, String yexh){
		Date date = new Date();
		String time = format.format(date);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content",note));
		params.add(new BasicNameValuePair("title",title));
		params.add(new BasicNameValuePair("time",time));
		params.add(new BasicNameValuePair("syxh",syxh));
		params.add(new BasicNameValuePair("yexh",yexh));
		params.add(new BasicNameValuePair("method", "addbwl"));
		params.add(new BasicNameValuePair("type", type));		//bwl001
		
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	/*
	 * 添加数据库信息
	 */
	public static int addBwl(Context context,Bwl bwl,String syxh, String yexh){
		Date date = new Date();
		String time = format.format(date);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content",bwl.getContents()));
		params.add(new BasicNameValuePair("title",bwl.getTitle()));
		params.add(new BasicNameValuePair("time",time));
		params.add(new BasicNameValuePair("method", "addbwl"));
		params.add(new BasicNameValuePair("type", bwl.getType()));
		params.add(new BasicNameValuePair("glbr", bwl.getGlbr()));
		params.add(new BasicNameValuePair("txsj", bwl.getTxsj()));
		params.add(new BasicNameValuePair("syxh",syxh));
		params.add(new BasicNameValuePair("yexh",yexh));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/*
	 * 添加数据库信息
	 */
	public static int upLoad(Context context,String LB,String LBSM,String path,String bwlid,String description,String fileName,String syxh,String yexh){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lb",LB));
		params.add(new BasicNameValuePair("lbsm",LBSM));
		params.add(new BasicNameValuePair("path",path));
		params.add(new BasicNameValuePair("bwlid", bwlid));
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("fileName", fileName));
		params.add(new BasicNameValuePair("syxh", syxh));
		params.add(new BasicNameValuePair("yexh", yexh));
		params.add(new BasicNameValuePair("method", "upload"));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	public static int upLoadUpdate(Context context,String path,String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("path",path));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("method", "uploadUpdate"));
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	
	/*
	 * 更新照片及录音的描述
	 */
	public static int upLoadUpdateDescription(Context context,String text,String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("text",text));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("method", "UpdateDescription"));
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	//媒体资源删除
	public static int deleteMedia(Context context,String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("method", "deleteMedia"));
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	/*
	 * 
	 */
	public static ArrayList<MediaModel> getMediaId(Context context,String xh){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id",xh));
		params.add(new BasicNameValuePair("method","getMedia"));
		
//		String res = HTTPGetTool.getTool().postJsonString(context,
//				WebUtils.HOST +WebUtils.BWL,params);
//		
//		System.out.println(res);
////		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		ArrayList<MediaModel> list=new ArrayList<MediaModel>();
		try {
			if (res.getCode() == 1){
				JSONArray medias=res.getJson().getJSONArray("medias");
				for(int i=0;i<medias.length();i++){
					JSONObject media=medias.getJSONObject(i);
					list.add(new MediaModel(media));
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	/*
	 * 下载内容
	 */
	public static InputStream downLoad(String path){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("path",path));
		params.add(new BasicNameValuePair("method","download"));
		
		
		return HTTPGetTool.getTool().downLoad(WebUtils.HOST +WebUtils.BWL, params);
	}
	
	
//	/*
//	 * 获取bwl数据库内容
//	 */
	public static List<Bwl> getMyBwl(Context context){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "getmybwl"));
		List<Bwl> list=new ArrayList<Bwl>();
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		try {
			if (res.getCode() == 1){
				JSONArray bwls=res.getJson().getJSONArray("bwls");
				for(int i=0;i<bwls.length();i++){
					JSONObject bwl=bwls.getJSONObject(i);
					list.add(new Bwl(bwl));
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	//获取备忘录
	public static List<Bwl> getBqBwl(Context context, String syxh, String yexh){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "getbqbwl"));
		params.add(new BasicNameValuePair("syxh", syxh));
		params.add(new BasicNameValuePair("yexh", yexh));
		List<Bwl> list=new ArrayList<Bwl>();
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		try {
			if (res.getCode() ==1){
				JSONArray bwls=res.getJson().getJSONArray("bwls");
				for(int i=0;i<bwls.length();i++){
					JSONObject bwl=bwls.getJSONObject(i);
					list.add(new Bwl(bwl));
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	//lp yong
	public static int  updateBwl(Context context,String xh,String txbz,String txsj,String cfbz,String contents,String kjbz){
		Date txdate;
		try {
			txdate=format.parse(txsj);
			txsj=format.format(txdate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("xh",xh));
		params.add(new BasicNameValuePair("txbz",txbz));
		params.add(new BasicNameValuePair("txsj",""));
		params.add(new BasicNameValuePair("cfbz",cfbz ));
		params.add(new BasicNameValuePair("contents",contents ));
		params.add(new BasicNameValuePair("kjbz",kjbz));
		params.add(new BasicNameValuePair("method", "updatebwl"));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	//gutao yong
	public static int updateBwl(Context context,String note,String title,String xh){
		Date date = new Date();
		String time = format.format(date);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content",note));
		params.add(new BasicNameValuePair("title",title));
		params.add(new BasicNameValuePair("time",time));
		params.add(new BasicNameValuePair("id",xh));
		params.add(new BasicNameValuePair("method", "updatebwlTitleOrContents"));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	/**
	 * ZHH
	 * @param context
	 * @param bwl
	 * @return
	 */
	public static int updateBwl(Context context,Bwl bwl){
		Date date = new Date();
		String time = format.format(date);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content",bwl.getContents()));
		params.add(new BasicNameValuePair("title",bwl.getTitle()));
		params.add(new BasicNameValuePair("time",time));
		params.add(new BasicNameValuePair("id",bwl.getXh()));
		params.add(new BasicNameValuePair("method", "updatebwl_new"));
		params.add(new BasicNameValuePair("txsj",bwl.getTxsj()));
		params.add(new BasicNameValuePair("glbr",bwl.getGlbr()));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	/*
	 * 删除bwl内容
	 */
	public static int deleteBwl(Context context,String xh){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("xh",xh));
		params.add(new BasicNameValuePair("method", "deletebwl"));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	public static int updateBwlTitle(Context context,String id,String title){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id",id));
		params.add(new BasicNameValuePair("title",title));
		params.add(new BasicNameValuePair("method", "updatebwltitle"));
		
		HttpResult res = HTTPGetTool.getTool().post2serverBwl(context,
				WebUtils.HOST +WebUtils.BWL, params);
		int result;
		try {
			if (res.getCode() == 1){
				result=Integer.parseInt(res.getJson().getString("result"));
				return result;
			}
		} catch (JSONException  e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
}
