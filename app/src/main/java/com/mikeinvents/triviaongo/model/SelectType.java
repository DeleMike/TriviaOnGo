package com.mikeinvents.triviaongo.model;

public class SelectType {
    private String selectTypeName;
    private String selectTypeId;

    public SelectType(String selectTypeName, String selectTypeId){
        this.selectTypeName = selectTypeName;
        this.selectTypeId = selectTypeId;
    }

    public String getSelectTypeName() {
        return selectTypeName;
    }

    public String getSelectTypeId() {
        return selectTypeId;
    }

    @Override
    public String toString() {
        return selectTypeName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SelectType){
            SelectType selectType = (SelectType) obj;
            if(selectType.getSelectTypeName().equals(selectTypeName) && selectType.getSelectTypeId().equals(selectTypeId)){
                return true;
            }
        }

        return false;
    }
}
