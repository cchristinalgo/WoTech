public class Main {
public static void main(String[] args) {  
float temperature = 5;

if (temperature < 5) {
  System.out.println("Wear super warm clothes!");
}
else if (temperature >= 5 && temperature < 15) {
  System.out.println("Wear warm clothes!");
}
else if (temperature >= 15 && temperature < 30) {
  System.out.println("Wear a normal clothes!");
}
else if (temperature >= 30) {
  System.out.println("Wear a cooling cloathes!");
}
}
}
