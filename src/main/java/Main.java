public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Task Tracker CLI!");

        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }
    }
}