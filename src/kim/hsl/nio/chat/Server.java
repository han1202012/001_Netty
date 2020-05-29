package kim.hsl.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 聊天室服务器端
 *
 * @author hsl
 * @date 2020-05-29 17:24
 */
public class Server {

    /**
     * 服务器监听的端口号
     */
    public static final int PORT = 8888;

    /**
     * 监听 ServerSocketChannel 通道和各个客户端对应的 SocketChannel 通道
     */
    private Selector selector;

    /**
     * 服务器端的套接字通道, 相当于 BIO 中的 ServerSocket
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * 初始化服务器相关操作
     */
    public Server() {
        initServerSocketChannelAndSelector();
    }

    /**
     * 初始化 服务器套接字通道 和
     */
    private void initServerSocketChannelAndSelector() {
        try {
            // 创建并配置 服务器套接字通道 ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            // 获取选择器, 并注册 服务器套接字通道 ServerSocketChannel
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Selector 开始执行 监听工作
     */
    private void selectorStartSelectOperation() {
        try {
            while (true) {
                // 阻塞监听, 如果有事件触发, 返回触发的事件个数
                // 被触发的 SelectionKey 事件都存放到了 Set<SelectionKey> selectedKeys 集合中
                // 下面开始遍历上述 selectedKeys 集合
                int eventTriggerCount = selector.select();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
