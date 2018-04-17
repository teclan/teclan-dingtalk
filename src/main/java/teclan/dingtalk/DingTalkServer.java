package teclan.dingtalk;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DingTalkServer {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String url;
	private static List<String> phones;
	private static boolean enable;

	static {
		File file = new File("config/application.conf");

		Config root = ConfigFactory.parseFile(file);
		Config config = root.getConfig("config");
		url = config.getString("dingtalk.url");
		phones = config.getStringList("dingtalk.phones");
		enable = config.getBoolean("dingtalk.enable");
	}

	public static String send(String title, String content) throws IOException {
		return send(url, title, content, phones);
	}

	public static String send(String url, String title, String content, List<String> phones) throws IOException {

		if (!enable) {
			return null;
		}

		OkHttpClient client = new OkHttpClient();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgtype", "markdown");

		JSONObject markdown = new JSONObject();
		markdown.put("title", title);



		StringBuilder sb = new StringBuilder();

		sb.append("## ").append(title).append("\n");
		sb.append(content);


		JSONObject at = new JSONObject();
		JSONArray atMobiles = new JSONArray();
		for (String phone : phones) {
			atMobiles.add(phone);
			sb.append(" @").append(phone);
		}

		sb.append(" 时间:").append(DATE_FORMAT.format(new Date()));

		at.put("atMobiles", atMobiles);
		at.put("isAtAll", false);


		markdown.put("text", sb.toString());
		jsonObject.put("markdown", markdown);
		jsonObject.put("at", at);

		RequestBody body = RequestBody.create(JSON, jsonObject.toJSONString());
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

}
