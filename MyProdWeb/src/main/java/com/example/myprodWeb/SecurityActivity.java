package com.example.myprodWeb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecurityActivity extends Activity implements OnClickListener{
	private EditText etNumber;
	private EditText etPassword;
	private EditText etPassword2;
	private Button btn_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security);
		initView();
	}

	private void initView() {
		etNumber = (EditText) findViewById(R.id.et_number);
		etPassword = (EditText) findViewById(R.id.et_password);
		etPassword2 = (EditText) findViewById(R.id.et_password2);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btn_submit) {
			final String uname = etNumber.getText().toString();
			final String password = etPassword.getText().toString();
			final String password2 = etPassword2.getText().toString();
			if ("".equals(uname) || uname == null) {
				Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			} else if ("".equals(password) || password == null
					|| "".equals(password2) || password2 == null) {
				Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
			} else if (!password.equals(password2)) {
				Toast.makeText(this, "前后密码不一致", Toast.LENGTH_SHORT).show();
			} else {
				//判断用户名是否存在
				new Thread() {// 开启子线程访问网络
					public void run() {
						String result=SecurityService.findOne(uname);
						if ((result != null) && result.equals("0")) {
							String result2=SecurityService.ChangePasswordByClientGet(uname, password);
							if ((result2 != null) && result2.equals("0")) {//密码修改成功
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(SecurityActivity.this,"密码修改成功.", Toast.LENGTH_SHORT).show();
										boolean isSaveSuccess = Utils.saveUserInfo(SecurityActivity.this, uname, password);
										if (isSaveSuccess) {
											Toast.makeText(SecurityActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(SecurityActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
										}
										Intent intent = new Intent();
										intent.setClassName(SecurityActivity.this,
												"com.example.myprodWeb.LoginActivity");
										startActivity(intent);									
									}
								});
							} else {// 密码修改失败
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(SecurityActivity.this,"密码修改失败.", Toast.LENGTH_SHORT).show();
									}
								});
							}
						} else {// 用户名不存在
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(SecurityActivity.this,"用户名不存在.", Toast.LENGTH_SHORT).show();
								}
							});
						}
					};
				}.start();
			}
		}
	}

}
