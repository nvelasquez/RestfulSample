package com.restfultest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by NestorLuis on 1/4/14.
 */
public abstract class JSONHttpClient<T> {
	/**
	 * Tag
	 */
	private final String TAG = getClass().getName();
	/**
	 * JSON format type
	 */
	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	/**
	 * {@link java.util.List} of {@link org.apache.http.NameValuePair} objects params.
	 */
	protected final List<NameValuePair> mParams;

	/**
	 * Default constructor.
	 */
	public JSONHttpClient() {
		mParams = new ArrayList<NameValuePair>();
	}

	/**
	 * 
	 * @param url
	 * @param object
	 * @param objectClass
	 * @return
	 */
	@SuppressWarnings("hiding")
	protected <T> T PostObject(final String url, final T object,
			final Type objectClass) {
	
		try {
			StringEntity stringEntity = new StringEntity(new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
					.toJson(object));

				OkHttpClient client = new OkHttpClient();

				RequestBody body = RequestBody.create(JSON, convertStreamToString(stringEntity.getContent()));
				Request request = new Request.Builder().url(url).post(body).build();
				Response response = client.newCall(request).execute();

				String resultString = response.body().string();

				return new GsonBuilder().setDateFormat("yyyy-MM-dd").create()
						.fromJson(resultString, objectClass);			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (ClientProtocolException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} finally {
			mParams.clear();
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param objectClass
	 * @return
	 */
	@SuppressWarnings("hiding")
	protected <T> T PostParams(String url, final Type objectClass) {
		if (isEmpty(mParams)) {
			String paramString = URLEncodedUtils.format(mParams, "utf-8");
			url += "?" + paramString;
		}

		return PostObject(url, null, objectClass);
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	private String convertStreamToString(InputStream inputStream) {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * 
	 * @param url
	 * @param objectClass
	 * @return
	 */
	@SuppressWarnings("hiding")
	protected <T> T Get(String url, final Type objectClass)
			{

		if (!isEmpty(mParams)) {
			String paramString = URLEncodedUtils.format(mParams, "utf-8");
			url += "?" + paramString;
		}

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(url).build();
		try {
			Response response = client.newCall(request).execute();
			String resultString = response.body().string();

			if (resultString.isEmpty() || resultString.equalsIgnoreCase("[]")) {
				return null;
			}
			
			return new GsonBuilder().setDateFormat("yyyy-MM-dd").create()
					.fromJson(resultString, objectClass);

		} catch (Exception e) {
			throw new IllegalArgumentException("Error descargando la informacion: "
					+ e.getMessage());
		} finally {
			mParams.clear();
		}

	}

    private boolean isEmpty(List<NameValuePair> mParams) {
        if (mParams == null) return true;

        return mParams.isEmpty();
    }

    /**
	 * 
	 * @param url
	 * @return
	 */
	protected boolean Delete(String url) {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		String paramString = URLEncodedUtils.format(mParams, "utf-8");
		url += "?" + paramString;
		HttpDelete httpDelete = new HttpDelete(url);

		HttpResponse httpResponse = null;
		try {
			httpResponse = defaultHttpClient.execute(httpDelete);
			return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			mParams.clear();
		}

		return false;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void addParam(final String key, final String value) {
		mParams.add(new BasicNameValuePair(key, value));
	}
}
