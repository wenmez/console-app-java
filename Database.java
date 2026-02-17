import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database {
    private final List<User> users;
    private final List<Homework> homeworks;
    private int homeworkId = 0;
    private int userId = 0;


    public Database() {
        this.users = new ArrayList<>();
        this.homeworks = new ArrayList<>();
    }
    //үй тапсырмасының жалпыға ортақ айди генерациясы
    public String generateHomeworkId() {
        homeworkId++;
        return "HW-" + homeworkId;
    }
    //студенттің жазбасына айди генерациясы
    public String generateUserId() {
        userId++;
        return "U-" + userId;
    }
    //User-ге байланысты
    public void addUser(User user) {
        users.add(user);
    }

    public Optional<User> findByLogin(String login) {
        for (User u : users) {
            if (u.getLogin().equals(login)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    //Homework-қа байланысты
    public void addHomework(Homework hw) {
        homeworks.add(hw);
    }

    public Optional<Homework> findHomeworkById(String id) {
        for (Homework h : homeworks) {
            if (h.getId().equals(id)) {
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }

    public List<Homework> getAllHomeworks() {
        return new ArrayList<>(homeworks);
    }

    public List<Homework> getHomeworksForStudent(String studentId) {
        //ерекшелігі: бір студентке жеке тапсырма беру немесе ALL-мен барлығына тапсырма беру
        List<Homework> result = new ArrayList<>();
        for (Homework h : homeworks) {
            if (h.getStudentId().equals(studentId) || h.getStudentId().equals("ALL")) {
                result.add(h);
            }
        }
        return result;
    }

    public void removeHomework(String id) {
        for (int i = 0; i < homeworks.size(); i++) {
            if (homeworks.get(i).getId().equals(id)) {
                homeworks.remove(i);
                break;
            }
        }
    }

    // кішігірім статистика мысалы: студенттің орта бағасы
    public Double averageGradeForStudent(String studentId) {
        List<Integer> grades = new ArrayList<>();
        for (Homework h : getHomeworksForStudent(studentId)) {
            if (h.getGrade() != null) grades.add(h.getGrade());
        }
        if (grades.isEmpty()) return null;
        double sum = 0;
        for (Integer g : grades) sum += g;
        return sum / grades.size();
    }
}
