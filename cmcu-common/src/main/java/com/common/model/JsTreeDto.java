package com.common.model;

import lombok.Data;

@Data
public class JsTreeDto {

    private Object id;

    private String text;

    private Object parent;

    private State state;

    private String icon;

    private Object data;

    public JsTreeDto(){
        this.state=new State();
    }



    public JsTreeDto(Object id,String text,Object parent){
        this.state=new State();
        this.id=id;
        this.text=text;
        this.parent=parent;
    }




    @Data
    public class State{

        private boolean disabled;

        private boolean opened;

        private boolean selected;
    }
}
