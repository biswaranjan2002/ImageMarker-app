package com.example.practiceangledraw;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class ViewBox extends Pane {


    @Override
    protected void layoutChildren() {

        super.layoutChildren();

        if (getChildren().isEmpty()) return;

        Region child ;

        if (getChildren().get(0) != null && getChildren().get(0) instanceof Region) {
            child = (Region) getChildren().get(0);

            setWidth(child.getPrefWidth());
            setHeight(child.getPrefHeight());
            setPrefHeight(child.getPrefHeight());
            setPrefWidth(child.getPrefWidth());
            setMaxHeight(child.getPrefHeight());
            setMaxWidth(child.getPrefWidth());
            setMinHeight(child.getPrefHeight());
            setMinWidth(child.getPrefWidth());

        }
        else{
            System.out.println("Child is not a region.");
            return;
        }


        Region parent;
        if(getParent() != null && getParent() instanceof Region){
            parent = (Region) getParent();

        }
        else{
            System.out.println("Parent is not a region.");
            return;
        }

        updateScale();
        parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateScale();
        });

        parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            updateScale();
        });

    }




    public void updateScale(){


        Region child ; // Assume only one child
        double childAspectRatio = -1;

        if (getChildren().get(0) != null && getChildren().get(0) instanceof Region) {
            child = (Region) getChildren().get(0);
            childAspectRatio = child.getPrefWidth()/child.getPrefHeight();

        }
        else{
            System.out.println("Child is not a region.");
            return;
        }


        Region parent;
        double parentAspectRatio = -1;
        if(getParent() != null && getParent() instanceof Region){
            parent = (Region) getParent();
            parentAspectRatio = parent.getWidth()/parent.getHeight();
        }
        else{
            System.out.println("Parent is not a region.");
            return;
        }


        double scalex, scaley;

        double finalW, finalH;

        if (parentAspectRatio == childAspectRatio) {
            finalW = parent.getWidth();
            finalH = parent.getHeight();
            // System.out.println("same");
        } else if (childAspectRatio < parentAspectRatio) {
            finalH = parent.getHeight();
            finalW = childAspectRatio * parent.getHeight();
        } else {
            finalW = parent.getWidth();
            finalH = parent.getWidth() / childAspectRatio;
        }


        scalex = finalW/child.getPrefWidth();
        scaley = finalH/child.getPrefHeight();


        setScaleX(scalex);
        setScaleY(scaley);
    }
}