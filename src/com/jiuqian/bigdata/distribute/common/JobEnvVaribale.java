/*    */ package com.jiuqian.bigdata.distribute.common;
/*    */ 
/*    */ import com.jiuqian.bigdata.OperationSystem;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.apache.struts2.ServletActionContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JobEnvVaribale
/*    */ {
/* 14 */   static String workingDirectory = "./";
/*    */   
/* 16 */   static String logConfigFile = "conf/log4j.properties";
/* 17 */   static OperationSystem os = null;
/* 18 */   static long startTime = System.currentTimeMillis();
/*    */   
/*    */   public static void initialGlobalEnvVariable() {
/* 21 */     System.getenv();
/* 22 */     if (System.getProperty("dibo.log.dir") != null) {
/* 23 */       workingDirectory = System.getProperty("dibo.home.dir");
/*    */     }
/*    */     
/* 26 */     if (ServletActionContext.getServletContext().getRealPath(File.separator) != null) {
/* 27 */       workingDirectory = ServletActionContext.getServletContext().getRealPath(File.separator);
/*    */     }
/*    */   }
/*    */   
/*    */   public static String getWorkingDirectory()
/*    */   {
/* 33 */     System.out.println("workingDirectory -->" + workingDirectory);
/* 34 */     if (!workingDirectory.endsWith("/")) {
/* 35 */       workingDirectory += "/";
/*    */     }
/*    */     
/* 38 */     return workingDirectory;
/*    */   }
/*    */   
/*    */   public static void setWorkingDirectory(String workingDirectory) {
/* 42 */     workingDirectory = workingDirectory;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 46 */     System.out.println(getWorkingDirectory());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Desktop\autoserver\WEB-INF\classes\!\com\jiuqian\bigdata\distribute\common\JobEnvVaribale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */