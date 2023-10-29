import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Интерфейс для создания объектов Students
interface StudentsFactory {
    Students createStudents(String[] tnames, double[][] tscores);
}

// Реализация фабрики, создающей объекты Students
class ConcreteStudentsFactory implements StudentsFactory {
    @Override
    public Students createStudents(String[] tnames, double[][] tscores) {
        return new Students(tnames, tscores);
    }
}

// Класс Students
class Students {
    private String[] names = new String[5];
    private char[] grades = new char[5];
    private double[][] scores = new double[5][4];

    public Students(String[] tnames, double[][] tscores) {
        int i, j;

        for (i = 0; i < 5; i++) {
            names[i] = tnames[i];
        }

        for (i = 0; i < 5; i++) {
            for (j = 0; j < 4; j++) {
                scores[i][j] = tscores[i][j];
            }
        }
    }

    public String getName(int index) {
        return names[index];
    }

    public double getAvgScore(int index) {
        double sum = 0;
        for (int i = 0; i < 4; i++) {
            sum = sum + scores[index][i];
        }
        return (sum / 4.0);
    }

    public char getGrade(double avg) {
        if (avg >= 90) {
            return 'A';
        } else if (avg >= 80) {
            return 'B';
        } else if (avg >= 70) {
            return 'C';
        } else if (avg >= 60) {
            return 'D';
        } else {
            return 'F';
        }
    }
}

// Интерфейс для наблюдателя
interface Observer {
    void update(String name, double avg, char grade);
}

// Реализация наблюдателя
class GradeBookObserver implements Observer {
    @Override
    public void update(String name, double avg, char grade) {
        System.out.printf("\n %-10s %-10.2f %-10c ", name, avg, grade);
    }
}

// Класс для субъекта (GradeBook)
class GradeBook {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String name, double avg, char grade) {
        for (Observer observer : observers) {
            observer.update(name, avg, grade);
        }
    }
}

public class Main {

    public static void main(String[] args) {
        String[] names = new String[5];
        double[][] scores = new double[5][4];
        Scanner keyboard = new Scanner(System.in);

        int i, j;
        for (i = 0; i < 5; i++) {
            System.out.print("\n\n Enter student name: ");
            names[i] = keyboard.nextLine();

            System.out.print("\n Enter four scores of " + names[i] + ": ");

            for (j = 0; j < 4; j++) {
                scores[i][j] = keyboard.nextDouble();

                while (scores[i][j] < 0 || scores[i][j] > 100) {
                    System.out.print("\n\n Invalid Score. Please Re-enter: ");
                    scores[i][j] = keyboard.nextDouble();
                }
            }
            keyboard.nextLine();
        }

        // Создаем фабрику и объект Students
        StudentsFactory factory = new ConcreteStudentsFactory();
        Students studentsObj = factory.createStudents(names, scores);

        GradeBook gradeBook = new GradeBook();
        gradeBook.addObserver(new GradeBookObserver());

        System.out.printf("\n\n %-10s %-10s %-10s \n", "Name", "Average", "Grade");

        double avg;
        for (i = 0; i < 5; i++) {
            avg = studentsObj.getAvgScore(i);
            char grade = studentsObj.getGrade(avg);
            gradeBook.notifyObservers(studentsObj.getName(i), avg, grade);
        }

        System.out.println("\n\n");
    }
}
