package com.kuaishou.kcode;

import java.io.*;
import java.util.*;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeMain {

    public static void main(String[] args) throws Exception {
        Date start = new Date();
        // "demo.data" 是你从网盘上下载的测试数据，这里直接填你的本地绝对路径
        InputStream fileInputStream = new FileInputStream("I:\\data\\warmup-test.data");
        KcodeQuestion question = new KcodeQuestion();
        // 准备数据
        question.prepare(fileInputStream);
        System.out.println("读取数据完成，总耗时:"+(new Date().getTime()-start.getTime()));
        // 验证正确性
        BufferedReader br = null;
        try {
            int success=0,fail=0;
            InputStream fis = new FileInputStream("I:\\data\\result-test.data");
            br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] s = line.split("\\|");
                String[] s1 = s[0].split(",");
                String result = question.getResult(Long.parseLong(s1[0]), s1[1]);
                if (s[1].equals(result)) {
                    success++;
                    //System.out.println("success");
                } else {
                    fail++;
                    System.out.println("fail:  "+s[1]+"  "+result);
                    question.getResult(Long.parseLong(s1[0]), s1[1]);
                }
            }
            System.out.println("正确/失败: "+success+"/"+fail);
            System.out.println("验证完成，总耗时:"+(new Date().getTime()-start.getTime()));
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}