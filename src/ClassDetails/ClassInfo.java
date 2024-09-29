package ClassDetails;

public class ClassInfo {
    private String className;
    private String classDesc;
    private String classDifficulty;
    private double classPercentDone;

    public ClassInfo(String className, String classDesc, String classDifficulty, Double percentDone) {
        this.className = className;
        this.classDesc = classDesc;
        this.classDifficulty = classDifficulty;
        this.classPercentDone = percentDone;
    }

    // Getters and Setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getClassDifficulty() {
        return classDifficulty;
    }

    public void setClassDifficulty(String classDifficulty) {
        this.classDifficulty = classDifficulty;
    }

    public Double getPercentDone() {
        return classPercentDone;
    }

    public void setPercentDone(Double percentDone) {
        this.classPercentDone = percentDone;
    }
}
