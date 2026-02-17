import java.util.List;
import java.util.Scanner;

public class HomeworkManager {
    private final Database db;
    private final Scanner scanner;

    public HomeworkManager(Database db, Scanner scanner) {
        this.db = db;
        this.scanner = scanner;
    }

    public void userMenu(User user) {
        while (true) {
            System.out.println("\n    Пайдаланушы мәзірі (" + user.getName() + ")   ");
            System.out.println("1. Менің тапсырмаларымды көру");
            System.out.println("2. Тапсырманы орындағанын белгілеу");
            System.out.println("3. Профильді қарау");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    showMyHomeworks(user);
                    break;
                case "2":
                    completeHomework(user);
                    break;
                case "3":
                    showProfile(user);
                    break;
                case "0":
                    System.out.println("Пайдаланушы сессиясы аяқталды.");
                    return;
                default:
                    System.out.println("Қате таңдау. Қайта таңдаңыз.");
                    break;
            }
        }
    }

    private void showMyHomeworks(User user) {
        List<Homework> list = db.getHomeworksForStudent(user.getId());
        if (list.isEmpty()) {
            System.out.println("Сізге тапсырма тағайындалған жоқ.");
            return;
        }
        System.out.println("\n   Сіздің тапсырмаларыңыз");
        for (Homework h : list) {
            System.out.println("ID: " + h.getId() + "\nТақырып: " + h.getTitle() +
                    "\nМерзімі (дейін): " + h.getDeadline() +
                    "\nОрындалды: " + h.isCompleted() +
                    "\nБаға: " + (h.getGrade() == null ? "—" : h.getGrade()));
            if (!h.getTeacherComment().isEmpty()) System.out.println("\nОқытушы пікірі: " + h.getTeacherComment());
        }
    }

    private void completeHomework(User user) {
        System.out.print("Орындалған тапсырма ID енгізіңіз: ");
        String id = scanner.nextLine().trim();
        java.util.Optional<Homework> homeworkOptional = db.findHomeworkById(id);
        if (homeworkOptional.isPresent()) {
            Homework h = homeworkOptional.get();
            // тек сол студентке немесе ALL-ға қатысты тапсырманы өзгертуге рұқсат
            if (h.getStudentId().equals(user.getId()) || h.getStudentId().equals("ALL")) {
                if (h.isCompleted()) {
                    System.out.println("Тапсырма бұрыннан орындалған.");
                } else {
                    h.markCompleted();
                    System.out.println("Тапсырма «орындалды» деп белгіленді. Егер қажет болса, мұғалім бағалайды.");
                }
            } else {
                System.out.println("Бұл тапсырма сізге арналмаған.");
            }
        } else {
            System.out.println("Тапсырма табылмады.");
        }
    }

    private void showProfile(User user) {
        System.out.println("\n    Профиль: " + user);
    }

    // опшнал: студент өзіне жеке тапсырма жаза алуы мүмкін
    public void addPersonalHomework(User user) {
        System.out.print("Жеке тапсырма атауы: ");
        String title = scanner.nextLine().trim();
        System.out.print("Мерзімі (YYYY-MM-DD) дейін: ");
        String deadline = scanner.nextLine().trim();

        // ID генерациясы
        String id = db.generateHomeworkId();

        Homework hw = new Homework(id, user.getId(), title, deadline);
        db.addHomework(hw);
        System.out.println("Жеке тапсырма қосылды: " + id);
    }
}
