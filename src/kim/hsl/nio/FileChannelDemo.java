package kim.hsl.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {
    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            String hello = "Hello World";

            //1 . FileChannel 可以从 FileOutputStream 中获取
            fos = new FileOutputStream("file.txt");

            //2 . 创建 FileChannel , 从 FileOutputStream 中可以获取到
            //FileChannel 是抽象类 , 实际类型是 FileChannelImpl
            FileChannel fc = fos.getChannel();

            //3 . FileChannel 需要通过 缓冲区 Buffer 才能与数据进行读写交互
            ByteBuffer buffer = ByteBuffer.allocate(32);
            //将数据放入缓冲区中 , flip 方法作用是将 position 位置设置 0 , 修改缓冲区也会修改数组
            buffer.put(hello.getBytes());
            buffer.flip();

            //4 . 将 字节缓冲区 ByteBuffer 中的数据写入到 文件通道 FileChannel 中
            fc.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
