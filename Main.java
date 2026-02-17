import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();

        initSampleData(db);

        Admin admin = new Admin(db, scanner);
        HomeworkManager manager = new HomeworkManager(db, scanner);

        System.out.println("    Студенттердің үй тапсырмасын есепке алу жүйесіне қош келдіңіз!    ");

        mainLoop:
        while (true) {
            System.out.println("\n1. Кіру");
            System.out.println("2. Тіркелу");
            System.out.println("3. Әкімші ретінде кіру");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": // кіру
                    System.out.print("Логин: ");
                    String login = scanner.nextLine().trim();
                    System.out.print("Құпия сөз: ");
                    String pwd = scanner.nextLine().trim();
                    java.util.Optional<User> userOptional = db.findByLogin(login);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        if (user.checkPassword(pwd)) {
                            System.out.println("Қош келдіңіз, " + user.getName() + "!");
                            manager.userMenu(user);
                        } else {
                            System.out.println("Қате құпия сөз.");
                        }
                    } else {
                        System.out.println("Пайдаланушы табылмады. Тіркеліңіз.");
                    }
                    break;
                case "2": // тіркелу
                    System.out.print("Атыңыз: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Топ: ");
                    String group = scanner.nextLine().trim();
                    System.out.print("Логин ойлап табыңыз: ");
                    String newLogin = scanner.nextLine().trim();
                    if (db.findByLogin(newLogin).isPresent()) {
                        System.out.println("Бұл логин әлбетте қолданыста бар. Басқа логин таңдаңыз.");
                        break;
                    }
                    System.out.print("Құпия сөз ойлап табыңыз: ");
                    String newPwd = scanner.nextLine().trim();

                    // студент ID генерациясы
                    String id = db.generateUserId();

                    User newUser = new User(id, name, newLogin, newPwd, group);
                    db.addUser(newUser);
                    System.out.println("Тіркелу сәтті болды. Сіздің ID: " + id);
                    break;
                case "3": // admin login
                    System.out.print("Логин: ");
                    String adminLogin = scanner.nextLine().trim();
                    System.out.print("Құпия сөз: ");
                    String adminPwd = scanner.nextLine().trim();
                    if (Admin.ADMIN_LOGIN.equals(adminLogin) && Admin.ADMIN_PASSWORD.equals(adminPwd)) {
                        System.out.println("Сіз әкімші ретінде жүйеге кірдіңіз.");
                        admin.adminMenu();
                    } else {
                        System.out.println("Деректер сәйкес келмейді.");
                    }
                    break;
                case "0":
                    System.out.println("Жүйеден шығу...");
                    break mainLoop;
                default:
                    System.out.println("Қате таңдау. Қайталаңыз.");
            }
        }

        scanner.close();
    }

    private static void initSampleData(Database db) {
        User u1 = new User(db.generateUserId(), "Ли Минхо", "leeknow", "2510", "skz");
        User u2 = new User(db.generateUserId(), "Пак Сонхун", "sunghoon", "iceprince", "en-");
        User u3 = new User(db.generateUserId(), "Утинага Эри", "giselle", "2000", "ae");
        db.addUser(u1);
        db.addUser(u2);
        db.addUser(u3);
        // бір жалпы тапсырма (барлығына)
        Homework hw1 = new Homework(db.generateHomeworkId(), "ALL", "Java Collections туралы жан-жақты біліп келу", "2025-12-01");
        db.addHomework(hw1);
        // жеке тапсырмалар
        Homework hw2 = new Homework(db.generateHomeworkId(), u1.getId(), "түсіндіруге тақырып - Java Collections: List (ArrayList, LinkedList)", "2025-12-01");
        Homework hw3 = new Homework(db.generateHomeworkId(), u2.getId(), "түсіндіруге тақырып - Java Collections: Set (HashSet, LinkedHashSet. TreeSet)", "2025-12-01");
        Homework hw4 = new Homework(db.generateHomeworkId(), u3.getId(), "түсіндіруге тақырып - Java Collections: Queue (PriorityQueue, ArrayDeque)", "2025-12-01");
        db.addHomework(hw2);
        db.addHomework(hw3);
        db.addHomework(hw4);
    }
}
