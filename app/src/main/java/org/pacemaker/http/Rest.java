package org.pacemaker.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Rest {
    private static DefaultHttpClient httpClient = null;

    //URL used for communicating via REST - Changes based on where the original Play Application is ran from
//    public static String URL = "http://172.20.10.2:9000";
    public static String URL = "http://176.61.13.223:9000";
//    public static String URL = "http://192.168.1.79:9000";
//    public static String URL = "http://10.5.5.2:9000";

    /**
     * Set up te http client
     *
     * @return
     */
    private static DefaultHttpClient httpClient() {
        if (httpClient == null) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
            HttpConnectionParams.setSoTimeout(httpParameters, 10000);
            httpClient = new DefaultHttpClient(httpParameters);
        }
        return httpClient;
    }

    /**
     * Method used to execute a GET request
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String get(String path) throws Exception {
        HttpGet getRequest = new HttpGet(URL + path);
        getRequest.setHeader("accept", "application/json");
        HttpResponse response = httpClient().execute(getRequest);
        return new BasicResponseHandler().handleResponse(response);
    }

    /**
     * Method used to perform a DELETE request
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String delete(String path) throws Exception {
        HttpDelete deleteRequest = new HttpDelete(URL + path);
        HttpResponse response = httpClient().execute(deleteRequest);
        return new BasicResponseHandler().handleResponse(response);
    }

    /**
     * Method used to perform and PUT request
     *
     * @param path
     * @param json
     * @return
     * @throws Exception
     */
    public static String put(String path, String json) throws Exception {
        HttpPut putRequest = new HttpPut(URL + path);
        putRequest.setHeader("Content-type", "application/json");
        putRequest.setHeader("accept", "application/json");

        StringEntity s = new StringEntity(json);
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        putRequest.setEntity(s);

        HttpResponse response = httpClient().execute(putRequest);
        return new BasicResponseHandler().handleResponse(response);
    }

    /**
     * Method used to perform a POST request
     *
     * @param path
     * @param json
     * @return
     * @throws Exception
     */
    public static String post(String path, String json) throws Exception {
        HttpPost putRequest = new HttpPost(URL + path);
        putRequest.setHeader("Content-type", "application/json");
        putRequest.setHeader("accept", "application/json");

        StringEntity s = new StringEntity(json);
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        putRequest.setEntity(s);

        HttpResponse response = httpClient().execute(putRequest);
        return new BasicResponseHandler().handleResponse(response);
    }
}