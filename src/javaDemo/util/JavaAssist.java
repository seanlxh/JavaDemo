package javaDemo.util;

import javassist.*;
import java.io.IOException;

import java.util.Scanner;


//
//public class test {
//
//    public static void main(String args[]){
//        ClassPool pool = ClassPool.getDefault();
//        try {
//            CtClass pt = pool.get("TestBean");
//
//            // CtMethod m = CtNewMethod.make("public void xmove(int dx) {System.out.println(\"sdss\");}", pt);
//            CtMethod m1 = pt.getDeclaredMethod("aaa");
//            m1.insertBefore("{ System.out.println($1); System.out.println($2); System.out.println($3);}");
//            // pt.addMethod(m);
//            //pt.writeFile();
//            Class c = pt.toClass();
//            TestBean bean = (TestBean) c.newInstance();
//            bean.aaa(1,2,3);
//
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        } catch (CannotCompileException e) {
//            e.printStackTrace();
//        }   catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//
//}