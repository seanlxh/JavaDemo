package javaDemo.service;

import javaDemo.util.processCollection;

import java.util.Collection;
import java.util.Iterator;

import static javaDemo.util.processCollection.execute;
import static javaDemo.util.processCollection.judgeClass;

public class ExecuteService {


    public void excute(String className, String methodName) throws  Exception{


        Object tmp =  execute("com.up.student.dao.StudentDAO","list",
                new Class[]{int.class},new Object[]{1});
        Collection result = processCollection.setItems(tmp);

        if(result != null){
            Iterator it = result.iterator();

            while(it.hasNext()){
                String[] tmp1 = (String[])it.next();
                for (int i = 0 ; i < tmp1.length ; i ++){
                    System.out.println(tmp1[i]);
                }
            }
        }

        }

    }
