package kim.hsl.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import kim.hsl.netty.ServerHandler;

/**
 * HTTP 服务器
 * 客户端使用浏览器访问即可
 */
public class HTTPServer {
    public static void main(String[] args) {
        // 1. 创建 BossGroup 线程池 和 WorkerGroup 线程池, 其中维护 NioEventLoop 线程
        //     NioEventLoop 线程中执行无限循环操作

        // BossGroup 线程池 : 负责客户端的连接
        // 指定线程个数 : 客户端个数很少, 不用很多线程维护, 这里指定线程池中线程个数为 1
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
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
                                // 该方法在服务器与客户端连接建立成功后会回调
                                // 获取管道
                                ChannelPipeline pipeline = ch.pipeline();

                                // 为管道加入 HTTP 协议的编解码器 HttpServerCodec,
                                // codec 中的 co 是 coder 编码器的意思, dec 是 decoder 解码器的意思
                                // 第一个字符串是编解码器的名称
                                pipeline.addLast("HttpServerCodec" , new HttpServerCodec());

                                // 为管道 Pipeline 设置处理器 Hanedler
                                pipeline.addLast("HTTPServerHandler", new HTTPServerHandler());

                                // 管道初始化完成
                                System.out.println("管道初始化完成!");
                            }
                        }
                );
        System.out.println("HTTP 服务器准备完毕 ...");


        ChannelFuture channelFuture = null;
        try {
            // 绑定本地端口, 进行同步操作 , 并返回 ChannelFuture
            channelFuture = bootstrap.bind(8888).sync();
            System.out.println("HTTP 服务器开始监听 8888 端口 ...");

            //获取管道
            ChannelPipeline channelPipeline = channelFuture.channel().pipeline();

            // 获取并关闭通道 , 开始监听操作
            channelFuture.channel().closeFuture().sync();
            System.out.println("服务器准备完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 出现异常后, 优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}