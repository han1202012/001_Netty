package kim.hsl.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args) {
        // 客户端只需要一个 时间循环组 , 即 NioEventLoopGroup 线程池
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 客户端启动对象
        Bootstrap bootstrap = new Bootstrap();
        // 设置相关参数
        bootstrap.group(eventLoopGroup)     // 设置客户端的线程池
                .channel(NioSocketChannel.class)    // 设置客户端网络套接字通道类型
                .handler(   // 设置客户端的线程池对应的 NioEventLoop 设置对应的事件处理器 Handler
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ClientHandr());
                            }
                        }
                );

        try {
            // 开始连接服务器, 并进行同步操作
            // ChannelFuture 类分析 , Netty 异步模型
            // sync 作用是该方法不会再次阻塞
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();
            System.out.println("客户端连接服务器成功 ...");

            // 关闭通道, 开始监听
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 优雅的关闭
            eventLoopGroup.shutdownGracefully();
        }
    }
}