package com.kuaishou.kcode;

import javafx.collections.transformation.SortedList;
import sun.awt.util.IdentityArrayList;

import javax.management.relation.RoleList;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeQuestion {

    private Map<Integer, Map<String, String>> map = new HashMap<>();

    //private Queue<Map<Integer, Map<String, List>>> q = new ConcurrentLinkedQueue<>();
    private ExecutorService es = Executors.newFixedThreadPool(32);

    private BlockingQueue<byte[]> datas = new LinkedBlockingQueue<>();

    private BlockingQueue<format> formatQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<updataTest> updataTestQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<byte[]> dataQueue = new LinkedBlockingQueue<>();

    private static int ls =0;

    public KcodeQuestion() {
        for(int i=0;i<32;i++)
            new format();
        for(int i=0;i<8;i++)
            new updataTest();
        try {
            for(int i=0;i<20;i++)
                dataQueue.put(new byte[20100]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * prepare() �������������������ݼ������ݼ���ʽ�ο�README.md
     *
     * @param inputStream
     */
    public void prepare(InputStream inputStream) {

        try {


            /**
            Thread addData = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(true){
                        Map<Integer, Map<String, List>> map = q.poll();
                        if(map == null){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if ((map = q.poll()) == null) {
                                break;
                            }
                        }
                        for(Integer i:map.keySet()){
                            if(KcodeQuestion.this.map.get(i)==null){
                                KcodeQuestion.this.map.put(i,map.get(i));
                            } else {
                                for(String j:map.get(i).keySet()){
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
            });  */

            /**
             new Thread(new Runnable() {
            @Override
            public void run() {
            while(!es.isShutdown()){

            ThreadPoolExecutor tpe = ((ThreadPoolExecutor) es);
            int activeCount = tpe.getQueue().size();
            System.out.println("��ǰ�Ŷ��߳�����"+ activeCount+"����ִ���߳���:"+tpe.getCompletedTaskCount());
            System.out.println("ʣ���ڴ棺"+(Runtime.getRuntime().freeMemory()/1024/1024)+"M");

            try {
            Thread.sleep(1000);
            } catch (InterruptedException e) {
            e.printStackTrace();
            }
            }
            }
            }).start();
             */


            Thread buffer = new Thread(new buffer());

            buffer.start();
            byte one[] = dataQueue.take();
            //addData.start();
            int next;
            byte i;
            while (inputStream.read(one, 0, 20000) > 0) {


                next = 20000;

                while (true) {
                    i = (byte) inputStream.read();
                    if(i == '\n' || i ==-1)
                        break;
                    one[next++] = i;
                }
                //System.out.println("�Ѵ�������");
                one[next++] = ' ';
                datas.offer(one);
                one = dataQueue.take();

            }
            //System.out.println("�ܹ���ȡ���ݰ�:"+ls2+"��");
            datas.offer(new byte[0]);
            //System.out.println("���������Զ�ȡ���");
            buffer.join();
            //addData.join();

            /**
            while(((ThreadPoolExecutor) es).getActiveCount() != 0){
                Thread.sleep(100);
            }
             */
            //System.out.println(a1+" "+a2+" "+a3);

            /**
            long a = new Date().getTime();
            ExecutorService es2 = Executors.newFixedThreadPool(16);
            for(Integer i:map.keySet()) {
                es2.submit(new getResultTest((long) i));
                //System.out.println("�����ִ���"+i+"�뼶����");
            }
            es2.shutdown();
            es2.awaitTermination(60,TimeUnit.SECONDS);
            System.out.println("�����뼶����ʱ�䣺"+(new Date().getTime()-a));
             */

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
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
    public String getResult(Long timestamp, String methodName){
        /**
         int num = 0;
         for(List i:map.get(1587987961).values()){
         num+=i.size();
         }
         System.out.println(num);
         */

        //try {
            return map.get(timestamp.intValue()).get(methodName);
            /**
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("");
        }

        return null;
             */
    }

    private class updataTest implements Runnable{

        private List<List> data;

        public updataTest(){
            try {
                updataTestQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
                Map map = new HashMap();
                long time = 0;
                for (List line : data) {

                    String key = (String)line.get(1);
                    time = (Integer)line.get(0);

                        List z = (List)map.get(key);
                        if (z == null) {
                            z = new ArrayList();
                            z.add(line.get(2));
                            map.put(key, z);
                        } else {
                            z.add(line.get(2));
                        }

                }
                /**
                q.offer(map);
                synchronized (KcodeQuestion.this) {
                    es.submit(new getResultTest(time, map));
                }
                 */

                int QPS, P99, P50, AVG, MAX;
                for (Object q : map.keySet()) {
                    List cz = (List) map.get(q);
                    Collections.sort(cz);
                    QPS = cz.size();


                    double i = QPS * 0.99;
                    if (i - (int) i == 0) {
                        P99 = (int) cz.get((int) i - 1);
                        //return (int)Math.ceil((double)(cz.get((int)i)+cz.get((int)i-1))/2);
                    } else {
                        P99 = (int) cz.get((int) Math.ceil(i - 1));
                    }

                    i = QPS * 0.5;
                    if (i - (int) i == 0) {
                        P50 = (int) cz.get((int) i - 1);
                        //return (int)Math.ceil((double)(cz.get((int)i)+cz.get((int)i-1))/2);
                    } else {
                        P50 = (int) cz.get((int) Math.ceil(i - 1));
                    }


                    int sum = 0;
                    for (Object z : cz)
                        sum += (int) z;
                    AVG = (int) Math.ceil(1.0 * sum / QPS);
                    MAX = (int) cz.get(QPS - 1);

                    map.put(q,""+QPS + "," + P99 + "," + P50 + "," + AVG + "," + MAX);
                }
                KcodeQuestion.this.map.put((int)time,map);

            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                updataTestQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void setData(List<List> data) {
            this.data = data;
        }
    }

    private class buffer implements Runnable {
        @Override
        public void run() {
            Future<List<List>> result;
                result = es.submit(new Callable<List<List>>() {
                    @Override
                    public List<List> call(){
                        return new ArrayList<>();
                    }
                });
            format f;
            byte[] n;
            try{
                while ((n = datas.take()).length != 0) {
                    f = formatQueue.take();
                    f.setData(n);
                    f.setEnd(result);
                    result = es.submit(f);
                    //System.out.println("buffer�ѽ���");

                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private class format implements Callable<List<List>>{

        private byte[] data;
        private Future<List<List>> end;

        public format(){
            try {
                formatQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public List<List> call() {
            int now = 0;
            int paNum = 0;
            List<List> s = new ArrayList<>();
            String[] h = new String(data).split(" ")[0].split("\n");
            try {
                dataQueue.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<List> result = new ArrayList<>();
            boolean one = false;
            for(String line:h) {

                /**
                if(++ls%1000000 == 0){
                    ThreadPoolExecutor tpe = ((ThreadPoolExecutor) es);
                    System.out.println("�Ѵ���"+ls+"��ʣ���ڴ棺"+(Runtime.getRuntime().freeMemory()/1024/1024)+"����������"+datas.size()+"����ǰ��߳�����"+ tpe.getActiveCount()+"���Ŷ��߳���:"+tpe.getQueue().size()+"��formatQueue��"+formatQueue.size()+"��updataQueue��"+updataTestQueue.size());
                }
                 */


                String[] a = line.split(",");
                //if((Long.parseLong(a[0])/1000)==(long)1587989822 && a[1].equals("getInfo1"))
                    //System.out.print("");
                if(now == 0){
                    now = (int) (Long.parseLong(a[0]) / 1000);
                    List l = new ArrayList();
                    l.add(now);
                    l.add(a[1]);
                    l.add(Integer.parseInt(a[2]));
                    result.add(l);
                }else if (Long.parseLong(a[0]) / 1000 == now && paNum==0) {
                    List l = new ArrayList();
                    l.add(now);
                    l.add(a[1]);
                    l.add(Integer.parseInt(a[2]));
                    result.add(l);
                } else if (Long.parseLong(a[0]) / 1000 == now) {
                    List l = new ArrayList();
                    l.add(now);
                    l.add(a[1]);
                    l.add(Integer.parseInt(a[2]));
                    s.add(l);
                } else if(paNum == 0) {
                    paNum++;
                    now = (int) (Long.parseLong(a[0]) / 1000);
                    List l = new ArrayList();
                    l.add(now);
                    l.add(a[1]);
                    l.add(Integer.parseInt(a[2]));
                    s.add(l);
                } else {
                    updataTest f = null;
                    try {
                        f = updataTestQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    f.setData(s);
                    es.submit(f);
                    one = true;
                    s = new ArrayList<>();
                    now = (int) (Long.parseLong(a[0]) / 1000);
                    List l = new ArrayList();
                    l.add(now);
                    l.add(a[1]);
                    l.add(Integer.parseInt(a[2]));
                    s.add(l);
                }
            }
            try {
                if(end.get().size() != 0){
                    if ((int)end.get().get(0).get(0) == (int)result.get(0).get(0))
                        result.addAll(end.get());
                    else {
                        updataTest f = updataTestQueue.take();
                        f.setData(end.get());
                        es.submit(f);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(!one && s.size()==0) {
                try {
                    formatQueue.put(this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return result;
            } else {
                try {
                    updataTest f = updataTestQueue.take();
                    f.setData(result);
                    es.submit(f);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                formatQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public void setEnd(Future<List<List>> end) {
            this.end = end;
        }
    }
}
