package com.example.myprodWeb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myprodWeb.Account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccountDao {
	private MyHelper helper;

	public AccountDao(Context context) {
		helper = new MyHelper(context); // 创建Dao时, 创建Helper
	}

	public void insert(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase(); // 获取数据库对象
		// 用来装载要插入的数据的 Map<列名, 列的值>
		ContentValues values = new ContentValues();
		values.put("name", account.getName());
		values.put("balance", account.getBalance());
		long id = db.insert("account", null, values); // 向account表插入数据values,
		account.setId(id); // 得到id
		db.close(); // 关闭数据库

	}

	// 根据id 删除数据
	public int delete(long id) {
		// 对MySql数据库执行操作

		// final String result = ProdDBService.insertByClientGet(id);

		SQLiteDatabase db = helper.getWritableDatabase();
		// 按条件删除指定表中的数据, 返回受影响的行数
		int count = db.delete("account", "_id=?", new String[] { id + "" });
		db.close();

		return count;
	}

	// 更新数据
	public int update(Account account) {
		// 对MySql数据库执行操作
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues(); // 要修改的数据
		values.put("name", account.getName());
		values.put("balance", account.getBalance());
		int count = db.update("account", values, "_id=?",
				new String[] { account.getId() + "" }); // 更新并得到行数
		db.close();
		return count;
	}

	// 查询所有数据倒序排列
	public List<Account> queryAll() {
		// SQLiteDatabase db = helper.getReadableDatabase();
		// Cursor c = db.query("account", null, null, null, null, null,
		// "balance DESC");
		// while (c.moveToNext()) {
		// long id = c.getLong(c.getColumnIndex("_id")); // 可以根据列名获取索引
		// String name = c.getString(1);
		// int balance = c.getInt(2);
		// list.add(new Account(id, name, balance));
		// }
		// c.close();
		// db.close();
		List<Account> list = new ArrayList<Account>();
		String jsonString = ProdDBService.queryAllByClientGet();
		try {
			JSONArray accounts = new JSONArray(jsonString);
			if (accounts.length() > 0) {
				for (int i = 0; i < accounts.length(); i++) {
					JSONObject item = accounts.getJSONObject(i);
					// Log.i("MainActivity", item.getString("name"));
					// addContact(item.getString("name"),item.getString("phoneNumber"));
					Account a = new Account(item.getLong("_id"),
							item.getString("name"), item.getInt("balance"),
							item.getInt("count"));
					list.add(a);
					insertFromMySQL(a);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public void insertFromMySQL(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase(); // 获取数据库对象
		Cursor cursor = db.query("account", new String[] { "_id" }, "_id=?",
				new String[] { account.getId().toString() }, null, null, null);
		//将MySQL数据库中的数据同步到SQLite中
		if (cursor.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put("name", account.getName());
			values.put("balance", account.getBalance());
			values.put("count", account.getCount());
			db.update("account", values, "_id=?", new String[] { account
					.getId().toString() });
		} else {
			// 用来装载要插入的数据的 Map<列名, 列的值>
			ContentValues values = new ContentValues();
			values.put("name", account.getName());
			values.put("balance", account.getBalance());
			values.put("count", account.getCount());
			long id = db.insert("account", null, values); // 向account表插入数据values,
			// account.setId(id); // 得到id
		}
		db.close(); // 关闭数据库
	}
}
