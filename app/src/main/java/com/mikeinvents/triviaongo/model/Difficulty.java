package com.mikeinvents.triviaongo.model;

public class Difficulty {
    private String difficultyName;
    private String difficultyId;

    public Difficulty(String difficultyName, String difficultyId){
        this.difficultyName = difficultyName;
        this.difficultyId = difficultyId;
    }

    public String getDifficultyName() {
        return difficultyName;
    }


    public String getDifficultyId() {
        return difficultyId;
    }


    @Override
    public String toString() {
        return difficultyName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Difficulty){
            Difficulty difficulty = (Difficulty) obj;
            if(difficulty.getDifficultyName().equals(difficultyName) && difficulty.getDifficultyId().equals(difficultyId)){
                return true;
            }
        }
        return false;
    }
}
