public class Student {
    private String id;
    private String name;
    private int[] marks;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.marks = new int[3];
    }

    public Student(String id, String name, int mark1, int mark2, int mark3) {
        this.id = id;
        this.name = name;
        this.marks = new int[]{mark1, mark2, mark3};
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getMarks() {
        return marks;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setMarks(int mark1, int mark2, int mark3) {
        this.marks[0] = mark1;
        this.marks[1] = mark2;
        this.marks[2] = mark3;
    }

    public String getGrade() {
        double average = (marks[0] + marks[1] + marks[2]) / 3.0;
        if (average >= 80) {
            return "Distinction";
        }
        else if (average >= 70) {
            return "Merit";
        }
        else if (average >= 40) {
            return "Pass";
        }
        else if (average < 40) {
            return "Fail";
        }
        return "Invalid Mark";
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}
