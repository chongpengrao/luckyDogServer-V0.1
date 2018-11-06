package http;

import common.HttpContext;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 封裝http响应相关参数
 */
public class HttpResponse {
    private String protocol;//协议名
    private int status;//状态码
    private String contentType;//响应数据的格式
    private int contentLength;//响应数据的长度

    //存放状态码和描述信息
    private Map<Integer,String> httpMap = new HashMap<Integer, String>();

    private OutputStream outputStream;

    public HttpResponse(OutputStream outputStream){
        this.outputStream = outputStream;
        httpMap.put(HttpContext.CODE_OK, HttpContext.DESC_OK);
        httpMap.put(HttpContext.CODE_NOTFOUND, HttpContext.DESC_NOTFOUND);
        httpMap.put(HttpContext.CODE_ERROR, HttpContext.DESC_ERROR);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    //保证响应头只被发送一次
    private boolean isSend;

    public OutputStream getOutputStream() {
        if (!isSend){
            PrintStream printStream = new PrintStream(outputStream);
            //状态行
            printStream.println(protocol + " " + status + " "
                    + httpMap.get(status));
            printStream.println("Content-Type:" + contentType);
            printStream.println("Content-Length:" + contentLength);
            printStream.println();
            //将响应头状态改为已发送
            isSend = true;
        }
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
