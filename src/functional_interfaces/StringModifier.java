package functional_interfaces;

@FunctionalInterface
public interface StringModifier {
	String modify(String str);

	// void qualcosa(); // Le interfacce funzionali devono avere UN SOLO METODO ASTRATTO!
}
