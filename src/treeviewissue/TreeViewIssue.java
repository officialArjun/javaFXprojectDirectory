/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeviewissue;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author 91959
 */
public class TreeViewIssue extends Application {
    int count=0;
    HashMap<String,Boolean> hMap = new HashMap<String,Boolean>();
    //ArrayList<TreeItem<String>> lst=new ArrayList<>();
    ArrayList<String> lst=new ArrayList<>();
    
    void cpyToMap(HashMap<String,Boolean> readMap){
        this.hMap.clear();
        this.hMap.putAll(readMap);
    }
//    public void getNonLeafProject(TreeItem<String>node){
//        node.getChildren().forEach(x->{
//            if(!x.isLeaf()){
//                lst.add(x.getValue());
//            }
//        });
//    }
    public void AddtoProjStateList(TreeItem<String> rootNode){
            //Base Condition for exit from Recursion
            if(rootNode.isLeaf()){
                return;
            }
            rootNode.getChildren().forEach(x->{
                
                //add only those project who is expanded
                if(x.isLeaf()){
                    return;
                }
                else if(x.isLeaf()==false && x.isExpanded()){
                    lst.add(x.getValue());
                    
                }
                AddtoProjStateList(x);
                });
           System.out.println(lst);
    }
    
    @Override
    public void start(Stage primaryStage) {
        //Creating the treeview & its childrens
        TreeItem<String> rootNode=new TreeItem<>("Agnisys");
        TreeItem<String> node1=new TreeItem<>("Project1");
        TreeItem<String> node2=new TreeItem<>("Project2");
        TreeItem<String> node3=new TreeItem<>("Project3");
        TreeItem<String> node4=new TreeItem<>("Project4");
//        
//        for(int i=1;i<5;i++){
//            node4.getChildren().add(new TreeItem<String>("File "+i));
//
//        }
        for(int i=1;i<=3;i++){
        node1.getChildren().add(new TreeItem<String>("proj1File "+i));
        node2.getChildren().add(new TreeItem<String>("proj2File "+i));
        node3.getChildren().add(new TreeItem<String>("proj3File "+i));
        
        }
        node1.getChildren().forEach(x->x.getChildren().add(new TreeItem<String>("Child "+1) ));
        
        node2.getChildren().forEach(x->x.getChildren().add(new TreeItem<String>("Child "+1) ));
        
        node3.getChildren().forEach(x->x.getChildren().add(new TreeItem<String>("Child "+1) ));
        
        
        
        
        rootNode.getChildren().addAll(node1,node2,node3,node4);
        rootNode.setExpanded(true);
        TreeView treeView =new TreeView();
        treeView.setRoot(rootNode);
        
        
        //Determine size of the board
        int val=rootNode.getChildren().size();
        boolean[] board=new boolean[val];
        
     
        for(TreeItem<String> item: rootNode.getChildren()){
           // System.out.println(item.getChildren().size());
            hMap.put(item.getValue(), Boolean.FALSE);
        }
        //Adding Event handler
        for(TreeItem<String> item: rootNode.getChildren()){
       item.addEventHandler(TreeItem.branchExpandedEvent(),e->{
                if(item.isExpanded()){
                 hMap.put(item.getValue(),true);   
                }
                
                System.out.println("event: "+hMap);
       });
       item.addEventHandler(TreeItem.branchCollapsedEvent(),e->{
                if(!item.isExpanded()){
                 hMap.put(item.getValue(),false);   
                }
                System.out.println("event: "+hMap);
       });
        
        
        }
        //Display of Satage
        readTasksFile();
        StackPane root = new StackPane();
        root.getChildren().add(treeView);
        Scene scene = new Scene(root, 900, 750);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(this::onClose);
        cpyToMap(readTasksFile());
        expandTreeView(rootNode,hMap);
        primaryStage.show();
        AddtoProjStateList(rootNode);
        
    }
    //on close event
    private void onClose(WindowEvent event){
            FileOutputStream out = null;
        try{
            out=new FileOutputStream("tasks.xml");
            XMLEncoder encoder = new XMLEncoder(out);
            encoder.writeObject(hMap);
            encoder.close();
        }catch(FileNotFoundException e){
            if(out != null){
                try{
                    out.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
        }
            
        }
    
    }
    //Read TaskFile
    private HashMap<String,Boolean> readTasksFile(){
            FileInputStream in=null;
            HashMap<String,Boolean> tasksMap=new HashMap<>();
            try{
                in=new FileInputStream("tasks.xml");
                XMLDecoder decoder=new XMLDecoder(in);
                tasksMap=(HashMap<String,Boolean>) decoder.readObject();
                decoder.close();
            }catch(Exception e){
                if(in!=null)
                    try{
                        in.close();
                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                e.printStackTrace();
                
            }finally{
                return tasksMap;
            }
    }
    
    void expandTreeView(TreeItem<String> rootNode,HashMap<String,Boolean> h){
        for(TreeItem<String> item: rootNode.getChildren()){
            if(h.get(item.getValue())){
                item.setExpanded(true);
            }
        }
    }
    
    
    //main
    public static void main(String[] args) {
        launch(args);
    }
    
}
