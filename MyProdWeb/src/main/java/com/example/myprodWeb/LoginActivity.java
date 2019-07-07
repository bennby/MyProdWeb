package com.example.myprodWeb;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Map;

import com.example.myprodWeb.R;
import java.sql.Statement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText etNumber;
	private EditText etPassword;

	// private CheckBox cbRemember;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();

		// 取出号码
		Map<String, String> userInfo = Utils.getUserInfo(this);

		if (userInfo != null) {
			// 显示在界面上
			etNumber.setText(userInfo.get("number"));
			etPassword.setText(userInfo.get("password"));
		}

	}

	private void initView() {
		etNumber = (EditText) findViewById(R.id.et_number);
		etPassword = (EditText) findViewById(R.id.et_password);
		Button btn_register = (Button) findViewById(R.id.btn_register);
		Button btn_getback = (Button) findViewById(R.id.Getbackbtn);
		// cbRemember = (CheckBox) findViewById(R.id.cb_remember);
		findViewById(R.id.btn_login).setOnClickListener(this);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				intent.setClassName(LoginActivity.this,
						"com.example.myprodWeb.RegisterActivity");

				startActivity(intent);
			}

		});
		btn_getback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				intent.setClassName(LoginActivity.this,
						"com.example.myprodWeb.SecurityActivity");

				startActivity(intent);
			}
		});
	}

	public void onClick(View v) {
		// 当点击登录时,获取账号 和密码
		final String number = etNumber.getText().toString().trim();
		final String password = etPassword.getText().toString().trim();
		// 校验号码和密码是否正确
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		}

		new Thread() {// 开启子线程访问网络
			public void run() {
				// 调用LoginService里面的方法请求网络获取数据
				final String result = LoginService.loginByClientGet(number,
						password);
				result.trim();
				if ((result != null) && result.equals("0")) {
					// if ((result != null)) {
					// 使用UI线程弹出toast
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Toast.makeText(LoginActivity.this, result,
							// 0).show();
							Intent intent = new Intent();

							intent.setClassName(LoginActivity.this,
									"com.example.myprodWeb.MainActivity");

							startActivity(intent);
						}
					});
				} else {// 请求失败 使用UI线程弹出toast
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(LoginActivity.this, "登录失败...", 0)
									.show();
						}
					});
				}
			};
		}.start();

		// 如果正确, 判断是否勾选了记住密码
		// if(cbRemember.isChecked()) {
		Log.i("MainActivity", "记住密码: " + number + ", " + password);
		// 保存用户信息
		boolean isSaveSuccess = Utils.saveUserInfo(this, number, password);
		if (isSaveSuccess) {
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
		}
		// }
	}
}
