package com.kuaishou.kcode;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeQuestion {

    private Map<Integer, Map<String, List>> map = new ConcurrentHashMap<>();

    private Object lock = new Object();

    //private Queue<Map<Integer, Map<String, List>>> q = new ConcurrentLinkedQueue<>();
    private ExecutorService es = Executors.newFixedThreadPool(16);

    private BlockingQueue<byte[]> datas = new LinkedBlockingQueue<>();

    private BlockingQueue<format> formatQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<updataTest> updataTestQueue = new LinkedBlockingQueue<>();


    public KcodeQuestion() {
        new format();
        new format();
        new format();
        new updataTest();
        new updataTest();
        new updataTest();
    }

    /**
     * prepare() 方法用来接受输入数据集，数据集格式参考README.md
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
                        Thread.sleep(2000);
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
            System.out.println("当前排队线程数："+ activeCount+"，总执行线程数:"+tpe.getCompletedTaskCount());
            System.out.println("剩余内存："+(Runtime.getRuntime().freeMemory()/1024/1024)+"M");

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
            byte one[] = new byte[1024 * 2001];
            //addData.start();
            while (inputStream.read(one, 0, 1024 * 2000) > 0) {

                int next = 1024*2000;

                byte i;
                while (true) {
                    i = (byte) inputStream.read();
                    if(i == '\n' || i ==-1)
                        break;
                    one[next++] = i;
                }
                //System.out.println("已存入数据");
                while(datas.size()>=120){
                    Thread.sleep(100);
                }
                datas.offer(one);
                one = new byte[1024 * 2001];

            }
            //System.out.println("总共读取数据包:"+ls2+"个");
            datas.offer(new byte[0]);
            //System.out.println("加载数据以读取完成");
            buffer.join();
            //addData.join();

            while(((ThreadPoolExecutor) es).getActiveCount() != 0){
                Thread.sleep(100);
            }
            //System.out.println(a1+" "+a2+" "+a3);

            /**
            long a = new Date().getTime();
            ExecutorService es2 = Executors.newFixedThreadPool(16);
            for(Integer i:map.keySet()) {
                es2.submit(new getResultTest((long) i));
                //System.out.println("已着手处理"+i+"秒级数据");
            }
            es2.shutdown();
            es2.awaitTermination(60,TimeUnit.SECONDS);
            System.out.println("处理秒级数据时间："+(new Date().getTime()-a));
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
     * getResult() 方法是由kcode评测系统调用，是评测程序正确性的一部分，请按照题目要求返回正确数据
     * 输入格式和输出格式参考 README.md
     *
     * @param timestamp  秒级时间戳
     * @param methodName 方法名称
     */
    public String getResult(Long timestamp, String methodName){
        /**
         int num = 0;
         for(List i:map.get(1587987951).values()){
         num+=i.size();
         }
         System.out.println(num);
         */

        try {
            return (String) map.get(timestamp.intValue()).get(methodName).get(0);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
        return null;
    }

    private class getResultTest implements Runnable {

        private Long timestamp;
        private Map<String, List> map;

        public getResultTest(Long timestamp,Map<String, List> map) {
            this.timestamp = timestamp;
            this.map = map;
        }

        @Override
        public void run() {
        }
    }

    private class updataTest implements Runnable{

        private List<List> data;

        public updataTest(){
            try {
                updataTestQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        public updataTest(List<List> data) {
            this.data = data;
        }

        @Override
        public void run() {

            try {
                Map<String, List> map = new HashMap();
                long time = 0;
                for (List line : data) {

                    String key = (String)line.get(1);
                    time = (Integer)line.get(0);

                        List z = map.get(key);
                        if (z == null) {
                            z = new ArrayList();
                            z.add((int)line.get(2));
                            map.put(key, z);
                        } else {
                            z.add((int)line.get(2));
                        }

                }
                /**
                q.offer(map);
                synchronized (KcodeQuestion.this) {
                    es.submit(new getResultTest(time, map));
                }
                 */

                int QPS, P99, P50, AVG, MAX;
                for (String q : map.keySet()) {
                    List cz = map.get(q);
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

                    cz.clear();
                    cz.add(""+QPS + "," + P99 + "," + P50 + "," + AVG + "," + MAX);
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
            Future<List<List>> result = null;
            synchronized (KcodeQuestion.this) {
                result = es.submit(new Callable<List<List>>() {
                    @Override
                    public List<List> call(){
                        return new ArrayList<>();
                    }
                });
            }
            ThreadPoolExecutor tpe = ((ThreadPoolExecutor) es);
            format f;
            while (true) {
                try {
                    byte[] n = datas.take();
                    if(n.length == 0)
                        break;
                    else {
                        f = formatQueue.take();
                        f.setData(n);
                        f.setEnd(result);
                        synchronized (KcodeQuestion.this) {
                            result = es.submit(f);
                        }
                    }
                    while(tpe.getQueue().size()>=50)
                        Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println("buffer已结束");
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
        };
        public format(byte[] data,Future<List<List>> end) {
            this.data = data;
            this.end = end;
        }

        @Override
        public List<List> call() {
            int now = 0;
            int paNum = 0;
            List<List> s = new ArrayList<>();
            String[] h = new String(data).replaceAll("\u0000","").split("\n");
            List<List> result = new ArrayList<>();
            boolean one = false;
            for(String line:h) {


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
