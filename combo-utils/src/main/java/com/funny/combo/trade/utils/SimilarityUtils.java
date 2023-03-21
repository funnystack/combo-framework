package com.funny.combo.trade.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
相似度度量（Similarity），即计算个体间的相似程度，相似度度量的值越小，说明个体间相似度越小，相似度的值越大说明个体差异越大。 对于多个不同的文本或者短文本对话消息要来计算他们之间的相似度如何，一个好的做法就是将这些文本中词语，映射到向量空间，形成文本中文字和向量数据的映射关系，通过计算几个或者多个不同的向量的差异的大小，来计算文本的相似度。下面介绍一个详细成熟的向量空间余弦相似度方法计算相似度 向量空间余弦相似度(Cosine Similarity) 余弦相似度用向量空间中两个向量夹角的余弦值作为衡量两个个体间差异的大小。余弦值越接近1，就表明夹角越接近0度，也就是两个向量越相似，这就叫"余弦相似性"。 ——————————
http://www.ruanyifeng.com/blog/2013/03/cosine_similarity.html
 * @author fangli
 */
public class SimilarityUtils {

    public static double getSimilarity(String doc1, String doc2) {
        if (doc1 != null && doc1.trim().length() > 0 && doc2 != null&& doc2.trim().length() > 0) {
            Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
            // 将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                // 标点和数字不处理
                if (!isHanZi(d1)) {
                    continue;
                }
                // 保存字符对应的GB2312编码
                int charIndex = getGB2312Id(d1);
                if (charIndex != -1) {
                    int[] fq = AlgorithmMap.get(charIndex);
                    if (fq != null && fq.length == 2) {
                        //已有该字符，加1
                        fq[0]++;
                    } else {
                        fq = new int[2];
                        fq[0] = 1;
                        fq[1] = 0;
                        //新增字符入map
                        AlgorithmMap.put(charIndex, fq);
                    }
                }
            }

            for (int i = 0; i < doc2.length(); i++) {
                char d2 = doc2.charAt(i);
                if(isHanZi(d2)){
                    int charIndex = getGB2312Id(d2);
                    if(charIndex != -1){
                        int[] fq = AlgorithmMap.get(charIndex);
                        if(fq != null && fq.length == 2){
                            fq[1]++;
                        }else {
                            fq = new int[2];
                            fq[0] = 0;
                            fq[1] = 1;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }

            Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
            double sqdoc1 = 0;
            double sqdoc2 = 0;
            double denominator = 0;
            while(iterator.hasNext()){
                int[] c = AlgorithmMap.get(iterator.next());
                denominator += c[0]*c[1];
                sqdoc1 += c[0]*c[0];
                sqdoc2 += c[1]*c[1];
            }
            //余弦计算
            return denominator / Math.sqrt(sqdoc1*sqdoc2);
        } else {
            throw new NullPointerException(" the Document is null or have not cahrs!!");
        }
    }

    public static boolean isHanZi(char ch) {
        // 判断是否汉字
        return (ch >= 0x4E00 && ch <= 0x9FA5);
      /*if (ch >= 0x4E00 && ch <= 0x9FA5) {//汉字
         return true;
      }else{
         String str = "" + ch;
         boolean isNum = str.matches("[0-9]+");
         return isNum;
      }*/
      /*if(Character.isLetterOrDigit(ch)){
         String str = "" + ch;
         if (str.matches("[0-9a-zA-Z\\u4e00-\\u9fa5]+")){//非乱码
                return true;
            }else return false;
      }else return false;*/
    }

    /**
     * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
     *
     * @param ch 输入的GB2312中文字符或者ASCII字符(128个)
     * @return ch在GB2312中的位置，-1表示该字符不认识
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
                // 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
                return -1;
            }
            // 编码从A1开始，因此减去0xA1=161
            int b0 = (int) (buffer[0] & 0x0FF) - 161;
            int b1 = (int) (buffer[1] & 0x0FF) - 161;
            // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        String str1="斯柯达 柯迪亚克 2018款 TSI330 5座两驱舒适版";
        String str2="斯柯达 柯迪亚克 2017款 TSI330 5座两驱舒适版";
        long start=System.currentTimeMillis();
        double Similarity= SimilarityUtils.getSimilarity(str1, str2);
        System.out.println("用时:"+(System.currentTimeMillis()-start));
        System.out.println(Similarity);
    }
}
