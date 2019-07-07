package com.example.myprodWeb;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Data;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button dialViewBtn;
	private Button dialBtn;
	private Button homeBtn;
	private Button urlBtn;
	private Button sendSmsBtm;
	private Button openCameraBtn;
	private Button prodActivityBtn;
	private Button backupATTENBtn;
	private Button getATTENBtn;
	private Button backupSMSBtn;
	private Button getSMSBtn;
	private Button jumpBtn;
	private List<Contact> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setListeners();
	}

	void init() {
		homeBtn = (Button) findViewById(R.id.homeBtn);
		urlBtn = (Button) findViewById(R.id.urlBtn);
		sendSmsBtm = (Button) findViewById(R.id.sendSmsBtm);
		dialViewBtn = (Button) findViewById(R.id.dialViewBtn);
		dialBtn = (Button) findViewById(R.id.dialBtn);
		openCameraBtn = (Button) findViewById(R.id.openCameraBtn);
		prodActivityBtn = (Button) findViewById(R.id.ProdActivityBtn);
		backupATTENBtn = (Button) findViewById(R.id.btnBackATTEN);
		getATTENBtn = (Button) findViewById(R.id.btnGetATTEN);
		backupSMSBtn = (Button) findViewById(R.id.btnBackSMS);
		getSMSBtn = (Button) findViewById(R.id.btnGetSMS);
		jumpBtn= (Button) findViewById(R.id.jumpBtn);
		// 事先加载ProdispActivity中的list数据
		final AccountDao dao = new AccountDao(MainActivity.this);
		new Thread() {
			public void run() {
				ProdispActivity.list = dao.queryAll();
			};
		}.start();
	}

	void setListeners() {
		homeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			}

		});
		urlBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("www.baidu.com"));
				intent.setClassName("com.android.browser",
						"com.android.browser.BrowserActivity");
				startActivity(intent);
			}

		});
		sendSmsBtm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SENDTO);
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("smsto:18025721907"));
				intent.putExtra("sms_body", "土豪我们做朋友吧");
				startActivity(intent);
			}

		});
		dialViewBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:18025721907"));
				startActivity(intent);
			}

		});
		dialBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:18025721907"));
				startActivity(intent);
			}

		});
		jumpBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName(MainActivity.this,
						"com.example.myprodWeb.SecondActivity");
				startActivity(intent);
			}

		});
		openCameraBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.media.action.IMAGE_CAPTURE");
				intent.addCategory("android.intent.category.DEFAULT");
				startActivity(intent);
			}

		});
		prodActivityBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AccountDao dao=new AccountDao(MainActivity.this);
				new Thread() {
					public void run() {
						ProdispActivity.list = dao.queryAll();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent();
								intent.setClassName(MainActivity.this,
										"com.example.myprodWeb.ProdispActivity");
								startActivity(intent);
							}
						});
					};
				}.start();
			}

		});
		backupATTENBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backupContacts();
			}
		});
		getATTENBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(){
					@Override
					public void run() {
						String jsonData=MainService.getContactsJson();
						Log.i("MainActivity", "服务器返回json数据："+jsonData);
						try {
							JSONArray jsonArray=new JSONArray(jsonData);
							if(jsonArray.length()>0){
								for(int i=0;i<jsonArray.length();i++){
									JSONObject item = jsonArray.getJSONObject(i);
									//Log.i("MainActivity", item.getString("name"));
									addContact(item.getString("name"),item.getString("phoneNumber"));
								}
							}
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainActivity.this, "还原联系人成功", Toast.LENGTH_SHORT).show();
								}
							});
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		
		backupSMSBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					MainService.backupSMS(MainActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		getSMSBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String jsonData=MainService.getSMSJson();
				try {
					JSONArray jsonArray=new JSONArray(jsonData);
					if(jsonArray.length()>0){
						for(int i=0;i<jsonArray.length();i++){
							JSONObject item = jsonArray.getJSONObject(i);
							addSMS(item.getString("address"), item.getString("date"), item.getString("body"), item.getString("type"));
						}
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "还原短信成功", Toast.LENGTH_SHORT).show();
						}
					});
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void backupContacts() {
		List<String> list = new ArrayList<String>();
		contacts = new ArrayList<Contact>();// 联系人列表
		Cursor cursor = null;
		try {
			// cursor指针 query询问 contract协议 kinds种类
			cursor = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					String displayName = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String number = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					Log.d("MainActivity", "读取联系人：" + displayName + " " + number);
					Contact contact = new Contact();
					contact.setName(displayName);
					contact.setPhoneNumber(number);
					list.add(displayName + '\n' + number);
					contacts.add(contact);
				}
			}
			new Thread() {
				public void run() {
					JSONArray dataArray = new JSONArray();
					for (Contact contact : contacts) {
						JSONObject data = new JSONObject();
						try {
							data.put("name", contact.getName());
							data.put("phoneNumber", contact.getPhoneNumber());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						dataArray.put(data);
					}
					Log.i("MainActivity", dataArray.toString());
					try {
						// 1 、创建HttpClient对象
						HttpClient client = new DefaultHttpClient();
						// 2、拼装路径,注意将参数编码
						String path = "http://10.0.2.2:8080/myProdServ/BackupServlet?TYPE=backup&JSONDATA="
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
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(MainActivity.this,
												"备份联系人成功", Toast.LENGTH_SHORT)
												.show();
									}
								});
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(MainActivity.this,
												"备份联系人失败", Toast.LENGTH_SHORT)
												.show();
									}
								});
							}
						} else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainActivity.this,
											"备份联系人失败", Toast.LENGTH_SHORT)
											.show();
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public void addContact(String name, String phoneNumber) {
		// 创建一个空的ContentValues
		ContentValues values = new ContentValues();

		// 向RawContacts.CONTENT_URI空值插入，
		// 先获取Android系统返回的rawContactId
		// 后面要基于此id插入值
		Uri rawContactUri = getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		values.clear();

		values.put(Data.RAW_CONTACT_ID, rawContactId);
		// 内容类型
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		// 联系人名字
		values.put(StructuredName.GIVEN_NAME, name);
		// 向联系人URI添加联系人名字
		getContentResolver().insert(Data.CONTENT_URI, values);
		values.clear();

		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		// 联系人的电话号码
		values.put(Phone.NUMBER, phoneNumber);
		// 电话类型
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		// 向联系人电话号码URI添加电话号码
		getContentResolver().insert(Data.CONTENT_URI, values);
		values.clear();
	}

	public void addSMS(String address, String date, String body, String type) {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("type", type);
		values.put("body", body);
		values.put("date",Long.parseLong(date));
		resolver.insert(uri, values);		
	}

}
