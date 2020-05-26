package kim.hsl.nio;

import java.nio.IntBuffer;

public class BufferDemo {
    public static void main(String[] args) {
        //1 . 创建一个存储 Int 类型数据的 Buffer , 可以存储 8 个 Int 数据
        IntBuffer buffer = IntBuffer.allocate(8);

        //2 . 设置 只 读写 3 个元素
        buffer.limit(3);

        //3 . 向 Buffer 中写入数据
        for(int i = 0; i < buffer.limit(); i ++){
            buffer.put(i);
        }

        //从 Buffer 中取出数据
        //4 . 先将 Buffer 翻转一下 , 然后读取 , 读出的数据与存储的数据顺序一样
        buffer.flip();

        //5 . 循环读取 buffer 中的 Int 数据, 维护了一个索引 ,
        //代表当前操作的数据索引 , 即 position
        while (buffer.hasRemaining()){
            System.out.println("position " + buffer.position() + " . " + buffer.get());
        }
    }
}
