package com.example.myprodWeb;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

//修改user表，用于登录
public class LoginService {
	// 使用HttpURLConnection GET方式提交数据
	public static String loginByGet(String username, String password) {
		try {
			// 拼装URL 注意为了防止乱码 这里需要将参数进行编码
			String path = "http://10.0.2.2:8080/myProdServ/LoginServlet?NAME="
					+ URLEncoder.encode(username, "UTF-8") + "&CODE="
					+ URLEncoder.encode(password);
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

	// 使用HttpURLConnection POST方式提交数据
	public static String loginByPost(String username, String password) {
		try {
			// 要访问的资源路径
			String path = "http://10.0.2.2:8080/myProdServ/LoginServlet";
			// 创建URL的实例
			URL url = new URL(path);
			// 获取HttpURLConnection对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时时间
			conn.setConnectTimeout(5000);
			// 指定请求方式
			conn.setRequestMethod("POST");
			// 准备数据 将参数编码
			String data = "NAME=" + URLEncoder.encode(username) + "&CODE="
					+ URLEncoder.encode(password);
			// 设置请求头
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", data.length() + "");
			// 将数据写给服务器
			conn.setDoOutput(true);
			// 得到输出流
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes()); // 将数据写入输出流中
			int code = conn.getResponseCode(); // 那到服务器返回的状态码
			if (code == 200) {
				// 得到服务器返回的输入流
				InputStream is = conn.getInputStream();
				// 将输入流转换成字符串
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

	// 采用httpclient get提交数据
	public static String loginByClientGet(String username, String password) {
		try {
			// 1 、创建HttpClient对象
			HttpClient client = new DefaultHttpClient();
			// 2、拼装路径,注意将参数编码
			String path = "http://10.0.2.2:8080/myProdServ/LoginServlet?NAME="
					+ URLEncoder.encode(username) + "&CODE="
					+ URLEncoder.encode(password);
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
				return text;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 采用httpclient post提交数据
	public static String loginByClientPost(String username, String password) {
		try {
			// 1、获取HttpClient对象
			HttpClient client = new DefaultHttpClient();
			// 2、指定访问地址
			String path = "http://10.0.2.2:8080/myProdServ/LoginServlet";
			// 3、POST方式请求网络
			HttpPost httpPost = new HttpPost(path);
			// 4、指定要提交的数据实体
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("NAME", username));
			parameters.add(new BasicNameValuePair("CODE", password));
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			// 5、请求服务器并拿到服务器返回的信息
			HttpResponse response = client.execute(httpPost);
			int code = response.getStatusLine().getStatusCode(); // 拿到状态码
			if (code == 200) { // 访问成功
				InputStream is = response.getEntity().getContent();
				// 将输入流转换成字符串
				String text = StreamTools.readInputStream(is);
				return text;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
