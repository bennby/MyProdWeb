package com.example.myprodWeb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Utils {
	// 保存手机号码和登录密码到data.xml文件中
	public static boolean saveUserInfo(Context context, String number,
			String password) {
		SharedPreferences sp = context.getSharedPreferences("data",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("userName", number);
		edit.putString("pwd", password);
		edit.commit();
		return true;
	}

	// 从data.xml文件中获取存储的手机号码和密码
	public static Map<String, String> getUserInfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences("data",
				Context.MODE_PRIVATE);
		String number = sp.getString("userName", null);
		String password = sp.getString("pwd", null);
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("number", number);
		userMap.put("password", password);
		return userMap;
	}

}
