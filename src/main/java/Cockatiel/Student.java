package Cockatiel;

class Student {
    Double id;
    String major;
    char gender;
    double scoreOriginal;
    double scoreRetake;

    Student(Double i, String m, char g){
        id = i;
        major = m;
        gender = g;
    }

    void setScoreOriginal(double score) {
        scoreOriginal = score;
    }

    void setScoreRetake(double score) {
        scoreRetake = score;
    }
}
