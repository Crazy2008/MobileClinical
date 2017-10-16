package com.winning.mobileclinical.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.winning.mobileclinical.activity.Login;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class HTTPGetTool {
	protected static DefaultHttpClient httpclient;
	private static HTTPGetTool tool = null;
	private String cookie = "";

	private HTTPGetTool() {
		httpclient = new DefaultHttpClient();
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 20000);
	}

	public static HTTPGetTool getTool() {
		if (tool == null) {
			tool = new HTTPGetTool();
		}
		return tool;
	}

	public JSONObject login(String url) {

		try {
			if (url == null)
				return new JSONObject("{\"success\":false,\"msg\":\"系统错误！\"}");
			HttpGet get = new HttpGet();
			get.setURI(new URI(url));
			HttpResponse response = httpclient.execute(get);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {

				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				JSONObject json = new JSONObject(strResult);
				Header[] headers = response.getHeaders("Set-Cookie");
				if (headers.length != 0)
					cookie = headers[0].toString();
				return json;
			} else
				return new JSONObject("{\"success\":false,\"msg\":\"系统错误！\"}");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public synchronized InputStream downLoad(String url,
			List<NameValuePair> parameters) {
		InputStream in = null;
		if (url == null)
			return null;
		HttpResponse response = null;
		HttpPost post = null;
		post = new HttpPost(url);
		post.setHeader("Cookie", cookie);
		try {
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			response = httpclient.execute(post);
			HttpEntity httpEntity = response.getEntity();
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				in = httpEntity.getContent();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	public synchronized JSONObject post(String url, Map<String, File> files) {
		if (url == null)
			return null;
		HttpResponse response = null;
		HttpPost post = null;
		try {
			post = new HttpPost(url);
			MultipartEntity entity = new MultipartEntity();
			/*
			 * if (param != null && !param.isEmpty()) { for (Map.Entry<String,
			 * String> entry : param.entrySet()) { if (entry.getValue() != null
			 * && entry.getValue().trim().length() > 0) {
			 * entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			 * } } }
			 */
			// 添加文件参数
			if (files != null && files.size() > 0) {
				for (Map.Entry<String, File> entry : files.entrySet()) {
					if (entry.getValue().exists()) {
						entity.addPart(entry.getKey(),
								new FileBody(entry.getValue()));
					}
				}
			}
			post.setEntity(entity);
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 60000);
			HttpConnectionParams.setSoTimeout(params, 60000);
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				System.out.println("success!!!!!!!!!!!!!!");
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				Log.e("web", strResult);
				if (strResult.equals("no session"))
					return null;
				JSONObject json = new JSONObject(strResult);
				return json;
			} else
				return new JSONObject(
						"{\"success\":\"false\",\"msg\":\"系统错误！\"}");

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
		}
	}

	public synchronized JSONObject post(String url,
			List<NameValuePair> parameters) {
		if (url == null)
			return null;
		HttpResponse response = null;
		HttpPost post = null;
		try {
			post = new HttpPost(url);
			post.setHeader("Cookie", cookie);
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				// Log.e("web", strResult);
				if (strResult.equals("no session")) {
					return null;
				}
				JSONObject json = new JSONObject(strResult);
				return json;
			} else
				return new JSONObject(
						"{\"success\":\"false\",\"msg\":\"系统错误！\"}");

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 1：正常响应 2：超时 3：网络连接异常 4：服务器错误 5：响应解析异常 6：会话超时 7：其他错误
	public synchronized String getJsonString(Context context, String url) {
		if (url == null)
			return null;

		try {
			HttpGet get = new HttpGet();
			get.setURI(new URI(url));
			get.setHeader("Cookie", cookie);
			HttpResponse response = httpclient.execute(get);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				// Log.e("web", strResult);
				if (strResult.equals("no session")) {
					Intent intent = new Intent();
					intent.setClass(context, Login.class);
					// intent.putExtra("relogin", false);
					// ((Activity) context).startActivityForResult(intent, 1);
					context.startActivity(intent);
					// ((Activity) context).finish();
					return null;
				}
				return strResult;

			} else
				return null;// new HttpResult(4, "服务器响应异常！", null);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, "", null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, e.getMessage(), null);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;// new HttpResult(4, "服务器响应异常！", null);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;// new HttpResult(2, "访问超时！", null);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return null;// new HttpResult(2, "访问超时！", null);
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			return null;// new HttpResult(3, "网络连接异常！", null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;// new HttpResult(3, e.getMessage(), null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, "访问地址不正确！", null);
		}

	}

	public synchronized String postJsonString(Context context, String url,
			List<NameValuePair> parameters) {
		// TODO Auto-generated method stub
		if (url == null)
			return null;

		try {
			HttpResponse response = null;
			HttpPost post = null;

			post = new HttpPost();
			post.setHeader("Cookie", cookie);
			post.setURI(new URI(url));
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				// Log.e("web", strResult);
				if (strResult.equals("no session")) {
					Intent intent = new Intent();
					intent.setClass(context, Login.class);
					// intent.putExtra("relogin", false);
					// ((Activity) context).startActivityForResult(intent, 1);
					context.startActivity(intent);

					return null;// new HttpResult(6, "会话超时！", null);
				}
				return strResult;
			} else
				return null;// new HttpResult(4, "服务器响应异常！", null);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, "", null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, e.getMessage(), null);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;// new HttpResult(4, "服务器响应异常！", null);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;// new HttpResult(2, "访问超时！", null);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return null;// new HttpResult(2, "访问超时！", null);
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			
			return null;// new HttpResult(3, "网络连接异常！", null);
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;// new HttpResult(3, e.getMessage(), null);
			// } catch (JSONException e) {
			// e.printStackTrace();
			// return null;//new HttpResult(5, "消息解析异常！", null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, "访问地址不正确！", null);
		}

	}

	public byte[] ImageDownLoad(Context context, String url) {
		// TODO Auto-generated method stub
		if (url == null)
			return null;

		try {
			HttpResponse response = null;
			HttpPost post = null;

			post = new HttpPost();
			post.setHeader("Cookie", cookie);
			post.setURI(new URI(url));
			post.setEntity(new UrlEncodedFormEntity(null, HTTP.UTF_8));
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				byte[] data = EntityUtils.toByteArray(response.getEntity());
				if (new String(data, HTTP.UTF_8).equals("no session")) {

					return null;// new HttpResult(6, "会话超时！", null);
				}
				return data;
			} else
				return null;// new HttpResult(4, "服务器响应异常！", null);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, "", null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, e.getMessage(), null);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;// new HttpResult(4, "服务器响应异常！", null);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;// new HttpResult(2, "访问超时！", null);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return null;// new HttpResult(2, "访问超时！", null);
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			return null;// new HttpResult(3, "网络连接异常！", null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;// new HttpResult(3, e.getMessage(), null);
			// } catch (JSONException e) {
			// e.printStackTrace();
			// return null;//new HttpResult(5, "消息解析异常！", null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;// new HttpResult(7, "访问地址不正确！", null);
		}

	}

	// 1：正常响应 2：超时 3：网络连接异常 4：服务器错误 5：响应解析异常 6：会话超时 7：其他错误
	public synchronized HttpResult post2server(Context context, String url,
			List<NameValuePair> parameters) {
		if (url == null)
			return new HttpResult(7, "访问地址不正确！", null);
		
		try {
			HttpResponse response = null;
			HttpPost post = null;

			post = new HttpPost();
			post.setHeader("Cookie", cookie);
			post.setURI(new URI(url));
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				Log.e("web", strResult);
				if (strResult.equals("no session"))
					return new HttpResult(6, "会话超时！", null);
				JSONObject json = new JSONObject(strResult);
				if(json.getString("success").equals("true")){
					return new HttpResult(1, "成功！", json);
				}else{
					return new HttpResult(4, json.getString("msg"),null);
				}
				
			} else
				return new HttpResult(4, "服务器响应异常！", null);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new HttpResult(7, "", null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new HttpResult(7, e.getMessage(), null);
		} catch (ParseException e) {
			e.printStackTrace();
			return new HttpResult(4, "服务器响应异常！", null);
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			return new HttpResult(2, "访问超时！", null);
		}catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return new HttpResult(2, "访问超时！", null);
		}catch (HttpHostConnectException e) {
			e.printStackTrace();
			return new HttpResult(3, "网络连接异常！", null);
		}catch (IOException e) {
			e.printStackTrace();
			return new HttpResult(3, e.getMessage(), null);
		} catch (JSONException e) {
			e.printStackTrace();
			return new HttpResult(5, "消息解析异常！", null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return new HttpResult(7, "访问地址不正确！", null);
		} 	
		
	}
	
	
	public synchronized HttpResult post3server(Context context, String url, List<NameValuePair> parameters) {
		if (url == null)
			return new HttpResult(7, "访问地址不正确！", null);
		
		try {
			HttpResponse response = null;
			HttpPost post = null;

			post = new HttpPost();
			post.setHeader("Cookie", cookie);
			post.setURI(new URI(url));
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				Log.e("web", strResult);
				if (strResult.equals("no session"))
					return new HttpResult(6, "会话超时！", null);
				JSONObject json = new JSONObject(strResult);
				if(json.getString("success").equals("true")){
					return new HttpResult(1, "成功！", json);
				}else{
					return new HttpResult(4, "失败！",null);
				}
				
			} else
				return new HttpResult(4, "服务器响应异常！", null);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new HttpResult(7, "", null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new HttpResult(7, e.getMessage(), null);
		} catch (ParseException e) {
			e.printStackTrace();
			return new HttpResult(4, "服务器响应异常！", null);
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			return new HttpResult(2, "访问超时！", null);
		}catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return new HttpResult(2, "访问超时！", null);
		}catch (HttpHostConnectException e) {
			e.printStackTrace();
			return new HttpResult(3, "网络连接异常！", null);
		}catch (IOException e) {
			e.printStackTrace();
			return new HttpResult(3, e.getMessage(), null);
		} catch (JSONException e) {
			e.printStackTrace();
			return new HttpResult(5, "消息解析异常！", null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return new HttpResult(7, "访问地址不正确！", null);
		} 	
		
	}
	
	
	// 1：正常响应 2：超时 3：网络连接异常 4：服务器错误 5：响应解析异常 6：会话超时 7：其他错误
	public synchronized HttpResult post2serverBwl(Context context, String url,
			List<NameValuePair> parameters) {
		if (url == null)
			return new HttpResult(7, "访问地址不正确！", null);

		try {
			HttpResponse response = null;
			HttpPost post = null;

			post = new HttpPost();
			post.setHeader("Cookie", cookie);
			post.setURI(new URI(url));
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			response = httpclient.execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
//				Log.e("web", strResult);
				if (strResult.equals("no session")) {
					Intent intent = new Intent();
					intent.setClass(context, Login.class);
					intent.putExtra("relogin", false);
					((Activity) context).startActivityForResult(intent, 1);
					
//					Toast.makeText(context, "会话超时，请重新登录！", Toast.LENGTH_LONG).show();
//					((Activity) context).finish();
					return new HttpResult(6, "会话超时！", null);
				}
				JSONObject json = new JSONObject(strResult);
				if (json.getString("success").equals("true")) {
					return new HttpResult(1, "成功！", json);
				} 
//				else if(json.getString("success").equals("empty")){
//					return new HttpResult(4, json.getString("msg"), null);
//				} 
				else {
					return new HttpResult(4, json.getString("success"), null);
				}

			} else
				return new HttpResult(4, "服务器响应异常！", null);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new HttpResult(7, "", null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new HttpResult(7, e.getMessage(), null);
		} catch (ParseException e) {
			e.printStackTrace();
			return new HttpResult(4, "服务器响应异常！", null);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return new HttpResult(2, "访问超时！", null);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return new HttpResult(2, "访问超时！", null);
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			return new HttpResult(3, "网络连接异常！", null);
		} catch (IOException e) {
			e.printStackTrace();
			return new HttpResult(3, e.getMessage(), null);
		} catch (JSONException e) {
			e.printStackTrace();
			return new HttpResult(5, "消息解析异常！", null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return new HttpResult(7, "访问地址不正确！", null);
		}

	}
	
	static public int checkResult(JSONObject json) {
		try {
			if (json == null || json.isNull("success"))
				return -1; // 错误，不成功
			String s = json.getString("success");
			if (s == null)
				return -1;
			if (s.equals("true"))
				return 0; // 成功
			if (s.equals("no session"))
				return 2; // 会话超时
			else
				return 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public InputStream getVerXMLFromServer(String url)
	{
		if (url == null)
			return null;
		HttpGet get = new HttpGet(url);
		try
		{
			InputStream inStream = null;

			HttpResponse response = httpclient.execute(get);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200)
			{
				inStream = response.getEntity().getContent();
				get.abort();
				return inStream;
			}
			else
			{
				get.abort();
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			get.abort();
			return null;
		}
		finally
		{
			get.abort();
		}
	}
}
