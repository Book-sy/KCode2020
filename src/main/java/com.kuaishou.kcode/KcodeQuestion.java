package com.kuaishou.kcode;

import java.io.*;
import java.util.*;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeQuestion {

    private Map<String, Map<Integer, List<Integer>>> map = new HashMap();

    private static long REDUCEVALUE = 1589700000000L;
    private static long num = 0;

    /**
     * prepare() 方法用来接受输入数据集，数据集格式参考README.md
     *
     * @param inputStream
     */
    public void prepare(InputStream inputStream) {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            Date start = new Date();
            while ((line = br.readLine()) != null) {
                String[] a = line.split(",");
                String key = a[1];
                long time = Long.parseLong(a[0]);
                Map<Integer, List<Integer>> list = map.get(key);
                if(list==null){
                    list = new HashMap();
                    List z = new ArrayList();
                    z.add(Integer.parseInt(a[2]));
                    list.put((int)(time/1000), z);
                    map.put(key, list);
                } else {
                    List z = list.get((int)(time/1000));
                    if(z == null){
                        z = new ArrayList();
                        z.add(Integer.parseInt(a[2]));
                        list.put((int)(time/1000), z);
                    } else {
                        z.add(Integer.parseInt(a[2]));
                    }
                }
                if(++num%1000000==0) {
                    System.out.println("成功读取" + num + "条数据，总耗时" + (new Date().getTime() - start.getTime()) + "ms");
                }
            }
            System.out.println("读取完毕，总共"+num+"条数据(我太难了)，总耗时"+(new Date().getTime()-start.getTime())+"ms");
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

    /**
     * getResult() 方法是由kcode评测系统调用，是评测程序正确性的一部分，请按照题目要求返回正确数据
     * 输入格式和输出格式参考 README.md
     *
     * @param timestamp 秒级时间戳
     * @param methodName 方法名称
     */
    public String getResult(Long timestamp, String methodName) {
        int QPS,P99,P50,AVG,MAX;
        List<Integer> cz = map.get(methodName).get(timestamp.intValue());
        Collections.sort(cz);
        QPS = cz.size();

        P99 = getPoint(cz,0.99);
        P50 = getPoint(cz, 0.5);


        int sum = 0;
        for(int z:cz)
            sum+=z;
        AVG = (int)Math.ceil(1.0*sum/QPS);
        MAX = cz.get(QPS-1);

        return QPS+","+P99+","+P50+","+AVG+","+MAX;
    }

    private int getPoint(List<Integer> cz,double p){
        double i = cz.size()*p;
        if(i-(int)i == 0){
            return cz.get((int)i-1);
            //return (int)Math.ceil((double)(cz.get((int)i)+cz.get((int)i-1))/2);
        } else {
            return cz.get((int)Math.ceil(i-1));
        }
    }
}
