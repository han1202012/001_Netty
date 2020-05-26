package kim.hsl.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo3 {
    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //1 . FileChannel 可以从 FileInputStream 中获取
            fis = new FileInputStream("file.txt");
            fos = new FileOutputStream("file2.txt");

            //2 . 创建 FileChannel , 从 FileInputStream / FileOutputStream 中可以获取到
            //FileChannel 是抽象类 , 实际类型是 FileChannelImpl
            FileChannel fcIn = fis.getChannel();
            FileChannel fcOut = fos.getChannel();

            //3 . FileChannel 需要通过 缓冲区 Buffer 才能 读写文件
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //4 . 读取 file.txt 文件数据到 字节缓冲区 ByteBuffer , 并写出到 file2.txt 文件中
            //循环退出条件 : 如果 文件 读取完毕, read 方法会返回 -1, 代表读取文件完毕
            while ( (fcIn.read(buffer)) >= 0 ){
                //将 ByteBuffer 中的数据写出 file2.txt 文件中
                //翻转后, 将 position 设置成 0, 才能开始写出
                buffer.flip();
                fcOut.write(buffer);
                //重置标志位, 供下一次循环使用
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null)
                    fis.close();
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}