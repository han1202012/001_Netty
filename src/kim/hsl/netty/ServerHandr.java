package kim.hsl.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Handler 处理者, 是 NioEventLoop 线程中处理业务逻辑的类
 *
 * 继承 : 该业务逻辑处理者 ( Handler ) 必须继承 Netty 中的 ChannelInboundHandlerAdapter 类
 * 才可以设置给 NioEventLoop 线程
 *
 * 规范 : 该 Handler 类中需要按照业务逻辑处理规范进行开发
 */
public class ServerHandr extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据 : 在服务器端读取客户端发送的数据
     * @param ctx
     *      通道处理者上下文对象 : 封装了 管道 ( Pipeline ) , 通道 ( Channel ), 客户端地址信息
     *      管道 ( Pipeline ) : 注重业务逻辑处理 , 可以关联很多 Handler
     *      通道 ( Channel ) : 注重数据读写
     * @param msg
     *      客户端上传的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 查看 ChannelHandlerContext 中封装的内容
        System.out.println("channelRead : ChannelHandlerContext ctx = " + ctx);

        // 将客户端上传的数据转为 ByteBuffer
        // 这里注意该类是 Netty 中的 io.netty.buffer.ByteBuf 类
        // 不是 NIO 中的 ByteBuffer
        // io.netty.buffer.ByteBuf 性能高于 java.nio.ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        // 将 ByteBuf 缓冲区数据转为字符串, 打印出来
        System.out.println(ctx.channel().remoteAddress() + " 接收到客户端发送的数据 : " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 服务器端读取数据完毕后回调的方法
     * @param ctx
     *      通道处理者上下文对象 : 封装了 管道 ( Pipeline ) , 通道 ( Channel ), 客户端地址信息
     *      *      管道 ( Pipeline ) : 注重业务逻辑处理 , 可以关联很多 Handler
     *      *      通道 ( Channel ) : 注重数据读写
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 数据编码 : 将字符串编码, 存储到 io.netty.buffer.ByteBuf 缓冲区中
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello Client", CharsetUtil.UTF_8);

        // 写出并刷新操作 : 写出数据到通道的缓冲区 ( write ), 并执行刷新操作 ( flush )
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 异常处理 , 上面的方法中都抛出了 Exception 异常, 在该方法中进行异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("通道异常, 关闭通道");
        //如果出现异常, 就关闭该通道
        ctx.close();
    }
}
