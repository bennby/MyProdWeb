package com.example.myprodWeb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import android.app.Activity;

public class MainService {
	public static final String TAG = "MainService";
	private static JSONArray dataArray;

	public static String getContactsJson() {
		try {
			// 拼装URL 注意为了防止乱码 这里需要将参数进行编码
			String path = "http://10.0.2.2:8080/myProdServ/BackupServlet?TYPE=getJson";
			// 创建URL实例
			URL url = new URL(path);
			// 获取HttpURLConnection对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000); // 设置超时时间
			conn.setRequestMethod("GET"); // 设置访问方式
			int code = conn.getResponseCode(); // 拿到返回的状态码
			if (code == 200) { // 请求成功
				InputStream is = conn.getInputStream();
				String text = StreamTools.readInputStream(is);
				return text;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void backupSMS(final Context context) throws Exception {
		dataArray = new JSONArray();
		Uri uri = Uri.parse("content://sms/");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "address", "date",
				"type", "body" }, null, null, null);
		while (cursor.moveToNext()) {
			String address = cursor.getString(0);
			String date = cursor.getString(1);
			String type = cursor.getString(2);
			String body = cursor.getString(3);
			JSONObject data = new JSONObject();
			data.put("address", address);
			data.put("date", date);
			data.put("type", type);
			data.put("body", body);
			dataArray.put(data);
		}
		Log.d(TAG, dataArray.toString());
		new Thread() {
			@Override
			public void run() {
				try {
					// 1 、创建HttpClient对象
					HttpClient client = new DefaultHttpClient();
					// 2、拼装路径,注意将参数编码
					String path = "http://10.0.2.2:8080/myProdServ/BackupServlet?TYPE=backupSMS&JSONDATA="
							+ URLEncoder.encode(dataArray.toString());
					// 3、GET方式请求
					HttpGet httpGet = new HttpGet(path);
					// 4、拿到服务器返回的HttpResponse对象
					HttpResponse response = client.execute(httpGet);
					// 5、拿到状态码
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						// 获取输入流
						InputStream is = response.getEntity().getContent();
						// 将输入流转换成字符串
						String text = StreamTools.readInputStream(is);
						if ("0".equals(text)) {
							((Activity) context).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(context, "备份短信成功",
											Toast.LENGTH_SHORT).show();
								}
							});
						} else {
							((Activity) context).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(context, "备份短信失败",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
					} else {
						((Activity) context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, "备份短信失败",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		cursor.close();
	}

	public static String getSMSJson() {
		try {
			// 拼装URL 注意为了防止乱码 这里需要将参数进行编码
			String path = "http://10.0.2.2:8080/myProdServ/BackupServlet?TYPE=getSMSJson";
			// 创建URL实例
			URL url = new URL(path);
			// 获取HttpURLConnection对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000); // 设置超时时间
			conn.setRequestMethod("GET"); // 设置访问方式
			int code = conn.getResponseCode(); // 拿到返回的状态码
			if (code == 200) { // 请求成功
				InputStream is = conn.getInputStream();
				String text = StreamTools.readInputStream(is);
				Log.d(TAG, text);
				return text;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
