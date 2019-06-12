/*     */ package com.jiuqian.bigdata.distribute.action;
/*     */ 
/*     */ import com.jiuqian.bigdata.distribute.common.JobEnvVaribale;
/*     */ import com.jiuqian.bigdata.distribute.common.JobSeedReader;
/*     */ import com.jiuqian.bigdata.distribute.common.JobStandConfs;
/*     */ import com.jiuqian.bigdata.distribute.util.FileUtil;
/*     */ import com.jiuqian.bigdata.distribute.util.PropertiesUtil;
/*     */ import com.jiuqian.bigdata.io.FileExt;
/*     */ import com.opensymphony.xwork2.ActionSupport;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.struts2.ServletActionContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JobTaskAction
/*     */   extends ActionSupport
/*     */ {
/*     */   private static final long serialVersionUID = -6867938946713633485L;
/*     */   private String file;
/*     */   private String regexAccpet;
/*     */   private int size;
/*     */   private String reLoadSeed;
/*     */   private int retCode;
/*     */   private String message;
/*     */   private List<Map<String, String>> list;
/*  42 */   private String work_base_dir = "";
/*     */   
/*  44 */   private static final Map<String, List<BufferedReader>> readers = new HashMap();
/*     */   
/*     */   public String listTask() {
/*  47 */     if (StringUtils.isBlank(this.file)) {
/*  48 */       this.retCode = -1;
/*  49 */       this.message = "file名字不能为空!";
/*  50 */       return "success";
/*     */     }
/*     */     
/*  53 */     if ((StringUtils.isBlank(this.reLoadSeed)) && ((this.size < 10) || (this.size >= 100))) {
/*  54 */       this.retCode = -1;
/*  55 */       this.message = "size没有传递或者传递的值非法,size范围(10,100]";
/*  56 */       return "success";
/*     */     }
/*     */     
/*  59 */     synchronized (readers) {
/*  60 */       if ((readers == null) || (readers.size() == 0)) {
/*  61 */         reInitAllJobTask();
/*     */       }
/*  63 */       List<BufferedReader> list_brs = (List)readers.get(this.file);
/*  64 */       if ((list_brs == null) || (list_brs.size() == 0)) {
/*  65 */         reInitJobTask(this.file);
/*  66 */         list_brs = (List)readers.get(this.file);
/*     */       }
/*     */       
/*  69 */       if ((StringUtils.isNotBlank(this.reLoadSeed)) && (this.reLoadSeed.contains("true"))) {
/*  70 */         reInitJobTask(this.file);
/*  71 */         list_brs = (List)readers.get(this.file);
/*  72 */         this.retCode = 2;
/*  73 */         this.message = ("Job file:" + this.file + " 重新加载文件完毕！");
/*  74 */         return "success";
/*     */       }
/*     */       
/*  77 */       if ((list_brs == null) || (list_brs.size() == 0)) {
/*  78 */         this.retCode = -2;
/*  79 */         this.message = ("Job " + this.file + "  no seeds!");
/*  80 */         return "success";
/*     */       }
/*     */       try {
/*  83 */         this.list = JobSeedReader.readSeeds(list_brs, this.size, this.file);
/*     */       }
/*     */       catch (IOException e) {
/*  86 */         e.printStackTrace();
/*  87 */         this.retCode = -1;
/*  88 */         this.message = "reader.readLine()存在异常,请检查!";
/*  89 */         return "success";
/*     */       }
/*     */       
/*  92 */       if ((this.list != null) && (this.list.size() > 0)) {
/*  93 */         this.message = "返回结果正常!";
/*     */       } else {
/*  95 */         this.message = "种子读取完成!";
/*     */       }
/*  97 */       this.retCode = 1;
/*  98 */       return "success";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void reInitAllJobTask()
/*     */   {
/* 113 */     JobEnvVaribale.setWorkingDirectory(ServletActionContext.getServletContext().getRealPath(File.separator));
/* 114 */     this.work_base_dir = PropertiesUtil.getPropValue(JobStandConfs.work_base_dir);
/* 115 */     System.out.println("+++" + this.work_base_dir);
/* 116 */     initJobTasks();
/*     */   }
/*     */   
/*     */   public void initJobTasks() {
/* 120 */     File directory = new File(this.work_base_dir);
/* 121 */     if ((directory.exists()) && (directory.isDirectory())) {
/* 122 */       File[] jobFileDir = directory.listFiles();
/* 123 */       if (jobFileDir != null) {
/* 124 */         for (File job : jobFileDir) {
/* 125 */           readJobSeeds(job);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void reInitJobTask(String file)
/*     */   {
/* 133 */     File directory = new File(this.work_base_dir + file);
/* 134 */     readJobSeeds(directory);
/*     */   }
/*     */   
/*     */   private void readJobSeeds(File job) {
/* 138 */     if ((job != null) && (job.isDirectory())) {
/* 139 */       String job_name = job.getName();
/* 140 */       System.out.println("Job Task :" + job_name + " start to read seeds ...");
/* 141 */       List<File> job_list_files = new ArrayList();
/* 142 */       if (StringUtils.isNotBlank(this.regexAccpet)) {
/* 143 */         FileExt.find2(job, this.regexAccpet, job_list_files);
/* 144 */         if (job_list_files.size() == 0) {
/* 145 */           System.err.println("Job Task :" + job_name + " no scd seeds ...");
/*     */         }
/* 147 */         FileUtil.sortAnimalTaskFiles(job_list_files, true);
/*     */       } else {
/* 149 */         FileExt.find2(job, "SCD", job_list_files);
/*     */       }
/*     */       
/* 152 */       List<BufferedReader> list_bf = new ArrayList();
/* 153 */       for (File f : job_list_files) {
/* 154 */         System.out.println("[job task : input seed ->" + job_name + f.getName() + "]");
/*     */         try
/*     */         {
/* 157 */           InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
/* 158 */           BufferedReader reader = new BufferedReader(isr);
/* 159 */           list_bf.add(reader);
/*     */         } catch (FileNotFoundException e) {
/* 161 */           e.printStackTrace();
/*     */         } catch (UnsupportedEncodingException e) {
/* 163 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */       readers.put(job_name, list_bf);
/*     */     }
/*     */   }
/*     */   
/* 176 */   public String getFile() { return this.file; }
/*     */   
/*     */   public void setFile(String file)
/*     */   {
/* 180 */     this.file = file;
/*     */   }
/*     */   
/*     */   public String getRegexAccpet() {
/* 184 */     return this.regexAccpet;
/*     */   }
/*     */   
/*     */   public void setRegexAccpet(String regexAccpet) {
/* 188 */     this.regexAccpet = regexAccpet;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 192 */     return this.size;
/*     */   }
/*     */   
/*     */   public void setSize(int size) {
/* 196 */     this.size = size;
/*     */   }
/*     */   
/*     */   public String getReLoadSeed()
/*     */   {
/* 201 */     return this.reLoadSeed;
/*     */   }
/*     */   
/*     */   public void setReLoadSeed(String reLoadSeed) {
/* 205 */     this.reLoadSeed = reLoadSeed;
/*     */   }
/*     */   
/*     */   public int getRetCode() {
/* 209 */     return this.retCode;
/*     */   }
/*     */   
/*     */   public void setRetCode(int retCode) {
/* 213 */     this.retCode = retCode;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 217 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 221 */     this.message = message;
/*     */   }
/*     */   
/*     */   public List<Map<String, String>> getList() {
/* 225 */     return this.list;
/*     */   }
/*     */   
/*     */   public void setList(List<Map<String, String>> list)
/*     */   {
/* 230 */     this.list = list;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 235 */     JobTaskAction action = new JobTaskAction();
/* 236 */     action.work_base_dir = "/home/cxfang/Data/autohome_mall/201708/output/0815/";
/* 237 */     action.initJobTasks();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Desktop\autoserver\WEB-INF\classes\!\com\jiuqian\bigdata\distribute\action\JobTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */