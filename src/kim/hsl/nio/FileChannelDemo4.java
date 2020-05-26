package kim.hsl.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannelDemo4 {
    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //1 . FileChannel 可以从 FileInputStream 中获取
            fis = new FileInputStream("file.txt");
            fos = new FileOutputStream("file3.txt");

            //2 . 创建 FileChannel , 从 FileInputStream / FileOutputStream 中可以获取到
            //FileChannel 是抽象类 , 实际类型是 FileChannelImpl
            FileChannel fcIn = fis.getChannel();
            FileChannel fcOut = fos.getChannel();

            //3 .  直接将 fcIn 通道的内容写出到 fcOut 通道
            fcOut.transferFrom(fcIn, 0, fcIn.size());
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