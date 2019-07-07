package com.example.myprodWeb;

import java.sql.Connection;
import java.util.List;

import com.example.myprodWeb.R;
import com.example.myprodWeb.Account;
import com.example.myprodWeb.AccountDao;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProdispActivity extends Activity {

	// 需要适配的数据集合
	public static List<Account> list;
	// 数据库增删改查操作类
	private AccountDao dao;
	// 输入姓名的EditText
	private EditText nameET;
	// 输入金额的EditText
	private EditText balanceET;
	// 输入数量的EditText
	private EditText countET;
	// 适配器
	private MyAdapter adapter;
	// ListView
	private ListView accountLV;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prodisplay);
		// 从MySQL数据库查询出所有数据

//		Thread th = new Thread() {
//			public void run() {
//				list = dao.queryAll();
//			};
//		};
//		th.start();
//		try {
//			th.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		// 初始化控件
		initView();
		dao = new AccountDao(this);

		adapter = new MyAdapter();
		accountLV.setAdapter(adapter);// 给ListView添加适配器(自动把数据生成条目)
	}

	// 初始化控件
	private void initView() {
		accountLV = (ListView) findViewById(R.id.accountLV);
		nameET = (EditText) findViewById(R.id.nameET);
		balanceET = (EditText) findViewById(R.id.balanceET);
		countET = (EditText) findViewById(R.id.countET);
		// 添加监听器, 监听条目点击事件
		accountLV.setOnItemClickListener(new MyOnItemClickListener());

	}

	// activity_mian.xml 对应ImageView的点击事件触发的方法
	public void add(View v) {
		String name = nameET.getText().toString().trim();
		String balance = balanceET.getText().toString().trim();
		String count = countET.getText().toString().trim();
		// 三目运算 balance.equals(“”) 则等于0
		// 如果balance 不是空字符串 则进行类型转换
		final Account a = new Account(name, balance.equals("") ? 0
				: Integer.parseInt(balance), count.equals("") ? 0
				: Integer.parseInt(count));
		dao.insert(a); // 插入数据库

		new Thread() {// 开启子线程访问网络
			public void run() {
				// 调用LoginService里面的方法请求网络获取数据
				final String result = ProdDBService.insertByClientGet(a);

				if ((result != null) && result.equals("0")) {
					// 使用UI线程弹出toast
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Toast.makeText(LoginActivity.this, result,
							// 0).show();
							Toast.makeText(ProdispActivity.this, "恭喜您，添加成功",
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {// 请求失败 使用UI线程弹出toast
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ProdispActivity.this,
									"添加失败...result=" + result, 0).show();
						}
					});
				}
			};
		}.start();

		list.add(a); // 插入集合
		adapter.notifyDataSetChanged(); // 刷新界面
		// 选中最后一个
		accountLV.setSelection(accountLV.getCount() - 1);
		nameET.setText("");
		balanceET.setText("");
		countET.setText("");
	}

	// 自定义一个适配器(把数据装到ListView的工具)
	private class MyAdapter extends BaseAdapter {
		public int getCount() { // 获取条目总数
			return list.size();
		}

		public Object getItem(int position) { // 根据位置获取对象
			return list.get(position);
		}

		public long getItemId(int position) { // 根据位置获取id
			return position;
		}

		// 获取一个条目视图
		public View getView(int position, View convertView, ViewGroup parent) {
			// 重用convertView
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(), R.layout.item, null);
			// 获取该视图中的TextView
			TextView idTV = (TextView) item.findViewById(R.id.idTV);
			TextView nameTV = (TextView) item.findViewById(R.id.nameTV);
			TextView balanceTV = (TextView) item.findViewById(R.id.balanceTV);
			TextView countTV = (TextView) item.findViewById(R.id.countTV);
			// 根据当前位置获取Account对象
			final Account a = list.get(position);
			// 把Account对象中的数据放到TextView中
			idTV.setText(a.getId() + "");
			nameTV.setText(a.getName());
			balanceTV.setText(a.getBalance() + "");
			countTV.setText(a.getCount() + "");
			ImageView upIV = (ImageView) item.findViewById(R.id.upIV);
			ImageView downIV = (ImageView) item.findViewById(R.id.downIV);
			ImageView deleteIV = (ImageView) item.findViewById(R.id.deleteIV);
			// 向上箭头的点击事件触发的方法
			upIV.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					a.setBalance(a.getBalance() + 1); // 修改值
					notifyDataSetChanged(); // 刷新界面
					new Thread() {// 开启子线程访问网络
						public void run() {
							// 调用LoginService里面的方法请求网络获取数据
							final String result = ProdDBService
									.updateByClientGet(a);

							if ((result != null) && result.equals("0")) {
								// 使用UI线程弹出toast
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// Toast.makeText(LoginActivity.this,
										// result, 0).show();
										Toast.makeText(ProdispActivity.this,
												"恭喜您，更新成功", Toast.LENGTH_SHORT)
												.show();
									}
								});
							} else {// 请求失败 使用UI线程弹出toast
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(ProdispActivity.this,
												"更新失败...", 0).show();
									}
								});
							}
						};
					}.start();
					dao.update(a); // 更新数据库
				}
			});
			// 向下箭头的点击事件触发的方法
			downIV.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					a.setBalance(a.getBalance() - 1);
					notifyDataSetChanged();
					new Thread() {// 开启子线程访问网络
						public void run() {
							// 调用LoginService里面的方法请求网络获取数据
							final String result = ProdDBService
									.updateByClientGet(a);

							if ((result != null) && result.equals("0")) {
								// 使用UI线程弹出toast
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// Toast.makeText(LoginActivity.this,
										// result, 0).show();
										Toast.makeText(ProdispActivity.this,
												"恭喜您，更新成功", Toast.LENGTH_SHORT)
												.show();
									}
								});
							} else {// 请求失败 使用UI线程弹出toast
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(ProdispActivity.this,
												"更新失败...", 0).show();
									}
								});
							}
						};
					}.start();
					dao.update(a);
				}
			});
			// 删除图片的点击事件触发的方法
			deleteIV.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// 删除数据之前首先弹出一个对话框
					android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							list.remove(a); // 从集合中删除
							new Thread() {// 开启子线程访问网络
								public void run() {
									// 调用LoginService里面的方法请求网络获取数据
									final String result = ProdDBService
											.deleteByClientGet(a.getId());

									if ((result != null) && result.equals("0")) {
										// 使用UI线程弹出toast
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// Toast.makeText(LoginActivity.this,
												// result, 0).show();
												Toast.makeText(
														ProdispActivity.this,
														"恭喜您，删除成功",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
									} else {// 请求失败 使用UI线程弹出toast
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(
														ProdispActivity.this,
														"注册失败...", 0).show();
											}
										});
									}
								};
							}.start();
							dao.delete(a.getId()); // 从数据库中删除
							notifyDataSetChanged();// 刷新界面
						}
					};
					Builder builder = new Builder(ProdispActivity.this); // 创建对话框
					builder.setTitle("确定要删除吗?"); // 设置标题
					// 设置确定按钮的文本以及监听器
					builder.setPositiveButton("确定", listener);
					builder.setNegativeButton("取消", null); // 设置取消按钮
					builder.show(); // 显示对话框
				}
			});
			return item;
		}
	}

	// ListView的Item点击事件
	private class MyOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 获取点击位置上的数据
			Account a = (Account) parent.getItemAtPosition(position);
			Toast.makeText(getApplicationContext(), a.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

}
