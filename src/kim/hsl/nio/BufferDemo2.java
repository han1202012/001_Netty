package kim.hsl.nio;

import java.nio.ByteBuffer;

public class BufferDemo2 {
    public static void main(String[] args) {
        //1 . 创建一个存储 Int 类型数据的 Buffer , 可以存储 1024 个字节
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //2 . 向缓冲区中放入数据
        buffer.putInt(8888);
        buffer.putDouble(88.888);
        buffer.putShort((short) 888);

        //3 . 写入转读取前先翻转, 将 position 设置为 0
        buffer.flip();

        //4 . 从缓冲区中读取数据
        int intValue = buffer.getInt();
        double doubleValue = buffer.getDouble();
        short shortValue = buffer.getShort();
        //已经读取完了, 在读取就溢出了 java.nio.BufferUnderflowException
        //buffer.getInt();

        //5 . 打印读取的数据信息
        System.out.println(String.format("intValue = %d, doubleValue = %f, shortValue = %d",
                intValue, doubleValue, shortValue));
    }
}