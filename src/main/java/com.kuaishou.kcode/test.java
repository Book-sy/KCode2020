package com.kuaishou.kcode;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;

/**
 * test��
 * Git to�� http://hs.mccspace.com:3000/Qing_ning/kcode-warm-up/
 *
 * @TIME 2020/5/27 19:36
 * @AUTHOR ��˶~
 */

public class test {

    public static void main(String[] ar) throws IOException {

        long startTime=System.currentTimeMillis();
        readNIO();


    }

    public static void readNIO() {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File("D:\\warmup-test.data"));
            FileChannel channel = fin.getChannel();

            int capacity = 1000;// �ֽ�
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            System.out.println("�����ǣ�" + bf.limit() + ",�����ǣ�" + bf.capacity() + " ,λ���ǣ�" + bf.position());
            int length = -1;

            while ((length = channel.read(bf)) != -1) {

                /*
                 * ע�⣬��ȡ�󣬽�λ����Ϊ0����limit��Ϊ����, �Ա��´ζ��뵽�ֽڻ����У���0��ʼ�洢
                 */
                bf.clear();
                byte[] bytes = bf.array();
                System.out.println("start..............");

                String str = new String(bytes, 0, length);
                //System.out.println(str);
                //System.out.write(bytes, 0, length);

                System.out.println("end................");

                System.out.println("�����ǣ�" + bf.limit() + "�����ǣ�" + bf.capacity() + "λ���ǣ�" + bf.position());

            }

            channel.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
