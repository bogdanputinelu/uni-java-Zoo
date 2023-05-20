import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Service service = Service.getInstance();
        Scanner scanner = new Scanner(System.in);
        Integer command;
        boolean quit = true;
        while (quit){
            service.showMenu();
            command = Integer.parseInt(scanner.nextLine());

            switch (command){
                case 1 -> service.addEmployee();
                case 2 -> service.deleteEmployee();
                case 3 -> service.addEnclosure();
                case 4 -> service.deleteEnclosure();
                case 5 -> service.addAnimalToEnclosure();
                case 6 -> service.showAvailableEnclosures();
                case 7 -> service.showAllEnclosures();
                case 8 -> service.transferAnimal();
                case 9 -> service.showAllAnimals();
                case 10 -> service.showAnimalsFromEnclosure();
                case 11 -> service.showEmployees();
                case 12 -> service.showEmployeeResponsibility();
                case 13 -> service.addResponsibilityToEmployee();
                case 14 -> service.transferResponsibility();
                case 15 -> service.showAnimalEnclosure();
                case 16 -> {
                    quit = false;
                    System.out.println("Leaving...");
                }
                default -> System.out.println("Invalid command");

            }
        }
    }
}