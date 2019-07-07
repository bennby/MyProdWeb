package com.example.myprodWeb;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


//修改account表
public class ProdDBService {
	// 采用httpclient get提交数据
			public static String insertByClientGet(Account account) {
				String sId=String.valueOf(account.getId());
				String sBa=String.valueOf(account.getBalance());
				String sCo=String.valueOf(account.getCount());
				try {
					//1 、创建HttpClient对象
					HttpClient client = new DefaultHttpClient();
					//2、拼装路径,注意将参数编码
					String path = "http://10.0.2.2:8080/myProdServ/ProdDBServlet?ACTION="
							+ URLEncoder.encode("0")
							+ "&SID="
							+ URLEncoder.encode(sId)
							+ "&SNAME="
							+ URLEncoder.encode(account.getName())
							+ "&SBA="
							+ URLEncoder.encode(sBa)
							+"&SCO="
							+ URLEncoder.encode(sCo);
					//3、GET方式请求
					HttpGet httpGet = new HttpGet(path);
		              //4、拿到服务器返回的HttpResponse对象
					HttpResponse response = client.execute(httpGet);
		              //5、拿到状态码
					int code = response.getStatusLine().getStatusCode(); 
					if (code == 200) { 
		                   //获取输入流
						InputStream is = response.getEntity().getContent();
		                    //将输入流转换成字符串
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
			public static String deleteByClientGet(long id) {
				String sId=String.valueOf(id);
				try {
					//1 、创建HttpClient对象
					HttpClient client = new DefaultHttpClient();
					//2、拼装路径,注意将参数编码
					String path = "http://10.0.2.2:8080/myProdServ/ProdDBServlet?ACTION="
							+ URLEncoder.encode("1")
							+ "&SID="
							+ URLEncoder.encode(sId);
					//3、GET方式请求
					HttpGet httpGet = new HttpGet(path);
		              //4、拿到服务器返回的HttpResponse对象
					HttpResponse response = client.execute(httpGet);
		              //5、拿到状态码
					int code = response.getStatusLine().getStatusCode(); 
					if (code == 200) { 
		                   //获取输入流
						InputStream is = response.getEntity().getContent();
		                    //将输入流转换成字符串
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
			public static String updateByClientGet(Account account) {
				String sId=String.valueOf(account.getId());
				String sBa=String.valueOf(account.getBalance());
				try {
					//1 、创建HttpClient对象
					HttpClient client = new DefaultHttpClient();
					//2、拼装路径,注意将参数编码
					String path = "http://10.0.2.2:8080/myProdServ/ProdDBServlet?ACTION="
							+ URLEncoder.encode("2")
							+ "&SID="
							+ URLEncoder.encode(sId)
							+ "&SNAME="
							+ URLEncoder.encode(account.getName())
							+ "&SBA="
							+ URLEncoder.encode(sBa);
					//3、GET方式请求
					HttpGet httpGet = new HttpGet(path);
		              //4、拿到服务器返回的HttpResponse对象
					HttpResponse response = client.execute(httpGet);
		              //5、拿到状态码
					int code = response.getStatusLine().getStatusCode(); 
					if (code == 200) { 
		                   //获取输入流
						InputStream is = response.getEntity().getContent();
		                    //将输入流转换成字符串
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
			
			public static String queryAllByClientGet() {
				try {
					//1 、创建HttpClient对象
					HttpClient client = new DefaultHttpClient();
					//2、拼装路径,注意将参数编码
					String path = "http://10.0.2.2:8080/myProdServ/ProdDBServlet?ACTION="
							+ URLEncoder.encode("3");
					//3、GET方式请求
					HttpGet httpGet = new HttpGet(path);
		              //4、拿到服务器返回的HttpResponse对象
					HttpResponse response = client.execute(httpGet);
		              //5、拿到状态码
					int code = response.getStatusLine().getStatusCode(); 
					if (code == 200) { 
		                   //获取输入流
						InputStream is = response.getEntity().getContent();
		                    //将输入流转换成字符串
						String text = StreamTools.readInputStream(is);
						Log.d("ProdDBService",text);
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
