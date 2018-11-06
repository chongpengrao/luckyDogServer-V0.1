package server;
/**
 * 处理客户端请求，并作出响应
 */

import common.HttpContext;
import common.ServletContext;
import http.HttpRequest;
import http.HttpResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            HttpRequest request = new HttpRequest(socket.getInputStream());
            //todo:get请求时获取请求参数
            //request.getParamMap();
            //post请求时请求体
            String requestEntity = request.getRequestEntity();
            HttpResponse response = new HttpResponse(socket.getOutputStream());
            response.setProtocol(ServletContext.protocol);
            File file = getRealFileAndResponseStatus(response,request.getUri());
            response.setContentType(getContentTypeByFile(file));
            response.setContentLength((int) file.length());

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] bs = new byte[(int) file.length()];
            bufferedInputStream.read(bs);
            //输出页面
            response.getOutputStream().write(bs);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getRealFileAndResponseStatus(HttpResponse response, String uri) {
        if ("/".equals(uri)){
            response.setStatus(HttpContext.CODE_OK);
            return new File(ServletContext.webRoot+"/"+ServletContext.homePage);
        }
        String pathName = ServletContext.webRoot+uri;
        File file = new File(pathName);
        if (!file.exists()){
            file = new File(ServletContext.webRoot+"/"+ServletContext.notFoundPage);
            response.setStatus(HttpContext.CODE_NOTFOUND);
        }else {
            response.setStatus(HttpContext.CODE_OK);
        }
        return file;
    }


    private String getContentTypeByFile(File file) {
        String name = file.getName();
        String key = name.substring(name.lastIndexOf(".") + 1);
        return ServletContext.typeMap.get(key);
    }
}
