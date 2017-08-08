package com.example.okhttpdemo;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.okhttpdemo.HttpUtils.HttpCallBack;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		HttpUtils httpUtils = new HttpUtils();

		/**
		 * get方式访问
		 */
		httpUtils
				.getMethod(
						"http://10.197.198.225/hrserver/MemberAction!checkUser.action?l_no=F1677616&l_password=....&device=sdk&m_name=%E7%8E%8B%E4%BB%8E%E7%BB%B4",
						new HttpCallBack()
						{

							@Override
							public void onstart()
							{

							}

							@Override
							public void onSusscess(String data)
							{
								// 可对数据进行操作
								System.out.println("get方式获取到的数据：" + data);

								Toast.makeText(getApplicationContext(), data, 1)
										.show();

							}

							@Override
							public void onError(String meg)
							{
							}
						});

		/**
		 * post方式访问
		 */
		String url = "http://10.197.198.225/hrserver/MemberAction!checkUser.action";

		Map<String, String> map = new HashMap<String, String>();
		map.put("l_no", "F1677616");
		map.put("l_password", "...");
		map.put("device", "sdk");
		map.put("m_name", "%E7%8E%8B%E4%BB%8E%E7%BB%B4");

		httpUtils.postMap(url, map, new HttpCallBack()
		{

			@Override
			public void onstart()
			{

			}

			@Override
			public void onSusscess(String data)
			{
				System.out.println("post方式获取到的数据：" + data);

				Toast.makeText(getApplicationContext(), data, 1).show();

			}

			@Override
			public void onError(String meg)
			{
			}
		});
	}
}
