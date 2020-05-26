package kim.hsl.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo2 {
    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            //1 . FileChannel 可以从 FileInputStream 中获取
            fis = new FileInputStream("file.txt");

            //2 . 创建 FileChannel , 从 FileInputStream 中可以获取到
            //FileChannel 是抽象类 , 实际类型是 FileChannelImpl
            FileChannel fc = fis.getChannel();

            //3 . FileChannel 需要通过 缓冲区 Buffer 才能与数据进行读写交互
            ByteBuffer buffer = ByteBuffer.allocate(32);

            //4 . 将 字节缓冲区 ByteBuffer 中的数据写入到 文件通道 FileChannel 中
            int len = fc.read(buffer);

            //5 . 将数据从缓冲区中取出
            byte[] stringData = new byte[len];
            //注意 : ByteBuffer 需要 flip 翻转后才能读取
            buffer.flip();
            buffer.get(stringData);

            //6 . byte 数组数据转为字符串并打印出来
            String fileString = new String(stringData);
            System.out.println(fileString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
