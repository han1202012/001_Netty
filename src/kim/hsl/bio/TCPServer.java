package kim.hsl.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器端 : 编写服务器端 , 监听 8888 端口 , 阻塞等待客户端连接 , 连接成功后 , 创建线程 , 线程中阻塞等待客户端发送请求数据 ;
 * 客户端 : 编写一个客户端 , 请求服务器的 8888 端口号 , 客户端发送 "Hello World" 字符串给服务器端 ;
 * Telnet 客户端 : 使用 Telnet 客户端向上述服务器端 8888 端口 发送 "Hello World" 字符串请求 ;
 */
public class TCPServer {
    public static void main(String[] args) {
        try {
            //创建线程池
            ExecutorService threadPool = Executors.newCachedThreadPool();
            //创建服务器套接字
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("服务器启动,监听 8888 端口");
            while (true){
                //阻塞, 等待客户端连接请求 ( 此处是第一个阻塞点 )
                Socket socket = serverSocket.accept();
                System.out.println("客户端连接成功");
                //线程池启动线程
                threadPool.execute(new ClientRquest(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 与客户端交互类
     */
    static class ClientRquest implements Runnable {
        private Socket socket;
        public ClientRquest(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                clientRequest();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //最终要将 Socket 关闭, 如果出异常继续捕获
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void clientRequest() throws IOException {
            //获取输入流, 读取客户端写入的信息
            byte[] buffer = new byte[1024];
            InputStream is = socket.getInputStream();

            System.out.println("等到客户端请求");
            //此处会阻塞等待客户端的请求 ( 此处是第二个阻塞点 )
            int count = is.read(buffer);
            String request = new String(buffer, 0, count);
            System.out.println("客户端请求到达 : " + request);
        }
    }
}
