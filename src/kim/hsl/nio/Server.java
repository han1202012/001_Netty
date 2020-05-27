package kim.hsl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) {

        try {
            //创建 ServerSocketChannel, 等价于 BIO 中的 ServerSocket
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //绑定本地端口, 获取其内部封装的 ServerSocket, 绑定 ServerSocket 的 8888 端口
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(8888));

            //设置网络通信非阻塞模式
            serverSocketChannel.configureBlocking(false);


            //获取 选择器 ( Selector )
            Selector selector = Selector.open();
            //将 serverSocketChannel 通道注册给 选择器 ( Selector ), 这里注册连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                //查看是否有事件触发, 如果有就在下面处理, 如果没有 continue 终止循环, 继续下一次循环
                if(selector.select(1000) <= 0){
                    //阻塞 100 毫秒, 等待是否有事件发生, 如果返回 0, 说明没有事件发生
                    System.out.println("服务器等待 1000 毫秒");
                    continue;
                }

                //如果能执行到该位置, 说明 selector.select(1000) 方法返回值大于 0
                //当前有 1 个或多个事件触发, 下面就是处理事件的逻辑

                //获取当前发生的事件的 SelectionKey 集合, 通过 SelectionKey 可以获取对应的 通道
                Set<SelectionKey> keys =  selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()){

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
