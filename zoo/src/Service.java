import Animal.*;
import DBServices.BirdEnclosureService;
import DBServices.EmployeeService;
import DBServices.MammalEnclosureService;
import DBServices.ReptileEnclosureService;
import Employee.Employee;
import Enclosure.*;
import Exceptions.InvalidPhoneNumber;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Service {
    private static Service instance;
    private EmployeeService employeeService;
    private BirdEnclosureService birdEnclosureService;
    private MammalEnclosureService mammalEnclosureService;
    private ReptileEnclosureService reptileEnclosureService;
    List<Animal> animals = new ArrayList<>();
    List<Enclosure> enclosures = new ArrayList<>();
    List<Employee> employees = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    List<Integer> unmanagedEnclosures = new ArrayList<>();

    File audit = new File("audit.csv");
    PrintWriter writer = null;
    private Service(){
        employeeService = EmployeeService.getInstance();
        employeeService.deleteAllEmployees();
        birdEnclosureService = BirdEnclosureService.getInstance();
        birdEnclosureService.deleteAllBirdEnc();
        mammalEnclosureService = MammalEnclosureService.getInstance();
        mammalEnclosureService.deleteAllMammalEnc();
        reptileEnclosureService = ReptileEnclosureService.getInstance();
        reptileEnclosureService.deleteAllReptileEnc();

        try {
            writer = new PrintWriter(audit);
            writer.println("Timestap" + ", " + "Action");
            writer.flush();
        } catch (IOException ex){
            System.out.println("Can't create the audit file!");
        }
    }
    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }
    private void makeAudit(String action){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        writer.print(timestamp + ", ");
        writer.println(action);
        writer.flush();
    }
    public void showEmployees(){
        employees.forEach(System.out::println);
        makeAudit("Show all employees");
    }
    public void addEmployee(){
        System.out.println("First name:");
        String firstName = scanner.nextLine();
        System.out.println("Last name:");
        String lastName =  scanner.nextLine();
        System.out.println("Phone number:");
        try {
            String phoneNumber = scanner.nextLine();
            if(phoneNumber.length() != 10){
                throw new InvalidPhoneNumber();
            }
            System.out.println("Email: ");
            String email = scanner.nextLine();
            Employee emp = new Employee(firstName, lastName, phoneNumber, email);
            employees.add(emp);
            employeeService.addEmployee(emp);
            makeAudit("Added the employee " + firstName + " " + lastName);
            Collections.sort(employees, Comparator.comparing(Employee::toString));
        }
        catch (InvalidPhoneNumber e) {
            System.out.println("Numar de telefon invalid");
        }
    }
    public void deleteEmployee(){
        showEmployees();
        System.out.println("ID of employee:");
        Integer id = Integer.parseInt(scanner.nextLine());
        Employee employee = employees.stream()
                .filter(emp -> id.equals(emp.getEmployeeId()))
                .findFirst()
                .orElse(null);
        if(employee == null){
            System.out.println("There is no employee with this id.");
        }
        else {
            unmanagedEnclosures.addAll(employee.getEnclosureIds());

            employees.remove(employee);
            employeeService.deleteEmployee(employee.getEmployeeId());
            makeAudit("Deleted employee " + employee.getFirstName() + " " + employee.getLastName());
            System.out.println("Successful!");
        }
    }
    public void addEnclosure(){
        System.out.println("Enter what kind of enclosure you wish to build:\n" +
                "1.Bird enclosure\n" +
                "2.Reptile enclosure\n" +
                "3.Mammal enclosure\n");
        Integer command = Integer.parseInt(scanner.nextLine());
        switch (command){
            case 1 -> {
                System.out.println("Name:");
                String name = scanner.nextLine();
                System.out.println("Surface:");
                Integer surface = Integer.parseInt(scanner.nextLine());
                System.out.println("Maximum capacity:");
                Integer maxCapacity = Integer.parseInt(scanner.nextLine());
                System.out.println("Height:");
                Integer height = Integer.parseInt(scanner.nextLine());
                System.out.println("Number of nests:");
                Integer nrOfNests = Integer.parseInt(scanner.nextLine());

                Enclosure e = new BirdEnclosure(surface, maxCapacity, name, nrOfNests, height);
                makeAudit("Added bird enclosure " + name);

                enclosures.add(e);
                birdEnclosureService.addBirdEnc((BirdEnclosure) e);
                unmanagedEnclosures.add(e.getEnclosureId());
            }
            case 2 -> {
                System.out.println("Name:");
                String name = scanner.nextLine();
                System.out.println("Surface:");
                Integer surface = Integer.parseInt(scanner.nextLine());
                System.out.println("Maximum capacity:");
                Integer maxCapacity = Integer.parseInt(scanner.nextLine());
                System.out.println("Temperature:");
                Integer temperature = Integer.parseInt(scanner.nextLine());

                Enclosure e = new ReptileEnclosure(surface, maxCapacity, name, temperature);
                makeAudit("Added reptile enclosure " + name);

                enclosures.add(e);
                reptileEnclosureService.addReptileEnc((ReptileEnclosure) e);
                unmanagedEnclosures.add(e.getEnclosureId());
            }
            case 3 -> {
                System.out.println("Name:");
                String name = scanner.nextLine();
                System.out.println("Surface:");
                Integer surface = Integer.parseInt(scanner.nextLine());
                System.out.println("Maximum capacity:");
                Integer maxCapacity = Integer.parseInt(scanner.nextLine());
                System.out.println("Shaded surface:");
                Integer shadedSurface = Integer.parseInt(scanner.nextLine());

                Enclosure e = new MammalEnclosure(surface, maxCapacity, name, shadedSurface);
                makeAudit("Added mammal enclosure " + name);

                enclosures.add(e);
                mammalEnclosureService.addMammalEnc((MammalEnclosure) e);
                unmanagedEnclosures.add(e.getEnclosureId());
            }
            default -> System.out.println("Invalid command");
        }
    }
    public void deleteEnclosure(){
        showAllEnclosures();
        System.out.println("ID of enclosure:");
        Integer id = Integer.parseInt(scanner.nextLine());
        Enclosure enclosure = enclosures.stream()
                .filter(enc -> id.equals(enc.getEnclosureId()))
                .findFirst()
                .orElse(null);
        if(enclosure == null){
            System.out.println("There is no enclosure with this id.");
        }
        else {
            if (unmanagedEnclosures.contains(enclosure.getEnclosureId())){
                unmanagedEnclosures.remove(enclosure.getEnclosureId());
            }
            else{
                Employee e = employees.stream()
                        .filter(emp -> emp.getEnclosureIds().contains(enclosure.getEnclosureId()))
                        .findFirst()
                        .orElse(null);

                e.removeResponsibility(enclosure.getEnclosureId());
            }
            animals.stream()
                    .filter(animal -> animal.getEnclosureId() == enclosure.getEnclosureId())
                    .forEach(a -> a.setEnclosureId(-1));
            enclosures.remove(enclosure);
            if(enclosure instanceof BirdEnclosure) {
                birdEnclosureService.deleteBirdEnc(enclosure.getEnclosureId());
            } else if (enclosure instanceof MammalEnclosure) {
                mammalEnclosureService.deleteMammalEnc(enclosure.getEnclosureId());
            } else {
                reptileEnclosureService.deleteReptileEnc(enclosure.getEnclosureId());
            }
            makeAudit("Deleted enclosure " + enclosure.getName());
            System.out.println("Successful!");
        }
    }
    public boolean showAvailableEnclosures(){
        List<Enclosure> encl = enclosures.stream()
                .filter(enclosure -> enclosure.getCurrentCapacity() != enclosure.getMaxCapacity())
                .toList();
        makeAudit("Show available enclosures");
        if (encl.isEmpty()) {
            System.out.println("There are no available enclosures");
            return false;
        }
        else{
            System.out.println("Available enclosures:");
            encl.forEach(System.out::println);
            return true;
        }
    }
    public Enclosure getEnclosureById(Integer id){
        return enclosures.stream()
                .filter(enc -> id.equals(enc.getEnclosureId()))
                .findFirst()
                .orElse(null);
    }
    public void addAnimalToEnclosure(){
        if (showAvailableEnclosures()) {

            List<Animal> unhousedAnimals = animals.stream()
                    .filter(animal -> animal.getEnclosureId() == -1)
                    .toList();
            if (!unhousedAnimals.isEmpty()){
                System.out.println("Unhoused animals:");
                unhousedAnimals.forEach(System.out::println);
                List<Integer> availableIds = unhousedAnimals.stream()
                            .map(animal -> animal.getAnimalId())
                            .toList();
                System.out.println("Choose animal id:");
                Integer id = Integer.parseInt(scanner.nextLine());
                while (!availableIds.contains(id)) {
                    System.out.println("Please choose a valid id:");
                    id = Integer.parseInt(scanner.nextLine());
                }

                Animal animal = getAnimalById(id);

                List<Enclosure> encl = enclosures.stream()
                        .filter(enclosure -> enclosure.getCurrentCapacity() != enclosure.getMaxCapacity())
                        .filter(enclosure -> enclosure.getEnclosureId() != animal.getEnclosureId())
                        .filter(enclosure -> {
                            if (animal.getCategory() == Category.Bird && enclosure instanceof BirdEnclosure)
                                return true;
                            else if (animal.getCategory() == Category.Reptile && enclosure instanceof ReptileEnclosure)
                                return true;
                            else if (animal.getCategory() == Category.Mammal && enclosure instanceof MammalEnclosure)
                                return true;
                            return false;
                        })
                        .toList();
                if (encl.isEmpty()) {
                    System.out.println("There are no available enclosures");
                }
                else {
                    System.out.println("Available enclosures:");
                    encl.forEach(System.out::println);

                    List<Integer> availableIds2 = encl.stream()
                            .map(ani -> ani.getEnclosureId())
                            .toList();
                    System.out.println("Choose enclosure id:");
                    Integer id2 = Integer.parseInt(scanner.nextLine());
                    while (!availableIds2.contains(id2)) {
                        System.out.println("Please choose a valid id:");
                        id2 = Integer.parseInt(scanner.nextLine());
                    }

                    Enclosure newEnclosure = getEnclosureById(id2);
                    newEnclosure.addAnimal(id);
                    animal.setEnclosureId(id2);
                    makeAudit("Added animal " + animal.getName() + " to enclosure " + newEnclosure.getName());
                }

            }
            else {

                List<Integer> availableIds = enclosures.stream()
                        .filter(enclosure -> enclosure.getCurrentCapacity() != enclosure.getMaxCapacity())
                        .map(enc -> enc.getEnclosureId())
                        .toList();

                System.out.println("Choose enclosure id:");
                Integer id = Integer.parseInt(scanner.nextLine());
                while (!availableIds.contains(id)) {
                    System.out.println("Please choose a valid id:");
                    id = Integer.parseInt(scanner.nextLine());
                }

                System.out.println("Name:");
                String name = scanner.nextLine();
                System.out.println("Specie:");
                String specie = scanner.nextLine();
                System.out.println("Age:");
                Integer age = Integer.parseInt(scanner.nextLine());
                System.out.println("Category:\n" +
                        "1.Bird\n" +
                        "2.Reptile\n" +
                        "3.Mammal\n");
                Integer option = Integer.parseInt(scanner.nextLine());
                while (option != 1 && option != 2 && option != 3) {
                    System.out.println("Please enter a number between 1 and 3:");
                    option = Integer.parseInt(scanner.nextLine());
                }
                Category category = Category.Bird;
                if (option == 2) {
                    category = Category.Reptile;
                } else {
                    if (option == 3) {
                        category = Category.Mammal;
                    }
                }
                Enclosure e = getEnclosureById(id);
                if ((option == 1 && e instanceof BirdEnclosure)
                        || (option == 2 && e instanceof ReptileEnclosure)
                        || (option == 3 && e instanceof MammalEnclosure)) {

                    Animal newAnimal = new Animal(name, category, specie, age);
                    newAnimal.setEnclosureId(id);
                    e.addAnimal(newAnimal.getAnimalId());
                    animals.add(newAnimal);
                    makeAudit("Added animal " + newAnimal.getName() + " to enclosure " + e.getName());
                } else {
                    System.out.println("Error! The animal can't reside in the enclosure.");
                }
            }
        }
        else{
            System.out.println("Please build some enclosures!");
        }
    }
    public void showAllAnimals(){
        animals.forEach(System.out::println);
        makeAudit("Show all animals");
    }
    public void showAnimalEnclosure(){
        showAllAnimals();
        List<Integer> availableIds = animals.stream()
                .map(ani -> ani.getAnimalId())
                .toList();
        System.out.println("Choose animal id:");
        Integer id = Integer.parseInt(scanner.nextLine());
        while(!availableIds.contains(id)){
            System.out.println("Please choose a valid id:");
            id = Integer.parseInt(scanner.nextLine());
        }
        final Integer id1 = id;
        Enclosure enclosure = enclosures.stream()
                .filter(enc -> enc.getAnimalIds().contains(id1))
                .findFirst()
                .orElse(null);
        makeAudit("Show animals' " + getAnimalById(id).getName() + " enclosure");
        if (enclosure == null){
            System.out.println("This animal is unhoused.");
        }
        else {
            System.out.println(enclosure);
        }
    }
    public void showAllEnclosures(){
        enclosures.forEach(System.out::println);
        makeAudit("Show all enclosures");
    }
    public Animal getAnimalById(Integer id){
        return animals.stream()
                .filter(ani -> id.equals(ani.getAnimalId()))
                .findFirst()
                .orElse(null);
    }
    public void showAnimalsFromEnclosure(){
        showAllEnclosures();
        List<Integer> availableIds = enclosures.stream()
                .map(ani -> ani.getEnclosureId())
                .toList();
        System.out.println("Choose enclosure id:");
        Integer id = Integer.parseInt(scanner.nextLine());
        while(!availableIds.contains(id)){
            System.out.println("Please choose a valid id:");
            id = Integer.parseInt(scanner.nextLine());
        }

        Enclosure enc = getEnclosureById(id);
        enc.getAnimalIds().stream().map(this::getAnimalById).forEach(System.out::println);
        makeAudit("Show all animals from enclosure " + enc.getName());
    }
    public Employee getEmployeeById(Integer id){
        return employees.stream()
                .filter(emp -> id.equals(emp.getEmployeeId()))
                .findFirst()
                .orElse(null);
    }
    public void addResponsibilityToEmployee(){
        if (unmanagedEnclosures.isEmpty()){
            System.out.println("There are no unmanaged enclosures.");
        }
        else {
            showEmployees();
            List<Integer> availableIds = employees.stream()
                    .map(ani -> ani.getEmployeeId())
                    .toList();
            System.out.println("Choose employee id:");
            Integer id = Integer.parseInt(scanner.nextLine());
            while (!availableIds.contains(id)) {
                System.out.println("Please choose a valid id:");
                id = Integer.parseInt(scanner.nextLine());
            }

            Employee emp = getEmployeeById(id);
            System.out.println("Unmanaged enclosures:");
            enclosures.stream()
                    .filter(enc -> unmanagedEnclosures.contains(enc.getEnclosureId()))
                    .forEach(System.out::println);
            System.out.println("Choose enclosure id:");
            Integer id2 = Integer.parseInt(scanner.nextLine());
            while (!unmanagedEnclosures.contains(id2)) {
                System.out.println("Please choose a valid id:");
                id2 = Integer.parseInt(scanner.nextLine());
            }
            emp.addResponsibility(id2);
            unmanagedEnclosures.remove(id2);
            makeAudit("Added responsibility to " + emp.getFirstName() + " " + emp.getLastName());
        }
    }
    public void transferAnimal(){
        showAllAnimals();
        List<Integer> availableIds = animals.stream()
                .map(ani -> ani.getAnimalId())
                .toList();
        System.out.println("Choose animal id:");
        Integer id = Integer.parseInt(scanner.nextLine());
        while(!availableIds.contains(id)){
            System.out.println("Please choose a valid id:");
            id = Integer.parseInt(scanner.nextLine());
        }

        Animal animal = getAnimalById(id);

        List<Enclosure> encl = enclosures.stream()
                .filter(enclosure -> enclosure.getCurrentCapacity() != enclosure.getMaxCapacity())
                .filter(enclosure -> enclosure.getEnclosureId() != animal.getEnclosureId())
                .filter(enclosure -> {
                    if (animal.getCategory() == Category.Bird && enclosure instanceof BirdEnclosure)
                        return true;
                    else if (animal.getCategory() == Category.Reptile && enclosure instanceof ReptileEnclosure)
                        return true;
                    else if (animal.getCategory() == Category.Mammal && enclosure instanceof MammalEnclosure)
                        return true;
                    return false;
                })
                .toList();
        if (encl.isEmpty()) {
            System.out.println("There are no available enclosures");
        }
        else {
            System.out.println("Available enclosures:");
            encl.forEach(System.out::println);

            List<Integer> availableIds2 = encl.stream()
                    .map(ani -> ani.getEnclosureId())
                    .toList();
            System.out.println("Choose enclosure id:");
            Integer id2 = Integer.parseInt(scanner.nextLine());
            while (!availableIds2.contains(id2)) {
                System.out.println("Please choose a valid id:");
                id2 = Integer.parseInt(scanner.nextLine());
            }

            Enclosure newEnclosure = getEnclosureById(id2);
            newEnclosure.addAnimal(id);
            Enclosure exEnclosure = getEnclosureById(animal.getEnclosureId());
            exEnclosure.removeAnimal(id);
            animal.setEnclosureId(id2);
            makeAudit("Transferred animal " + animal.getName() + " from enclosure "
                    + exEnclosure.getName() + " to enclosure " + newEnclosure.getName());
        }
    }
    public void showEmployeeResponsibility(){
        showEmployees();
        List<Integer> availableIds = employees.stream()
                .map(ani -> ani.getEmployeeId())
                .toList();
        System.out.println("Choose employee id:");
        Integer id = Integer.parseInt(scanner.nextLine());
        while (!availableIds.contains(id)) {
            System.out.println("Please choose a valid id:");
            id = Integer.parseInt(scanner.nextLine());
        }

        Employee emp = getEmployeeById(id);
        makeAudit("Show employee's " + emp.getFirstName() + " " + emp.getLastName() + " responsibility");
        if (emp.getEnclosureIds().isEmpty()){
            System.out.println("He has no responsibility of enclosures.");
        }
        else{
            System.out.println("He is responsible for:");
            enclosures.stream()
                .filter(enc -> emp.getEnclosureIds().contains(enc.getEnclosureId()))
                .forEach(System.out::println);
        }
    }
    public void transferResponsibility(){
        System.out.println("Transfer responsibility from:");
        employees.stream()
                .filter(emp -> !emp.getEnclosureIds().isEmpty())
                .forEach(System.out::println);

        List<Integer> availableIds = employees.stream()
                .filter(emp -> !emp.getEnclosureIds().isEmpty())
                .map(emp -> emp.getEmployeeId())
                .toList();

        System.out.println("Choose employee id:");
        Integer idFrom = Integer.parseInt(scanner.nextLine());
        while (!availableIds.contains(idFrom)) {
            System.out.println("Please choose a valid id:");
            idFrom = Integer.parseInt(scanner.nextLine());
        }

        final Integer fromId = idFrom;

        System.out.println("Transfer responsibility to:");
        employees.stream()
                .filter(emp -> emp.getEmployeeId()!=fromId)
                .forEach(System.out::println);

        List<Integer> availableIds2 = employees.stream()
                .filter(emp -> emp.getEmployeeId()!=fromId)
                .map(emp -> emp.getEmployeeId())
                .toList();

        Integer idTo = Integer.parseInt(scanner.nextLine());
        while (!availableIds2.contains(idTo)) {
            System.out.println("Please choose a valid id:");
            idTo = Integer.parseInt(scanner.nextLine());
        }

        Employee employeeFrom = getEmployeeById(idFrom);
        Employee employeeTo = getEmployeeById(idTo);

        System.out.println("Choose which responsibility to transfer:");
        enclosures.stream()
                .filter(enc -> employeeFrom.getEnclosureIds().contains(enc.getEnclosureId()))
                .forEach(System.out::println);

        List<Integer> availableIds3 = enclosures.stream()
                .map(enc -> enc.getEnclosureId())
                .filter(enclosureId -> employeeFrom.getEnclosureIds().contains(enclosureId))
                .toList();

        Integer idResp = Integer.parseInt(scanner.nextLine());
        while (!availableIds3.contains(idResp)) {
            System.out.println("Please choose a valid id:");
            idResp = Integer.parseInt(scanner.nextLine());
        }

        employeeFrom.removeResponsibility(idResp);
        employeeTo.addResponsibility(idResp);
        makeAudit("Transferred responsibility from " + employeeFrom.getFirstName()
                + " " + employeeFrom.getLastName() + " to " + employeeTo.getFirstName() + " " + employeeTo.getLastName());

    }
    public void employeeUpdatePhone(){
        showEmployees();
        List<Integer> availableIds = employees.stream()
                .map(e -> e.getEmployeeId()).toList();
        System.out.println("Choose employee id:");
        Integer idFrom = Integer.parseInt(scanner.nextLine());
        while (!availableIds.contains(idFrom)) {
            System.out.println("Please choose a valid id:");
            idFrom = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Enter new phone number: ");
        try {
            String phoneNumber = scanner.nextLine();
            if(phoneNumber.length() != 10){
                throw new InvalidPhoneNumber();
            }
            employeeService.updatePhoneNumber(idFrom, phoneNumber);
            Employee e = getEmployeeById(idFrom);
            e.setPhoneNumber(phoneNumber);
            makeAudit("Updated phone number for employee" + idFrom);
        }
        catch (InvalidPhoneNumber e){
            System.out.println("Numar de telefon invalid");
        }
    }
    public void birdUpdateNests(){
        showBridEncSQL();
        List<Integer> availableIds = enclosures.stream()
                .filter(enclosure -> enclosure instanceof BirdEnclosure)
                .map(e -> e.getEnclosureId()).toList();
        System.out.println("Choose enclosure id:");
        Integer idFrom = Integer.parseInt(scanner.nextLine());
        while (!availableIds.contains(idFrom)) {
            System.out.println("Please choose a valid id:");
            idFrom = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Enter new nr of nests: ");
        Integer nests = Integer.parseInt(scanner.nextLine());
        BirdEnclosure b = (BirdEnclosure) getEnclosureById(idFrom);
        b.setNrOfNests(nests);
        birdEnclosureService.updateNrOfNests(idFrom,nests);
        makeAudit("Updated nr of nests for bird enclosure" + idFrom);

    }
    public void mammalUpdateShade(){
        showMammalEncSQL();
        List<Integer> availableIds = enclosures.stream()
                .filter(enclosure -> enclosure instanceof MammalEnclosure)
                .map(e -> e.getEnclosureId()).toList();
        System.out.println("Choose enclosure id:");
        Integer idFrom = Integer.parseInt(scanner.nextLine());
        while (!availableIds.contains(idFrom)) {
            System.out.println("Please choose a valid id:");
            idFrom = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Enter new shaded surface: ");
        Integer nests = Integer.parseInt(scanner.nextLine());
        MammalEnclosure b = (MammalEnclosure) getEnclosureById(idFrom);
        b.setShadedSurface(nests);
        mammalEnclosureService.updateShadedSurface(idFrom,nests);
        makeAudit("Updated shaded surface for mammal enclosure" + idFrom);

    }
    public void reptileUpdateTemperature(){
        showReptileEncSQL();
        List<Integer> availableIds = enclosures.stream()
                .filter(enclosure -> enclosure instanceof ReptileEnclosure)
                .map(e -> e.getEnclosureId()).toList();
        System.out.println("Choose enclosure id:");
        Integer idFrom = Integer.parseInt(scanner.nextLine());
        while (!availableIds.contains(idFrom)) {
            System.out.println("Please choose a valid id:");
            idFrom = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Enter new temperature: ");
        Integer nests = Integer.parseInt(scanner.nextLine());
        ReptileEnclosure b = (ReptileEnclosure) getEnclosureById(idFrom);
        b.setTemperature(nests);
        reptileEnclosureService.updateTemperature(idFrom,nests);
        makeAudit("Updated temperature for reptile enclosure" + idFrom);

    }
    public void showEmployeesSQL(){
        employeeService.allEmployees();
        makeAudit("Show employees from SQL");
    }
    public void showBridEncSQL(){
        birdEnclosureService.allBirdEnc();
        makeAudit("Show bird enclosures from SQL");
    }
    public void showMammalEncSQL(){
        mammalEnclosureService.allMammalEnc();
        makeAudit("Show mammal enclosures from SQL");
    }
    public void showReptileEncSQL(){
        reptileEnclosureService.allReptileEnc();
        makeAudit("Show reptile enclosures from SQL");
    }
    public void showMenu(){
        System.out.println("Menu:\n" +
                "1.Hire an employee\n" +
                "2.Fire an employee:\n" +
                "3.Build enclosure\n" +
                "4.Demolish enclosure\n" +
                "5.Add animal to an enclosure\n" +
                "6.Show available enclosures\n" +
                "7.Show all enclosures\n" +
                "8.Transfer an animal to another enclosure\n" +
                "9.Show all animals\n" +
                "10.Show animals from enclosure\n" +
                "11.Show employees\n" +
                "12.Show an employees' responsibility\n" +
                "13.Give responsibility to an employee\n" +
                "14.Transfer responsibility to another employee\n" +
                "15.Show an animals' enclosure\n" +
                "16.Update employee phone number\n" +
                "17.Show employees from SQL\n" +
                "18.Show bird enclosures from SQL\n" +
                "19.Update bird enclosure nr of nests\n" +
                "20.Show mammal enclosures from SQL\n" +
                "21.Update mammal enclosure shaded surface\n" +
                "22.Show reptile enclosures from SQL\n" +
                "23.Update reptile enclosure temperature\n" +
                "24.Quit\n");
    }
}
