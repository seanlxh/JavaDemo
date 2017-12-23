/**
 * author lixiaohe
 * date 2017/11/20
 */

//这里是函数的数据结构
package javaDemo.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.jvm.hotspot.debugger.cdbg.FunctionType;

public class Function {

    private final StringProperty className;

    private final StringProperty methodName;

    private final StringProperty levelName;

    private final StringProperty enableName;

    public Function(){
        this(null, null , null);
    }

    public Function(String className, String methodName , String levelName){
        this.className = new SimpleStringProperty(className);
        this.methodName = new SimpleStringProperty(methodName);
        this.levelName = new SimpleStringProperty(levelName);
        this.enableName = new SimpleStringProperty("");
    }




    public StringProperty classNameProperty() {
        return className;
    }

    public void setClassName(String className) {
        this.className.set(className);
    }

    public StringProperty methodNameProperty() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName.set(methodName);
    }

    public StringProperty levelNameProperty() {
        return levelName;
    }

    public void setLevelName(String level){
        this.levelName.set(level);
    }

    public StringProperty enableNameProperty() {
        return enableName;
    }

    public void setEnableName(String enableName){
        this.enableName.set(enableName);
    }
}
