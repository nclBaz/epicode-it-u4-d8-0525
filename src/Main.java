import com.github.javafaker.Faker;
import entities.User;
import functional_interfaces.StringModifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

//		users.forEach(user -> {
//			if (isAdult.test(user)) System.out.println("L'utente " + user + " è maggiorenne");
//			else System.out.println("L'utente " + user + " è minorenne");
//		});

		// ****************************************** REMOVE IF **************************************
		//users.removeIf(user -> user.getAge() >= 18);
		// users.removeIf(isAdult); come sopra ma riutilizzando una lambda Predicate già definito in precedenza

		users.forEach(user -> System.out.println(user));


		// ******************************************* STREAM - OPERAZIONI INTERMEDIE ***************************************

		System.out.println("------------------------------------ FILTER --------------------------------------");
		Stream<User> stream = users.stream().filter(user -> user.getAge() < 18);
		stream.forEach(user -> System.out.println(user));

		System.out.println("------------------------------------ MAP --------------------------------------");
		Stream<String> nomiConcatenati = users.stream().map(user -> user.getName() + " --- " + user.getSurname());
		nomiConcatenati.forEach(fullName -> System.out.println(fullName));
		Stream<Integer> streamAges = users.stream().map(user -> user.getAge());
		streamAges.forEach(age -> System.out.println(age));

		System.out.println("------------------------------------ FILTER & MAP --------------------------------------");
		users.stream()
				.filter(user -> user.getAge() >= 18)
				.map(user -> user.getSurname())
				.forEach(surname -> System.out.println(surname));

		// ******************************************* STREAM - OPERAZIONI TERMINALI ***************************************
		System.out.println("------------------------------------ ANYMATCH & ALLMATCH --------------------------------------");
		// Sono i corrispettivi di .some() e .every() del mondo JS
		// AnyMatch: controlla se ALMENO UN elemento della lista/stream soddisfa una certa condizione (Predicate)
		// AllMatch: controlla se TUTTI gli elementi della lista/stream soddsifano una certa condizione (Predicate)
		// Entrambi quindi tornano un BOOLEANO

		if (users.stream().anyMatch(user -> user.getAge() >= 18)) System.out.println("Almeno uno è maggiorenne");
		else System.out.println("Sono tutti minorenni");

		if (users.stream().allMatch(user -> user.getAge() >= 18)) System.out.println("Sono tutti maggiorenni");
		else System.out.println("C'è almeno un minorenne");

		System.out.println("------------------------------------ REDUCE --------------------------------------");
		int total = users.stream()
				.filter(user -> user.getAge() < 18)
				.map(user -> user.getAge())
				.reduce(0, (partialSum, currElem) -> partialSum + currElem);
		System.out.println("Totale delle età dei minorenni: " + total);

		System.out.println("------------------------------------ TO LIST --------------------------------------");
		List<User> minorenni = users.stream().filter(user -> user.getAge() < 18).toList(); // Crea una NUOVA lista
		System.out.println(minorenni);

		// List<User> minorenni2 = users.stream().filter(user -> user.getAge() < 18).collect(Collectors.toList());
		// Alternativa a sopra meno compatta e leggibile, però tramite il collect avremo per le mani un metodo versatile che fa molte cose in più (vedremo domani)

		// **************************************** DATE *********************
		// Per semplicità per gestire le date in Java meglio usare LocalDate piuttosto che Date
		LocalDate today = LocalDate.now();
		System.out.println(today);

		LocalDate yesterday = today.minusDays(1);
		System.out.println(yesterday);

		LocalDate tomorrow = today.plusDays(1);
		System.out.println(tomorrow);

		LocalDate todayNextYear = today.plusYears(1);
		System.out.println(todayNextYear);

		LocalDateTime currentDateAndTime = LocalDateTime.now();
		System.out.println(currentDateAndTime);

		System.out.println(todayNextYear.isBefore(today));
		System.out.println(todayNextYear.isAfter(today));
		System.out.println(todayNextYear.getMonth());

		LocalDate date = LocalDate.of(1990, 3, 5);
		System.out.println(date);
	}
}
