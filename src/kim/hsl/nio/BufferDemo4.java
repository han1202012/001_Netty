package kim.hsl.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 分散 ( Scattering ) 与 聚合 ( Gathering ) 示例
 *
 * 分散 ( Scattering ) : 通道 ( Channel ) 向 缓冲区数组 中写出数据,
 *                       按照索引从第 0 个缓冲区 ( Buffer ) 开始, 依次写入数据
 * 聚合 ( Gathering ) : 通道 ( Channel ) 从 缓冲区数组 中读取数据,
 *                      按照索引从第 0 个缓冲区 ( Buffer ) 开始, 依次读取数据
 *
 * 使用 服务器套接字通道 ( ServerSocketChannel ) 和 套接字通道 ( SocketChannel ) 进行聚合演示
 * 使用 文件通道 ( FileChannel ) 进行分散演示
 *
 * @author han
 *
 */
public class BufferDemo4 {
    public static void main(String[] args) {

        try {
            //1 . 创建 ServerSocketChannel, 绑定本地端口
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
            serverSocketChannel.socket().bind(inetSocketAddress);

            //2 . 创建 2 个 ByteBuffer, 并放入数组中
            ByteBuffer[] buffers = new ByteBuffer[2];
            buffers[0] = ByteBuffer.allocate(4);
            buffers[1] = ByteBuffer.allocate(8);

            //3 . ServerSocketChannel 阻塞等待客户端连接, 该方法执行后一直阻塞
            SocketChannel socketChannel = serverSocketChannel.accept();

            //4 . 阻塞读取数据, 将数据读取到 buffers 缓冲区数组中的缓冲区中
            socketChannel.read(buffers);

            //5 . 将两个 缓冲区 flip 翻转一下, position 设置为 0
            buffers[0].flip();
            buffers[1].flip();

            //6 . 将读取到的数据写出到文件中
            FileOutputStream fos = null;
            try {
                //1 . FileChannel 可以从 FileOutputStream 中获取
                fos = new FileOutputStream("file4.txt");

                //2 . 创建 FileChannel , 从 FileInputStream / FileOutputStream 中可以获取到
                //FileChannel 是抽象类 , 实际类型是 FileChannelImpl
                FileChannel fcOut = fos.getChannel();

                //3 .  将读取到的数据写出到文件中
                fcOut.write(buffers);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}