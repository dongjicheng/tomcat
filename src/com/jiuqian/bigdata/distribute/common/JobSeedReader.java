/*     */ package com.jiuqian.bigdata.distribute.common;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class JobSeedReader
/*     */ {
/*  15 */   private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  16 */   private static final Map<String, String> tempLines = new HashMap();
/*  17 */   private static int count = 0;
/*     */   
/*     */   public static boolean readSeeds(BufferedReader reader, int howManyToRead, String jobName, List<Map<String, String>> result) throws IOException {
/*  20 */     Map<String, String> map = new HashMap();
/*  21 */     boolean isBrEnd = false;
/*  22 */     int i = 0;
/*  23 */     int size = result.size();
/*  24 */     String priLine = (String)tempLines.get(jobName);
/*  25 */     if (StringUtils.isNotBlank(priLine)) {
/*  26 */       KV kv = getKV(priLine);
/*  27 */       map.put(kv.getKey(), kv.getValue());
/*     */     }
/*  29 */     String line = null;
/*  30 */     while ((line = reader.readLine()) != null) {
/*  31 */       if (StringUtils.isNotBlank(line)) {
/*  32 */         String tempLine = StringUtils.trimToEmpty(line);
/*  33 */         if (StringUtils.startsWithIgnoreCase(tempLine, "<DOCID>")) {
/*  34 */           if (i == 0) {
/*  35 */             KV kv = getKV(tempLine);
/*  36 */             map.put(kv.getKey(), kv.getValue());
/*     */           } else {
/*  38 */             result.add(map);
/*  39 */             size++;
/*  40 */             if (size >= howManyToRead) {
/*  41 */               tempLines.put(jobName, tempLine);
/*  42 */               return isBrEnd;
/*     */             }
/*  44 */             map = new HashMap();
/*  45 */             KV kv = getKV(tempLine);
/*  46 */             map.put(kv.getKey(), kv.getValue());
/*     */           }
/*     */         }
/*     */         else {
/*  50 */           KV kv = getKV(tempLine);
/*  51 */           map.put(kv.getKey(), kv.getValue());
/*     */         }
/*     */       }
/*  54 */       i++;
/*     */     }
/*  56 */     isBrEnd = true;
/*     */     
/*  58 */     if (map.size() > 0) {
/*  59 */       tempLines.remove(jobName);
/*  60 */       result.add(map);
/*  61 */       return isBrEnd;
/*     */     }
/*  63 */     return isBrEnd;
/*     */   }
/*     */   
/*     */   public static List<Map<String, String>> readSeeds(List<BufferedReader> readers, int howManyToRead, String jobName) throws IOException {
/*  67 */     List<Map<String, String>> result = new ArrayList();
/*  68 */     if ((readers != null) && (readers.size() > 0)) {
/*  69 */       BufferedReader reader = (BufferedReader)readers.get(0);
/*  70 */       boolean isEnd = readSeeds(reader, howManyToRead, jobName, result);
/*  71 */       if (isEnd) {
/*  72 */         readers.remove(0);
/*  73 */         count += 1;
/*  74 */         System.out.println("already done-->" + count);
/*  75 */         isEnd = false;
/*     */       }
/*     */       
/*  78 */       if (result.size() < howManyToRead) {
/*  79 */         if ((readers != null) && (readers.size() > 0)) {
/*  80 */           reader = (BufferedReader)readers.get(0);
/*  81 */           isEnd = readSeeds(reader, howManyToRead, jobName, result);
/*     */         } else {
/*  83 */           System.out.println("[ ALL readers done --" + readers.size() + "]");
/*     */         }
/*     */       }
/*     */       
/*  87 */       if (isEnd) {
/*  88 */         readers.remove(0);
/*  89 */         count += 1;
/*  90 */         System.out.println("已经完成-->" + count);
/*     */       }
/*     */     }
/*     */     
/*  94 */     System.out.println("[" + sdf.format(new java.util.Date()) + " batch size ++++++> " + result.size() + "]");
/*  95 */     return result;
/*     */   }
/*     */   
/*     */   static class KV {
/*     */     private String key;
/*     */     private String value;
/*     */     
/*     */     public String getKey() {
/* 103 */       return this.key;
/*     */     }
/*     */     
/*     */     public void setKey(String key) {
/* 107 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 111 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(String value) {
/* 115 */       this.value = value;
/*     */     }
/*     */     
/*     */     public KV(String key, String value) {
/* 119 */       this.key = key;
/* 120 */       this.value = value;
/*     */     }
/*     */   }
/*     */   
/*     */   private static KV getKV(String str) {
/* 125 */     if (StringUtils.isBlank(str)) {
/* 126 */       return null;
/*     */     }
/* 128 */     return new KV("<" + StringUtils.substringBetween(str, "<", ">") + ">", StringUtils.substringAfter(str, ">"));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Desktop\autoserver\WEB-INF\classes\!\com\jiuqian\bigdata\distribute\common\JobSeedReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */