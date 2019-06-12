/*    */ package com.jiuqian.bigdata.distribute.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileUtil
/*    */ {
/*    */   public static void sortAnimalTaskFiles(List<File> files, boolean isAsc)
/*    */   {
/* 20 */     int length = files.size();
/*    */     
/*    */ 
/* 23 */     Map<String, String> map = new HashMap();
/* 24 */     String[] arr = new String[files.size()];
/* 25 */     String regx = "\\-(\\d){1,}\\-\\.SCD";
/* 26 */     Pattern p = Pattern.compile(regx);
/* 27 */     for (int i = 0; i < length; i++) {
/* 28 */       Matcher m = p.matcher(((File)files.get(i)).getAbsolutePath());
/* 29 */       if (m.find()) {
/* 30 */         String file_index = StringUtils.substringBetween(m.group(), "-", "-");
/* 31 */         if (StringUtils.isNotBlank(file_index)) {
/* 32 */           int index = Integer.parseInt(file_index.replaceAll("\\s+?", ""));
/*    */           
/* 34 */           map.put("" + index, ((File)files.get(i)).getAbsolutePath());
/* 35 */           arr[i] = ("" + index);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 40 */     bubbleOrder(arr, isAsc);
/* 41 */     files.clear();
/* 42 */     if (isAsc) {
/* 43 */       for (int i = 0; i < length; i++) {
/* 44 */         files.add(new File((String)map.get(arr[i])));
/*    */       }
/* 46 */     } else if (!isAsc) {
/* 47 */       for (int i = length - 1; i >= 0; i--) {
/* 48 */         files.add(new File((String)map.get(arr[i])));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private static void bubbleOrder(String[] arr, boolean isAsc) {
/* 54 */     for (int i = 0; i <= arr.length - 2; i++) {
/* 55 */       for (int j = i + 1; j <= arr.length - 1; j++) {
/* 56 */         int a = Integer.parseInt(arr[i]);
/* 57 */         int b = Integer.parseInt(arr[j]);
/* 58 */         if (isAsc) {
/* 59 */           if (b < a) {
/* 60 */             arr[i] = String.valueOf(b);
/* 61 */             arr[j] = String.valueOf(a);
/*    */           }
/*    */         }
/* 64 */         else if (b > a) {
/* 65 */           arr[i] = String.valueOf(b);
/* 66 */           arr[j] = String.valueOf(a);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void sortAnimalTaskFiles(List<File> files)
/*    */   {
/* 77 */     sortAnimalTaskFiles(files, true);
/*    */   }
/*    */ }