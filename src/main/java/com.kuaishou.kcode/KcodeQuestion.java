package com.kuaishou.kcode;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeQuestion {

    private Map<String, Map<Integer, List<Integer>>> map = new HashMap();

    private static final long REDUCEVALUE = 1589700000000L;
    private static long num = 0;

    /**
     * prepare() �������������������ݼ������ݼ���ʽ�ο�README.md
     *
     * @param inputStream
     */
    public void prepare(InputStream inputStream) {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            List<String> data = new ArrayList<>();

            ExecutorService es = Executors.newFixedThreadPool(16);

            while ((line = br.readLine()) != null) {
                data.add(line);

                if(++num%50000==0){
                    es.submit(new updataTest(data));
                    data = new ArrayList<>();
                }
            }
            es.submit(new updataTest(data));
            es.shutdown();
            es.awaitTermination(60,TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
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
     * getResult() ��������kcode����ϵͳ���ã������������ȷ�Ե�һ���֣��밴����ĿҪ�󷵻���ȷ����
     * �����ʽ�������ʽ�ο� README.md
     *
     * @param timestamp  �뼶ʱ���
     * @param methodName ��������
     */
    public String getResult(Long timestamp, String methodName) {

        int QPS, P99, P50, AVG, MAX;
        List<Integer> cz = map.get(methodName).get(timestamp.intValue());
        Collections.sort(cz);
        QPS = cz.size();


        double i = QPS * 0.99;
        if (i - (int) i == 0) {
            P99 =  cz.get((int) i - 1);
            //return (int)Math.ceil((double)(cz.get((int)i)+cz.get((int)i-1))/2);
        } else {
            P99 = cz.get((int) Math.ceil(i - 1));
        }

        i = QPS * 0.5;
        if (i - (int) i == 0) {
            P50 = cz.get((int) i - 1);
            //return (int)Math.ceil((double)(cz.get((int)i)+cz.get((int)i-1))/2);
        } else {
            P50 = cz.get((int) Math.ceil(i - 1));
        }


        int sum = 0;
        for (int z : cz)
            sum += z;
        AVG = (int) Math.ceil(1.0 * sum / QPS);
        MAX = cz.get(QPS - 1);

        return QPS + "," + P99 + "," + P50 + "," + AVG + "," + MAX;
    }

    class updataTest implements Runnable{

        private List<String> data;

        public updataTest(List<String> data) {
            this.data = data;
        }

        @Override
        public void run() {
            Map<String, Map<Integer, List<Integer>>> map = new HashMap();
            for(String line: data){
                String[] a = line.split(",");
                String key = a[1];
                long time = Long.parseLong(a[0]);
                Map<Integer, List<Integer>> list = map.get(key);
                if (list == null) {
                    list = new HashMap();
                    List z = new ArrayList();
                    z.add(Integer.parseInt(a[2]));
                    list.put((int) (time / 1000), z);
                    map.put(key, list);
                } else {
                    List z = list.get((int) (time / 1000));
                    if (z == null) {
                        z = new ArrayList();
                        z.add(Integer.parseInt(a[2]));
                        list.put((int) (time / 1000), z);
                    } else {
                        z.add(Integer.parseInt(a[2]));
                    }
                }
            }
            synchronized (KcodeQuestion.this) {
                for(String i:map.keySet()){
                    if(KcodeQuestion.this.map.get(i)==null){
                        KcodeQuestion.this.map.put(i,map.get(i));
                    } else {
                        for(Integer j:map.get(i).keySet()){
                            if(KcodeQuestion.this.map.get(i).get(j)==null){
                                KcodeQuestion.this.map.get(i).put(j,map.get(i).get(j));
                            } else {
                                KcodeQuestion.this.map.get(i).get(j).addAll(map.get(i).get(j));
                            }
                        }
                    }
                }
            }
        }
    }
}