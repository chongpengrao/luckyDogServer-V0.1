package server;

import common.ServletContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代表服务器：接收客户端请求并响应
 */
public class WebServer {

    private ServerSocket server;

    private ExecutorService threadPool;

    public WebServer(){
        try {
            server = new ServerSocket(ServletContext.port);
            threadPool = Executors.newFixedThreadPool(ServletContext.maxThread);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start(){
        try {
            while (true){
                //接收客户端请求
                Socket socket = server.accept();
                //根据请求向客户端做出响应
                ClientHandler clientHandler = new ClientHandler(socket);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
