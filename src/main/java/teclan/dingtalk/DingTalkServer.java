package teclan.dingtalk;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DingTalkServer {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static String send(String url, String title, String content, List<String> phones) throws IOException {

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
