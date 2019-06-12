/*    */ package com.jiuqian.bigdata.distribute.util;
/*    */ 
/*    */ import com.jiuqian.bigdata.distribute.common.JobEnvVaribale;
/*    */ import com.jiuqian.bigdata.distribute.common.JobStandConfs;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class PropertiesUtil
/*    */ {
/* 13 */   private static Properties p = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized String getPropValue(String key, String path)
/*    */   {
/* 21 */     if (p == null) {
/* 22 */       String npath = path + "WEB-INF/classes/config.properties";
/* 23 */       InputStream is = null;
/*    */       try {
/* 25 */         is = new FileInputStream(npath);
/*    */       } catch (FileNotFoundException e1) {
/* 27 */         e1.printStackTrace();
/*    */       }
/* 29 */       p = new Properties();
/*    */       try {
/* 31 */         p.load(is);
/*    */       } catch (IOException e) {
/* 33 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 36 */     return p.getProperty(JobStandConfs.work_base_dir);
/*    */   }
/*    */   
/*    */   public static synchronized String getPropValue(String key) {
/* 40 */     if (p == null) {
/* 41 */       String npath = JobEnvVaribale.getWorkingDirectory() + "WEB-INF/classes/config.properties";
/* 42 */       System.out.println("npath ---->" + npath);
/* 43 */       InputStream is = null;
/*    */       try {
/* 45 */         is = new FileInputStream(npath);
/*    */       } catch (FileNotFoundException e1) {
/* 47 */         e1.printStackTrace();
/*    */       }
/* 49 */       p = new Properties();
/*    */       try {
/* 51 */         p.load(is);
/*    */       } catch (IOException e) {
/* 53 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 56 */     return p.getProperty(JobStandConfs.work_base_dir);
/*    */   }
/*    */ }