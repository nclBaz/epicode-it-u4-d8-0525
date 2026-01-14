import com.github.javafaker.Faker;
import entities.User;
import functional_interfaces.StringModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
	public static void main(String[] args) {
		// Se esiste un'interfaccia funzionale che faccia al caso, io posso creare una lambda function su di essa
		StringModifier dotsWrapper = str -> "..." + str + "...";
		StringModifier starsWrapper = str -> "***" + str + "***";

		StringModifier stringInverter = str -> {
			String[] splitted = str.split("");
			String inverted = "";

			for (int i = splitted.length - 1; i >= 0; i--) {
				inverted += splitted[i];
			}
			return inverted;
		};

		System.out.println(dotsWrapper.modify("HELLO WORLD"));
		System.out.println(starsWrapper.modify("HELLO WORLD"));
		System.out.println(stringInverter.modify("HELLO WORLD"));


		// ********************************** SUPPLIER **************************************
		Supplier<Integer> randomIntSupplier = () -> {
			Random rndm = new Random();
			return rndm.nextInt(1, 100000);
		};

		List<Integer> randomIntNumbers = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			randomIntNumbers.add(randomIntSupplier.get());
		}

		System.out.println(randomIntNumbers);

		Supplier<User> randomUserSupplier = () -> {
			Faker faker = new Faker(Locale.ITALIAN);
			Random rndm = new Random();
			return new User(faker.lordOfTheRings().character(), faker.name().lastName(), rndm.nextInt(0, 100));
		};

		List<User> randomUsers = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			randomUsers.add(randomUserSupplier.get());
		}

//		for (User user : randomUsers) {
//			System.out.println(user);
//		}

		Supplier<ArrayList<User>> randomUserListSupplier = () -> {
			ArrayList<User> randomUsers2 = new ArrayList<>();
			Faker faker = new Faker(Locale.ITALIAN);
			Random rndm = new Random();
			for (int i = 0; i < 100; i++) {
				randomUsers2.add(new User(faker.lordOfTheRings().character(), faker.name().lastName(), rndm.nextInt(0, 100)));
			}
			return randomUsers2;
		};

		ArrayList<User> users = randomUserListSupplier.get();
		users.forEach(user -> System.out.println(user));
		// users.forEach(System.out::println);

		// **************************************************** PREDICATES *******************************************
		Predicate<Integer> isMoreThanZero = number -> number > 0;
		Predicate<Integer> isLessThanHundred = number -> number < 100;

		System.out.println(isMoreThanZero.test(-20));
		System.out.println(isLessThanHundred.test(30));

		System.out.println(isMoreThanZero.and(isLessThanHundred).test(50));

		Predicate<User> isAdult = user -> user.getAge() >= 18;

		users.forEach(user -> {
			if (isAdult.test(user)) System.out.println("L'utente " + user + " è maggiorenne");
			else System.out.println("L'utente " + user + " è minorenne");
		});

	}
}
