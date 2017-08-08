package com.example.okhttpdemo;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.Handler;
import android.os.Looper;

/**
 * 使用OkHttp封装的网络请求工具类
 */
public class HttpUtils
{

	private OkHttpClient client;

	public static final int TIMEOUT = 1000 * 60;

	private Handler handler = new Handler(Looper.getMainLooper());

	public HttpUtils()
	{
		this.init();
	}

	private void init()
	{
		client = new OkHttpClient();

		// 设置超时
		client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
				.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(TIMEOUT, TimeUnit.SECONDS).build();
	}

	/**
	 * get请求
	 * 
	 * @param url
	 *            ：请求url
	 * 
	 * @param callBack
	 *            ： 携带返回结果的回调对象
	 */
	public void getMethod(String url, final HttpCallBack callBack)
	{
		// 设置请求对象。无需指定get方法，因为默认就是get方式的
		Request request = new Request.Builder().url(url).build();

		// 启动回调对象的onstart方法，用于访问前的初始化操作
		OnStart(callBack);

		// 执行访问网络，返回结果
		client.newCall(request).enqueue(new Callback()
		{
			// 访问失败，返回错误信息
			@Override
			public void onFailure(Call call, IOException e)
			{
				OnError(callBack, e.getMessage());
			}

			// 访问成功：返回结果
			@Override
			public void onResponse(Call call, Response response)
					throws IOException
			{
				if (response.isSuccessful())
				{
					onSuccess(callBack, response.body().string());

				} else
				{
					OnError(callBack, response.message());
				}
			}
		});
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            ：请求url
	 * 
	 * @param map
	 *            ：map格式的请求参数
	 * 
	 * @param callBack
	 *            ： 携带返回结果的回调对象
	 */
	public void postMap(String url, Map<String, String> map,
			final HttpCallBack callBack)
	{
		// 设置请求参数
		FormBody.Builder builder = new FormBody.Builder();

		// 遍历map，获得请求参数，设置给builder
		if (map != null)
		{
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				builder.add(entry.getKey(), entry.getValue().toString());
			}
		}

		RequestBody body = builder.build();

		// 设置请求对象
		Request request = new Request.Builder().url(url).post(body).build();

		// 启动回调对象的onstart方法，用于访问前的初始化操作
		OnStart(callBack);

		// 执行访问网络，返回结果
		client.newCall(request).enqueue(new Callback()
		{
			// 访问失败，返回错误信息
			@Override
			public void onFailure(Call call, IOException e)
			{
				OnError(callBack, e.getMessage());
			}

			// 访问成功：返回结果
			@Override
			public void onResponse(Call call, Response response)
					throws IOException
			{
				if (response.isSuccessful())
				{
					onSuccess(callBack, response.body().string());

				} else
				{
					OnError(callBack, response.message());
				}
			}
		});
	}

	/**
	 * 初始化方法
	 */
	public void OnStart(HttpCallBack callBack)
	{
		if (callBack != null)
		{
			callBack.onstart();
		}
	}

	/**
	 * 访问成功的方法
	 * 
	 * @param callBack
	 *            ： 携带返回结果的回调对象
	 * 
	 * @param data
	 *            ：返回数据
	 */
	public void onSuccess(final HttpCallBack callBack, final String data)
	{
		if (callBack != null)
		{
			handler.post(new Runnable()
			{
				@Override
				public void run()
				{
					// 在主线程操作
					callBack.onSusscess(data);
				}
			});
		}
	}

	/**
	 * 访问失败的方法
	 * 
	 * @param callBack
	 *            ： 携带返回结果的回调对象
	 * 
	 * @param msg
	 *            ：失败信息
	 */
	public void OnError(final HttpCallBack callBack, final String msg)
	{
		if (callBack != null)
		{
			handler.post(new Runnable()
			{
				@Override
				public void run()
				{
					callBack.onError(msg);
				}
			});
		}
	}

	/**
	 * 携带数据的接口。本类访问网络获取到的数据，通过这个接口传递出去
	 */
	public interface HttpCallBack
	{
		// 开始访问：用于初始化
		public void onstart();

		// 访问成功：参数是访问成功的数据
		public void onSusscess(String data);

		// 访问失败：参数是访问失败的提示信息
		public void onError(String meg);
	}
}