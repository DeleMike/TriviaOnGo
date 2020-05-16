package com.mikeinvents.triviaongo.model;

public class Category {
    private String categoryName;
    private int categoryId;

    public Category(String categoryName, int categoryId){
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    //to display object as a string in the spinner
    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Category){
            Category category = (Category) obj;
            if(category.getCategoryName().equals(categoryName) && category.getCategoryId() == categoryId){
                return  true;
            }
        }

        return false;
    }
}
