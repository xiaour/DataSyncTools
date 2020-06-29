package com.luoboduner.wesync.ui.component;

import com.luoboduner.wesync.logic.bean.CombBoxItem;

import java.awt.*;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author zhangtao
 * @Class MyComboBox
 * @Description
 * @Date 2020/6/29 10:48
 * @Version 1.0.0
 */
public class MyComboBox<E> extends JComboBox {

    public MyComboBox(Vector values){
        super(values);
        rendererData(); //渲染数据
    }


    public void rendererData(){
        ListCellRenderer render = new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null){
                    CombBoxItem po = (CombBoxItem) value;
                    this.setText(po.getValue());
                }
                return this;
            }
        };
        this.setRenderer(render);
    }

    //修改Combox中的数据
    public void updateData(Vector values){
        setModel(new DefaultComboBoxModel(values));
        rendererData();
    }

    @Override
    public void setSelectedItem(Object anObject){ //选中text与传入的参数相同的项
        if (anObject != null){
            if (anObject instanceof CombBoxItem){
                super.setSelectedItem(anObject);
            }
            if(anObject instanceof String){
                for (int index = 0; index < getItemCount(); index++){
                    CombBoxItem po = (CombBoxItem) getItemAt(index);
                    if (po.getValue().equals(anObject.toString())){
                        super.setSelectedIndex(index);
                    }
                }
            }
        }else{
            super.setSelectedItem(anObject);
        }
    }

    public void setSelectedValue(Object anObject){ //选中value与传入的参数相同的项
        if(anObject != null){
            if(anObject instanceof CombBoxItem){
                super.setSelectedItem(anObject);
            }
            if(anObject instanceof String){
                for(int index = 0; index < getItemCount(); index++){
                    CombBoxItem po = (CombBoxItem) getItemAt(index);
                    if(po.getValue().equals(anObject.toString())){
                        super.setSelectedIndex(index);
                    }
                }
            }
        }else{
            super.setSelectedItem(anObject);
        }
    }



}
