package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装http相关的请求信息
 */
public class HttpRequest {

    private String method;//请求方式
    private String uri;//请求资源路径
    private String protocol;//请求协议
    private String requestEntity;//请求体

    private Map<String,String> paramMap = new HashMap<String, String>();

    public HttpRequest(InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            //请求行：  GET /index.html HTTP/1.1
            String requestLine = reader.readLine();
            String[] requestLineArray = requestLine.split(" ");
            method = requestLineArray[0];
            uri = requestLineArray[1];
            protocol = requestLineArray[2];
            if ("POST".equals(method)){
                while(true){
                    String nextLine = reader.readLine();
                    if (nextLine==null || "".equals(nextLine)){
                        requestEntity = reader.readLine();
                        break;
                    }
                }
            }
            if (uri != null && uri.contains("?")){
                String[] requestParams = uri.split("?")[1].split("&");
                for (String str : requestParams){
                    paramMap.put(str.split("=")[0],str.split("=")[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getParamValue(String key){
        return paramMap.get(key);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {

        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public String getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(String requestEntity) {
        this.requestEntity = requestEntity;
    }
}
