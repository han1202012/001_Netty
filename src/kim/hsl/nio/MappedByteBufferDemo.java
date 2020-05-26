package kim.hsl.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferDemo {
    public static void main(String[] args) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile("file.txt", "rw");
            FileChannel fc = randomAccessFile.getChannel();

            //FileChannel.MapMode.READ_WRITE : 指的是读写模式
            //0 : 将文件从 0 位置开始映射到内存中
            //10 : 将文件从 0 位置开始映射到内存中的大小
            //即 将 file.txt 文件从 0 开始的 10 字节映射到内存中
            MappedByteBuffer mappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 10);

            mappedByteBuffer.put(0, (byte) 'N');
            mappedByteBuffer.put(1, (byte) 'N');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(randomAccessFile != null)
                    randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}