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
        // "demo.data" ��������������صĲ������ݣ�����ֱ������ı��ؾ���·��
        InputStream fileInputStream = new FileInputStream("D:\\warmup-test.data");
        KcodeQuestion question = new KcodeQuestion();
        // ׼������
        question.prepare(fileInputStream);
        System.out.println("��ȡ������ɣ��ܺ�ʱ:"+(-(start.getTime()-(start = new Date()).getTime())));
        // ��֤��ȷ��
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
                    System.out.println("fail:  "+s1[0]+" "+s1[1]+" "+s[1]+"  "+result);
                    question.getResult(Long.parseLong(s1[0]), s1[1]);
                }
            }
            System.out.println("��ȷ/ʧ��: "+success+"/"+fail);
            System.out.println("��֤��ɣ���֤��ʱ:"+(new Date().getTime()-start.getTime()));
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