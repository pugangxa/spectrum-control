package com.gangs.spectrum.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static Properties confProperties;

	static {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() throws IOException {
		if (confProperties == null) {
			confProperties = new Properties();

			InputStream in = PropertiesUtil.class.getClassLoader()
					.getResourceAsStream("config/systemConfig.properties");

			try {
				confProperties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		}
	}
	
    public static Properties getProperties() throws IOException {
        init();
        return confProperties;
    }

    public static void clear() {
        confProperties.clear();
        confProperties = null;
    }

    public static String getStr(String key) {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return confProperties.getProperty(key);
    }

    public static int getInt(String key){
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(confProperties.getProperty(key));
    }
}
