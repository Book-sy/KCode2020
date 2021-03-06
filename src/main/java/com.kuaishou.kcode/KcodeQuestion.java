package com.kuaishou.kcode;

import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import static jdk.nashorn.internal.objects.NativeString.indexOf;
import static jdk.nashorn.internal.objects.NativeString.substring;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public final class KcodeQuestion {

    private int BUFFER_SIZE = 1024 * 32;

    private Map<Integer, Map<String, String>> map = new HashMap<>();

    //private Queue<Map<Integer, Map<String, List>>> q = new ConcurrentLinkedQueue<>();
    private ExecutorService es = Executors.newFixedThreadPool(32);

    //private BlockingQueue<byte[]> datas = new LinkedBlockingQueue<>();

    private BlockingQueue<format> formatQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<updataTest> updataTestQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<byte[]> dataQueue = new LinkedBlockingQueue<>();

    private static int ls = 0;

    public KcodeQuestion() {
        for (int i = 0; i < 20; i++)
            new format();
        for (int i = 0; i < 15; i++)
            new updataTest();

        try {
            for (int i = 0; i < 20; i++)
                dataQueue.put(new byte[BUFFER_SIZE + 100]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * prepare() 方法用来接受输入数据集，数据集格式参考README.md
     *
     * @param inputStream
     */

    public final void prepare(InputStream inputStream) {

        //try {


        /**
         Thread addData = new Thread(new Runnable() {
        @Override public void run() {
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
        @Override public void run() {
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

        byte[] one = null;
        try {
            one = dataQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //addData.start();
        int next;

        //最新版改进----------start----------------
        //Thread.currentThread().setPriority(9);
        try {
            FileChannel channel = ((FileInputStream) inputStream).getChannel();
            next = BUFFER_SIZE;

            ByteBuffer bf = ByteBuffer.allocate(next);
            ByteBuffer l = ByteBuffer.allocate(1);

            Future<List<List>> result;
            result = es.submit(new Callable<List<List>>() {
                @Override
                public List<List> call() {
                    return new ArrayList<>();
                }
            });
            format f;
            try {
                while (channel.read(bf) != -1) {
                    next = BUFFER_SIZE;
                    bf.flip();
                    bf.get(one, 0, BUFFER_SIZE);
                    while (channel.read(l) != -1) {
                        if (l.get(0) == '\n') {
                            l.clear();
                            break;
                        } else
                            one[next++] = l.get(0);
                        l.clear();
                    }

                    bf.clear();
                    one[next++] = ' ';

                    f = formatQueue.take();
                    f.setData(one);
                    f.setEnd(result);
                    result = es.submit(f);
                    //System.out.println("buffer已结束");

                    one = dataQueue.take();
                }
            } catch (BufferUnderflowException e) {
                f = formatQueue.take();
                f.setData(bf.array());
                f.setEnd(result);
                es.submit(f);
            }
            //System.out.println("已存入数据");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //最新版改进------------end---------------------
        /**
         while (inputStream.read(one, 0, 20000) > 0) {


         next = 20000;

         while (true) {
         i = (byte) inputStream.read();
         if(i == '\n' || i ==-1)
         break;
         one[next++] = i;
         }
         //System.out.println("已存入数据");
         one[next++] = ' ';
         datas.offer(one);
         one = dataQueue.take();

         }
         //System.out.println("总共读取数据包:"+ls2+"个");
         datas.offer(new byte[0]);
         //System.out.println("加载数据以读取完成");

         //addData.join();
         */
        //旧版改进-----------------------end---------------------------
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
         //System.out.println("已着手处理"+i+"秒级数据");
         }
         es2.shutdown();
         es2.awaitTermination(60,TimeUnit.SECONDS);
         System.out.println("处理秒级数据时间："+(new Date().getTime()-a));
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         */
    }

    /**
     * getResult() 方法是由kcode评测系统调用，是评测程序正确性的一部分，请按照题目要求返回正确数据
     * 输入格式和输出格式参考 README.md
     *
     * @param timestamp  秒级时间戳
     * @param methodName 方法名称
     */
    public final String getResult(Long timestamp, String methodName) {
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

    private final class updataTest implements Runnable {

        private List<List> data;

        public updataTest() {
            try {
                updataTestQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public final void run() {

            try {
                Map map = new HashMap();
                long time = 0;
                //改用迭代器 替换 增强for
                //for (List line : data) {
                Iterator iterator = data.iterator();
                List line;
                while (iterator.hasNext()){
                    line = (List)iterator.next();
                    String key = (String) line.get(1);
                    time = (Integer) line.get(0);

                    List z = (List) map.get(key);
                    if (z == null) {
                        z = new ArrayList(5);
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
                //改用迭代器 替换 增强for
                //for (Object q : map.keySet()) {
                iterator = map.keySet().iterator();
                Object q;
                while (iterator.hasNext()){
                    q = iterator.next();
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

                    map.put(q, "" + QPS + "," + P99 + "," + P50 + "," + AVG + "," + MAX);
                }
                KcodeQuestion.this.map.put((int) time, map);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                updataTestQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public final void setData(List<List> data) {
            this.data = data;
        }
    }

    private final class format implements Callable<List<List>> {

        private byte[] data;
        private Future<List<List>> end;

        public format() {
            try {
                formatQueue.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public final List<List> call() {
            int now = 0;
            int paNum = 0;
            List<List> s = new ArrayList<>();

            /**
             int size = data.length;
             StringBuffer sb = new StringBuffer();
             for(int i=0;i<size;i++){
             if(data[i] == '\n'){
             h.add(sb.toString());
             sb.delete(0,sb.length());
             } else if(data[i] == ' '){
             h.add(sb.toString());
             break;
             } else {
             sb.append(data[i]);
             }
             }
             */
            //旧分割方式 and 自制函数
            //String[] h = new String(data).split(" ")[0].split("\n");
            List<String> h = split(data);
            Future<List<List>> Newend = end;
            try {
                formatQueue.put(this);
                dataQueue.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<List> result = new ArrayList<>();
            boolean one = false;
            //改用迭代器 替换 增强for
            //for (String line : h) {
            Iterator iterator = h.iterator();
            String line;
            while (iterator.hasNext()){

                line = (String)iterator.next();
                /*if (++ls % 1000000 == 0) {
                    System.out.println("已处理" + ls + "，剩余内存：" + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "，data队列数量" + dataQueue.size() + "，es排队线程数:" + ((ThreadPoolExecutor) es).getQueue().size() + "，formatQueue：" + formatQueue.size() + "，updataQueue：" + updataTestQueue.size());
                }*/


                String[] a = line.split(",");
                //if((Long.parseLong(a[0])/1000)==(long)1587989822 && a[1].equals("getInfo1"))
                //System.out.print("");
                boolean t = (Long.parseLong(a[0]) / 1000 == now);
                if (now == 0) {
                    now = getNow(result, a);
                } else if (t && paNum == 0) {
                    List l = new ArrayList(5);
                    l.add(now);
                    l.add(a[1]);
                    l.add(Integer.parseInt(a[2]));
                    result.add(l);
                } else if (paNum == 0) {
                    paNum++;
                    now = getNow(s, a);
                } else if (t) {
                    List l = new ArrayList(5);
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
                    now = getNow(s, a);
                }
            }
            try {
                List<List> re = Newend.get();
                if (re.size() != 0) {
                    if ((int) re.get(0).get(0) == (int) result.get(0).get(0))
                        result.addAll(re);
                    else {
                        updataTest f = updataTestQueue.take();
                        f.setData(end.get());
                        es.submit(f);
                    }
                }
                if (!one && s.size() == 0) {
                    return result;
                } else {
                    updataTest f = updataTestQueue.take();
                    f.setData(result);
                    es.submit(f);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return s;
        }

        private final int getNow(List<List> s, String[] a) {
            int now;
            now = (int) (Long.parseLong(a[0]) / 1000);
            List l = new ArrayList(5);
            l.add(now);
            l.add(a[1]);
            l.add(Integer.parseInt(a[2]));
            s.add(l);
            return now;
        }

        public final void setData(byte[] data) {
            this.data = data;
        }

        public final void setEnd(Future<List<List>> end) {
            this.end = end;
        }
    }

    public static List<String> split(byte[] our) {

        ArrayList<String> list = new ArrayList<>(500);
        int off=-1;
        int size = our.length;
        for(int i=0;i<size;i++){
            byte z = our[i];
            if(z == '\n'){
                list.add(new String(our,off+1,i-off-1));
                off = i;
            } else if(z == ' '){
                list.add(new String(our,off+1,i-off-1));
                break;
            }
        }
        return  list;
    }
}
