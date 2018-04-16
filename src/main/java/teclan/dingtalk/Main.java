package teclan.dingtalk;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main {

	public static void main(String[] args) throws IOException {

		File file = new File("config/application.conf");

		Config root = ConfigFactory.parseFile(file);

		Config config = root.getConfig("config");

		String url = config.getString("url");
		List<String> phones = config.getStringList("phones");

		DingTalkServer.send(url, "系统状态提醒", "## 当前系统运行异常，请及时关注。IP：10.0.0.222", phones);

	}
}
