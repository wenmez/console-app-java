import java.util.List;
import java.util.Scanner;

public class Admin {
    private final Database db;
    private final Scanner scanner;


    //әкімшінің логині/құпия сөзі
    public static final String ADMIN_LOGIN = "admin";
    public static final String ADMIN_PASSWORD = "1234";

    public Admin(Database db, Scanner scanner) {
        this.db = db;
        this.scanner = scanner;
    }

    public void adminMenu() {
        while (true) {
            System.out.println("\n     Әкімші мәзірі    ");
            System.out.println("1. Барлық студенттерді көру");
            System.out.println("2. Барлық тапсырмаларды көру");
            System.out.println("3. Тапсырма қосу (барлығына немесе жеке)");
            System.out.println("4. Тапсырманы жою");
            System.out.println("5. Тапсырмаға баға қою / пікір қосу");
            System.out.println("6. Студент статистикасы");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    showAllUsers(); //1. Барлық студенттерді көру
                    break;
                case "2":
                    ; //2. Барлық тапсырмаларды көру
                    break;
                case "3":
                    createHomework(); //3. Тапсырма қосу (барлығына немесе жеке)
                    break;
                case "4":
                    deleteHomework(); //4. Тапсырманы жою
                    break;
                case "5":
                    gradeHomework(); //5. Тапсырмаға баға қою / пікір қосу
                    break;
                case "6":
                    showStudentStats(); //6. Студент статистикасы
                    break;
                case "0":
                    System.out.println("Әкімші сессиясы аяқталды.");
                    return;
                default:
                    System.out.println("Қате таңдау. Қайта таңдаңыз.");
                    break;
            }
        }
    }

    private void showAllUsers() {
        List<User> usersList = db.getAllUsers();
        if (usersList.isEmpty()) {
            System.out.println("Әзірге тіркелген студенттер жоқ.");
            return;
        }
        System.out.println("\n     Студенттер тізімі");
        for (User u : usersList) {
            System.out.println(u);
        }
    }

    private void showAllHomeworks() {
        List<Homework> homeworkList = db.getAllHomeworks();
        if (homeworkList.isEmpty()) {
            System.out.println("Тапсырмалар жоқ.");
            return;
        }
        System.out.println("\n     Барлық тапсырмалар    ");
        for (Homework h : homeworkList) {
            System.out.println("\nТапсырма ID: " + h.getId() + "\nСтудент ID: " + h.getStudentId()
                    + "\nТақырып: " + h.getTitle()
                    + "\nДедлайн: " + h.getDeadline()
                    + "\nОрындалды: " + h.isCompleted()
                    + "\nБаға: " + (h.getGrade() == null ? "—" : h.getGrade()));
            if (!h.getTeacherComment().isEmpty()) System.out.println("\nПікір: " + h.getTeacherComment());
        }
    }

    private void createHomework() {
        System.out.print("Тапсырма атауы: ");
        String title = scanner.nextLine().trim();
        System.out.print("Мерзімі (YYYY-MM-DD) дейін: ");
        String deadline = scanner.nextLine().trim();
        System.out.print("Тапсырманы барлығына тағайындау(ALL) немесе жеке студенттің ID енгізіңіз: ");
        String studentId = scanner.nextLine().trim();
        if (studentId.isEmpty()) studentId = "ALL";

        // ID генерациясы
        String id = db.generateHomeworkId();
        Homework hw = new Homework(id, studentId, title, deadline);
        db.addHomework(hw);
        System.out.println("Тапсырма қосылды. Тапсырма ID: " + id);
    }

    private void deleteHomework() {
        System.out.print("Жою үшін тапсырма ID енгізіңіз: ");
        String id = scanner.nextLine().trim();
        db.removeHomework(id);
        System.out.println("Тапсырма жойылды.");
    }

    private void gradeHomework() {
        System.out.println("\n    Бағалау    ");
        System.out.print("Тапсырма ID: ");
        String id = scanner.nextLine().trim();
        java.util.Optional<Homework> homeworkOptional = db.findHomeworkById(id);
        if (homeworkOptional.isPresent()) {
            Homework h = homeworkOptional.get();
            System.out.print("Қойылатын баға(0-100): ");
            String gradeS = scanner.nextLine().trim();
            try {
                int g = Integer.parseInt(gradeS);
                if (g < 0 || g > 100) {
                    System.out.println("Баға қатаң түрде 0-100 аралығында болуы керек!");
                    return;
                }
                h.setGrade(g);
                System.out.print("Сіздің пікіріңіз (міндетті емес): ");
                String comment = scanner.nextLine().trim();
                h.setTeacherComment(comment);
                System.out.println("Тапсырма бағаланды.");
            } catch (NumberFormatException ex) {
                System.out.println("Сан енгізу қажет.");
            }
        } else {
            System.out.println("Бұл ID арқылы тапсырма табылмады.");
        }
    }

    private void showStudentStats() {
        System.out.print("Студент ID енгізіңіз: ");
        String id = scanner.nextLine().trim();
        java.util.Optional<User> userOptional = db.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Double avg = db.averageGradeForStudent(id);
            System.out.println("Студент есімі: " + user.getName() + "\nLogin: " + user.getLogin());
            if (avg == null) {
                System.out.println("Бағалары жоқ.");
            } else {
                System.out.printf("Орташа баға: %.2f%n", avg);
            }
            System.out.println("Бар тапсырмалар:");
            for (Homework h : db.getHomeworksForStudent(id)) {
                System.out.println(h);
            }
        } else {
            System.out.println("Бұл ID арқылы студент табылмады.");
        }
    }
}
