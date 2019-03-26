package com.photograph_u.util;

import com.photograph_u.exception.HttpRequestException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpUtil {
    static String doPost(String url, String body) {
        HttpURLConnection conn = null;
        InputStream in = null;
        BufferedReader reader = null;
        OutputStreamWriter out = null;
        StringBuilder result = new StringBuilder();
        try {
            URL readUrl = new URL(url);
            conn = (HttpURLConnection) readUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(2000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(body);
            out.flush();
            in = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw new HttpRequestException("网络请求错误", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                throw new HttpRequestException("网络请求错误", e);
            }
            return result.toString();
        }
    }
}
