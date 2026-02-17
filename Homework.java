public class Homework {
    private final String id;
    private final String studentId; //кімге тапсырылғанын білу
    private String title;
    private String deadline; //мысал: "2025-11-01"
    private boolean completed;
    private Integer grade; // null — бағаланбаған, 0-100 — баға
    private String teacherComment;

    public Homework(String id, String studentId, String title, String deadline) {
        this.id = id;
        this.studentId = studentId;
        this.title = title;
        this.deadline = deadline;
        this.completed = false;
        this.grade = null;
        this.teacherComment = "";
    }

    public String getId() {
        return id;
    }
    public String getStudentId() {
        return studentId;
    }
    public String getTitle() {
        return title;
    }
    public String getDeadline() {
        return deadline;
    }
    public boolean isCompleted() {
        return completed;
    }
    public Integer getGrade() {
        return grade;
    }
    public String getTeacherComment() {
        return teacherComment;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public void markCompleted() {
        this.completed = true;
    }
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    @Override
    public String toString() {
        String g = (grade == null) ? "—" : grade.toString();
        return "      Үй тапсырмасы: \nID:'" + id + "\nТақырып: " + title + "'\nМерзімі (дейін):" + deadline +
                "\nОрындалды:" + completed + "\nБаға:" + grade + "\nОқытушы пікірі: " + teacherComment;
    }
}
