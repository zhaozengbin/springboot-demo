package com.zzb.jms.rabbit.mq.consume.handler;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：QueueService
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/24 6:32 下午
 * 修改备注：TODO
 */
@Service
public class QueueService {
    private static final Log LOG = Log.get(QueueService.class);

    private String username = "admin";
    private String password = "admin";
    private String url = "http://localhost:15672/api/queues";

    private CloseableHttpClient getBasicHttpClient() {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置BasicAuth
        CredentialsProvider provider = new BasicCredentialsProvider();
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        provider.setCredentials(scope, credentials);
        httpClientBuilder.setDefaultCredentialsProvider(provider);
        return httpClientBuilder.build();
    }

    /**
     * 根据API获得相关的MQ信息
     *
     * @return
     */

    public List<String> getMQJSONArray() {
        HttpGet httpPost = new HttpGet(url);
        CloseableHttpClient pClient = getBasicHttpClient();
        CloseableHttpResponse response = null;
        JSONArray jsonArray = null;
        List queueNameList = new ArrayList();
        try {
            response = pClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                String string = EntityUtils.toString(response.getEntity());
                jsonArray = (JSONArray) JSONObject.parse(string);
                if (null != jsonArray) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String name = (String) jsonArray.getJSONObject(i).get("name");
                        queueNameList.add(name);
                    }
                    System.out.println(queueNameList);
                    return queueNameList;
                }
            } else {
                System.out.println("请求返回:" + state + "(" + url + ")");
            }
        } catch (Exception e) {
            LOG.error("地址url:" + url + "，异常：" + e.toString());
        } finally {
            closeAll(response, pClient);
        }
        return queueNameList;
    }

    public void closeAll(CloseableHttpResponse response, CloseableHttpClient pClient) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
