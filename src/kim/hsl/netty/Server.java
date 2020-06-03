package kim.hsl.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty 案例服务器端
 */
public class Server {
    public static void main(String[] args) {

        // 1. 创建 BossGroup 线程池 和 WorkerGroup 线程池, 其中维护 NioEventLoop 线程
        //     NioEventLoop 线程中执行无限循环操作

        // BossGroup 线程池 : 负责客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // WorkerGroup 线程池 : 负责客户端连接的数据读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 2. 服务器启动对象, 需要为该对象配置各种参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup) // 设置 主从 线程组 , 分别对应 主 Reactor 和 从 Reactor
                .channel(NioServerSocketChannel.class)  // 设置 NIO 网络套接字通道类型
                .option(ChannelOption.SO_BACKLOG, 128)  // 设置线程队列维护的连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true)  // 设置连接状态行为, 保持连接状态
                .childHandler(  // 为 WorkerGroup 线程池对应的 NioEventLoop 设置对应的事件 处理器 Handler
                        new ChannelInitializer<SocketChannel>() {// 创建通道初始化对象
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                // 为 管道 Pipeline 设置处理器 Hanedler
                                ch.pipeline().addLast(new ServerHandr());
                            }
                        }
                );

        ChannelFuture cf = null;
        try {
            // 绑定本地端口, 进行同步操作 , 并返回 ChannelFuture
            cf = bootstrap.bind(8888).sync();
            // 关闭通道 , 开始监听操作
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 出现异常后, 优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}